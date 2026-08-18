package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.utils.GeoUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class LocalProRepository(private val database: AppDatabase) {

    companion object {
        @Volatile
        private var INSTANCE: LocalProRepository? = null

        fun getInstance(context: Context): LocalProRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getDatabase(context)
                val instance = LocalProRepository(db)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun ensureInitialized() = withContext(Dispatchers.IO) {
        val existingWorkers = database.workerDao().getAllWorkers().firstOrNull()
        if (existingWorkers.isNullOrEmpty() || existingWorkers.size < 35) {
            AppDatabase.populateInitialData(database)
        }
    }

    // User operations
    fun getUser(userId: String): Flow<UserEntity?> = database.userDao().getUserById(userId)
    fun getAllUsers(): Flow<List<UserEntity>> = database.userDao().getAllUsers()

    suspend fun saveUser(user: UserEntity) = withContext(Dispatchers.IO) {
        database.userDao().insertUser(user)
    }

    suspend fun toggleFavoriteWorker(userId: String, workerId: String) = withContext(Dispatchers.IO) {
        val user = database.userDao().getUserById(userId).firstOrNull() ?: return@withContext
        val updatedFavorites = if (user.favoriteWorkerIds.contains(workerId)) {
            user.favoriteWorkerIds - workerId
        } else {
            user.favoriteWorkerIds + workerId
        }
        database.userDao().updateUser(user.copy(favoriteWorkerIds = updatedFavorites))
    }

    // Workers operations
    fun getAllWorkers(): Flow<List<WorkerEntity>> = database.workerDao().getAllWorkers()

    fun getNearbyWorkers(
        userLat: Double,
        userLng: Double,
        categoryId: String? = null,
        query: String = "",
        maxDistanceKm: Double = 30.0
    ): Flow<List<Pair<WorkerEntity, Double>>> {
        return database.workerDao().getAllWorkers().map { workers ->
            workers
                .filter { worker ->
                    (categoryId == null || worker.categoryId == categoryId) &&
                    (query.isBlank() || worker.name.contains(query, ignoreCase = true) ||
                     worker.categoryName.contains(query, ignoreCase = true) ||
                     worker.skills.any { it.contains(query, ignoreCase = true) })
                }
                .map { worker ->
                    val distance = GeoUtils.calculateDistanceKm(userLat, userLng, worker.latitude, worker.longitude)
                    worker to distance
                }
                .filter { (_, distance) -> distance <= maxDistanceKm }
                .sortedBy { (_, distance) -> distance }
        }
    }

    fun getWorkerById(id: String): Flow<WorkerEntity?> = database.workerDao().getWorkerById(id)

    fun getFavoriteWorkers(workerIds: List<String>): Flow<List<WorkerEntity>> =
        database.workerDao().getWorkersByIds(workerIds)

    suspend fun updateWorkerAvailability(workerId: String, isAvailable: Boolean) = withContext(Dispatchers.IO) {
        val worker = database.workerDao().getWorkerById(workerId).firstOrNull() ?: return@withContext
        database.workerDao().updateWorker(worker.copy(isAvailable = isAvailable))
    }

    suspend fun registerWorker(worker: WorkerEntity) = withContext(Dispatchers.IO) {
        database.workerDao().insertWorker(worker)
    }

    // Bookings operations
    fun getAllBookings(): Flow<List<BookingEntity>> = database.bookingDao().getAllBookings()
    fun getBookingsForCustomer(customerId: String): Flow<List<BookingEntity>> = database.bookingDao().getBookingsByCustomer(customerId)
    fun getBookingsForWorker(workerId: String): Flow<List<BookingEntity>> = database.bookingDao().getBookingsByWorker(workerId)
    fun getBookingById(bookingId: String): Flow<BookingEntity?> = database.bookingDao().getBookingById(bookingId)

    suspend fun createBooking(booking: BookingEntity) = withContext(Dispatchers.IO) {
        database.bookingDao().insertBooking(booking)

        // Generate push notification for worker
        database.notificationDao().insertNotification(
            NotificationEntity(
                id = "notif_" + UUID.randomUUID().toString().take(8),
                userId = booking.workerId,
                title = if (booking.isEmergency) "🚨 Emergency Booking Request!" else "New Booking Request",
                body = "${booking.customerName} booked ${booking.categoryName} at ${booking.address}",
                type = "booking_new",
                bookingId = booking.id
            )
        )
    }

    suspend fun updateBookingStatus(
        bookingId: String,
        newStatus: BookingStatus,
        cancellationReason: String? = null
    ) = withContext(Dispatchers.IO) {
        val booking = database.bookingDao().getBookingById(bookingId).firstOrNull() ?: return@withContext
        val updated = booking.copy(
            status = newStatus,
            acceptedAt = if (newStatus == BookingStatus.ACCEPTED && booking.acceptedAt == null) System.currentTimeMillis() else booking.acceptedAt,
            completedAt = if (newStatus == BookingStatus.COMPLETED && booking.completedAt == null) System.currentTimeMillis() else booking.completedAt,
            cancellationReason = cancellationReason ?: booking.cancellationReason
        )
        database.bookingDao().updateBooking(updated)

        // Notify customer
        val title = when (newStatus) {
            BookingStatus.ACCEPTED -> "Booking Accepted 🎉"
            BookingStatus.IN_PROGRESS -> "Worker is On the Way 🚗"
            BookingStatus.COMPLETED -> "Service Completed ✅"
            BookingStatus.CANCELLED -> "Booking Cancelled ❌"
            BookingStatus.REJECTED -> "Booking Declined"
            BookingStatus.PENDING -> "Booking Pending"
        }
        val body = when (newStatus) {
            BookingStatus.ACCEPTED -> "${booking.workerName} has accepted your booking for ${booking.categoryName}."
            BookingStatus.IN_PROGRESS -> "${booking.workerName} started traveling to your location."
            BookingStatus.COMPLETED -> "Your ${booking.categoryName} job is complete. Tap to leave a review."
            BookingStatus.CANCELLED -> "Your booking was cancelled: ${cancellationReason ?: "User requested"}"
            BookingStatus.REJECTED -> "${booking.workerName} was unavailable. You can book another pro."
            BookingStatus.PENDING -> "Your request is awaiting worker confirmation."
        }
        database.notificationDao().insertNotification(
            NotificationEntity(
                id = "notif_" + UUID.randomUUID().toString().take(8),
                userId = booking.customerId,
                title = title,
                body = body,
                type = "status_change",
                bookingId = booking.id
            )
        )

        // If completed, update worker completed jobs count
        if (newStatus == BookingStatus.COMPLETED) {
            val worker = database.workerDao().getWorkerById(booking.workerId).firstOrNull()
            if (worker != null) {
                database.workerDao().updateWorker(worker.copy(completedJobs = worker.completedJobs + 1))
            }
        }
    }

    // Reviews operations
    fun getReviewsForWorker(workerId: String): Flow<List<ReviewEntity>> =
        database.reviewDao().getReviewsForWorker(workerId)

    suspend fun submitReview(review: ReviewEntity) = withContext(Dispatchers.IO) {
        database.reviewDao().insertReview(review)

        // Recalculate worker rolling average rating
        val worker = database.workerDao().getWorkerById(review.workerId).firstOrNull() ?: return@withContext
        val allReviews = database.reviewDao().getReviewsForWorker(review.workerId).firstOrNull() ?: emptyList()
        val totalReviewsCount = allReviews.size
        val newAvgRating = if (totalReviewsCount > 0) {
            allReviews.map { it.rating }.average()
        } else {
            review.rating
        }

        database.workerDao().updateWorker(
            worker.copy(
                rating = String.format("%.1f", newAvgRating).toDouble(),
                totalReviews = totalReviewsCount
            )
        )
    }

    // Chat operations
    fun getMessagesForChat(chatId: String): Flow<List<ChatMessageEntity>> =
        database.chatDao().getMessagesForChat(chatId)

    suspend fun sendMessage(message: ChatMessageEntity) = withContext(Dispatchers.IO) {
        database.chatDao().insertMessage(message)
    }

    // Notifications operations
    fun getNotificationsForUser(userId: String): Flow<List<NotificationEntity>> =
        database.notificationDao().getNotificationsForUser(userId)

    suspend fun markAllNotificationsAsRead(userId: String) = withContext(Dispatchers.IO) {
        database.notificationDao().markAllAsRead(userId)
    }

    suspend fun markNotificationAsRead(notifId: String) = withContext(Dispatchers.IO) {
        database.notificationDao().markAsRead(notifId)
    }
}
