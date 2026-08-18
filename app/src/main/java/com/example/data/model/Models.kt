package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

enum class UserRole {
    CUSTOMER,
    WORKER,
    ADMIN
}

enum class BookingStatus {
    PENDING,
    ACCEPTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    REJECTED
}

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी"),
    TELUGU("te", "Telugu", "తెలుగు")
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val phoneNumber: String,
    val name: String,
    val email: String = "",
    val profileImageUrl: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val latitude: Double = 17.4239,
    val longitude: Double = 78.4738,
    val address: String = "Banjara Hills, Hyderabad",
    val favoriteWorkerIds: List<String> = emptyList(),
    val preferredLanguage: String = "en",
    val isBlocked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "workers")
data class WorkerEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val nameLower: String,
    val phoneNumber: String,
    val profileImageUrl: String = "",
    val categoryId: String,
    val categoryName: String,
    val bio: String,
    val experienceYears: Int,
    val hourlyRate: Double,
    val rating: Double = 4.8,
    val totalReviews: Int = 24,
    val completedJobs: Int = 45,
    val isAvailable: Boolean = true,
    val isVerified: Boolean = true,
    val latitude: Double = 17.4239,
    val longitude: Double = 78.4738,
    val address: String = "Hyderabad, Telangana",
    val serviceRadiusKm: Double = 15.0,
    val skills: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

data class ServiceCategory(
    val id: String,
    val name: String,
    val nameHindi: String,
    val nameTelugu: String,
    val iconEmoji: String,
    val colorHex: Long,
    val avgRate: String,
    val unit: String = "/hr",
    val sortOrder: Int = 1
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val workerId: String,
    val workerName: String,
    val workerImageUrl: String = "",
    val categoryId: String,
    val categoryName: String,
    val status: BookingStatus = BookingStatus.PENDING,
    val scheduledAt: Long,
    val durationHours: Double = 2.0,
    val address: String,
    val latitude: Double = 17.4239,
    val longitude: Double = 78.4738,
    val notes: String = "",
    val estimatedCost: Double,
    val finalCost: Double? = null,
    val isEmergency: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val acceptedAt: Long? = null,
    val completedAt: Long? = null,
    val cancellationReason: String? = null
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val workerId: String,
    val customerId: String,
    val customerName: String,
    val customerImageUrl: String = "",
    val bookingId: String,
    val rating: Double,
    val comment: String,
    val praiseTags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val body: String,
    val type: String,
    val bookingId: String? = null,
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class VacancyEntity(
    val id: String,
    val title: String,
    val categoryId: String,
    val categoryName: String,
    val iconEmoji: String,
    val location: String,
    val jobType: String,
    val estimatedEarnings: String,
    val openingsCount: Int,
    val urgency: String,
    val experienceRequired: String,
    val skillsRequired: List<String>,
    val perks: List<String>,
    val description: String,
    val isHotVacancy: Boolean = false
)

data class JobApplicationEntity(
    val id: String,
    val vacancyId: String,
    val vacancyTitle: String,
    val categoryName: String,
    val applicantName: String,
    val applicantPhone: String,
    val applicantEmail: String = "",
    val experienceYears: Int,
    val preferredLocation: String,
    val hasTools: Boolean = true,
    val hasVehicle: Boolean = true,
    val appliedAt: Long = System.currentTimeMillis(),
    val status: String = "Application Received"
)

class Converters {
    private val moshi = Moshi.Builder().build()
    private val listType = Types.newParameterizedType(List::class.java, String::class.java)
    private val adapter = moshi.adapter<List<String>>(listType)

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return adapter.toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            adapter.fromJson(value) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
