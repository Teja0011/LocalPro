package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.LocalProRepository
import com.example.ui.screens.LocationOption
import com.example.ui.screens.hyderabadLocations
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LocalProRepository.getInstance(application)

    val allWorkers: StateFlow<List<WorkerEntity>> = repository.getAllWorkers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookings: StateFlow<List<BookingEntity>> = repository.getAllBookings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentUser = MutableStateFlow(
        UserEntity(
            id = "cust_001",
            phoneNumber = "+91 98765 43210",
            name = "Ravi Kumar",
            email = "ravi.kumar@example.com",
            role = UserRole.CUSTOMER,
            latitude = 17.4239,
            longitude = 78.4738,
            address = "Road No. 12, Banjara Hills, Hyderabad",
            favoriteWorkerIds = listOf("w_001", "w_002")
        )
    )
    val currentUser: StateFlow<UserEntity> = _currentUser.asStateFlow()

    private val _currentLocation = MutableStateFlow(hyderabadLocations[0])
    val currentLocation: StateFlow<LocationOption> = _currentLocation.asStateFlow()

    private val _appLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Provider partner profile (for provider console demo)
    private val _currentWorkerProfile = MutableStateFlow(
        WorkerEntity(
            id = "w_001",
            userId = "usr_w_001",
            name = "Suresh Reddy",
            nameLower = "suresh reddy",
            phoneNumber = "+91 98480 12345",
            categoryId = "electrician",
            categoryName = "Electrician",
            bio = "Certified master electrician with 8+ years experience in domestic wiring, switchboard repairs, and inverter setups.",
            experienceYears = 8,
            hourlyRate = 350.0,
            rating = 4.9,
            totalReviews = 142,
            completedJobs = 310,
            isAvailable = true,
            isVerified = true,
            latitude = 17.4250,
            longitude = 78.4710,
            address = "Banjara Hills, Hyderabad",
            serviceRadiusKm = 12.0,
            skills = listOf("Inverter Setup", "MCB Tripping Fix", "Fan & Light Fitting", "Appliance Wiring")
        )
    )
    val currentWorkerProfile: StateFlow<WorkerEntity> = _currentWorkerProfile.asStateFlow()

    // Notifications
    val notifications: StateFlow<List<NotificationEntity>> = repository.getNotificationsForUser("cust_001")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = notifications.map { list ->
        list.count { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Vacancies & Partner Job Openings
    private val _allVacancies = MutableStateFlow<List<VacancyEntity>>(com.example.ui.screens.sampleVacancies)
    val allVacancies: StateFlow<List<VacancyEntity>> = _allVacancies.asStateFlow()

    private val _submittedApplications = MutableStateFlow<List<JobApplicationEntity>>(
        listOf(
            JobApplicationEntity(
                id = "LP-HYD-71284",
                vacancyId = "vac_001",
                vacancyTitle = "Master Electrician & Inverter Specialist",
                categoryName = "Electrician",
                applicantName = "Ravi Kumar",
                applicantPhone = "+91 98765 43210",
                experienceYears = 3,
                preferredLocation = "Banjara Hills & Jubilee Hills Zone",
                appliedAt = System.currentTimeMillis() - 86400000L,
                status = "Under Review"
            )
        )
    )
    val submittedApplications: StateFlow<List<JobApplicationEntity>> = _submittedApplications.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureInitialized()
            // Observe repository user changes
            repository.getUser("cust_001").collect { user ->
                if (user != null) {
                    _currentUser.value = user
                }
            }
        }
    }

    fun setLocation(location: LocationOption) {
        _currentLocation.value = location
        _currentUser.value = _currentUser.value.copy(
            latitude = location.lat,
            longitude = location.lng,
            address = location.name
        )
    }

    fun setLanguage(language: AppLanguage) {
        _appLanguage.value = language
    }

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    fun loginUser(user: UserEntity) {
        _currentUser.value = user
        viewModelScope.launch {
            repository.saveUser(user)
        }
    }

    fun toggleFavorite(workerId: String) {
        viewModelScope.launch {
            repository.toggleFavoriteWorker(_currentUser.value.id, workerId)
        }
    }

    fun createBooking(booking: BookingEntity, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.createBooking(booking)
            onDone()
        }
    }

    fun updateBookingStatus(bookingId: String, newStatus: BookingStatus, cancellationReason: String? = null) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, newStatus, cancellationReason)
        }
    }

    fun submitReview(review: ReviewEntity, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.submitReview(review)
            onDone()
        }
    }

    fun getReviewsForWorker(workerId: String): Flow<List<ReviewEntity>> {
        return repository.getReviewsForWorker(workerId)
    }

    fun getChatMessages(chatId: String): Flow<List<ChatMessageEntity>> {
        return repository.getMessagesForChat(chatId)
    }

    fun sendChatMessage(chatId: String, senderId: String, senderName: String, text: String) {
        viewModelScope.launch {
            val msg = ChatMessageEntity(
                id = "msg_" + UUID.randomUUID().toString().take(8),
                chatId = chatId,
                senderId = senderId,
                senderName = senderName,
                text = text,
                timestamp = System.currentTimeMillis()
            )
            repository.sendMessage(msg)
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead(_currentUser.value.id)
        }
    }

    fun updateWorkerAvailability(isAvailable: Boolean) {
        _currentWorkerProfile.value = _currentWorkerProfile.value.copy(isAvailable = isAvailable)
        viewModelScope.launch {
            repository.updateWorkerAvailability(_currentWorkerProfile.value.id, isAvailable)
        }
    }

    fun registerNewWorker(worker: WorkerEntity) {
        _currentWorkerProfile.value = worker
        viewModelScope.launch {
            repository.registerWorker(worker)
        }
    }

    fun toggleWorkerVerification(worker: WorkerEntity) {
        viewModelScope.launch {
            repository.registerWorker(worker.copy(isVerified = !worker.isVerified))
        }
    }

    fun updateUserProfile(user: UserEntity) {
        _currentUser.value = user
        viewModelScope.launch {
            repository.saveUser(user)
        }
    }

    fun submitJobApplication(application: JobApplicationEntity) {
        _submittedApplications.value = listOf(application) + _submittedApplications.value
    }
}
