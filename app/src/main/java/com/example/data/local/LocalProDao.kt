package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)
}

@Dao
interface WorkerDao {
    @Query("SELECT * FROM workers")
    fun getAllWorkers(): Flow<List<WorkerEntity>>

    @Query("SELECT * FROM workers WHERE id = :id LIMIT 1")
    fun getWorkerById(id: String): Flow<WorkerEntity?>

    @Query("SELECT * FROM workers WHERE categoryId = :categoryId")
    fun getWorkersByCategory(categoryId: String): Flow<List<WorkerEntity>>

    @Query("SELECT * FROM workers WHERE isAvailable = 1")
    fun getAvailableWorkers(): Flow<List<WorkerEntity>>

    @Query("SELECT * FROM workers WHERE id IN (:workerIds)")
    fun getWorkersByIds(workerIds: List<String>): Flow<List<WorkerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkers(workers: List<WorkerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorker(worker: WorkerEntity)

    @Update
    suspend fun updateWorker(worker: WorkerEntity)
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :id LIMIT 1")
    fun getBookingById(id: String): Flow<BookingEntity?>

    @Query("SELECT * FROM bookings WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun getBookingsByCustomer(customerId: String): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE workerId = :workerId ORDER BY createdAt DESC")
    fun getBookingsByWorker(workerId: String): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Update
    suspend fun updateBooking(booking: BookingEntity)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE workerId = :workerId ORDER BY createdAt DESC")
    fun getReviewsForWorker(workerId: String): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesForChat(chatId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    fun getNotificationsForUser(userId: String): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: String)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)
}
