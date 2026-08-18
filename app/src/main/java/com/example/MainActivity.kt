package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.data.model.*
import com.example.data.utils.*
import com.example.ui.screens.*
import com.example.ui.theme.LocalProTheme
import com.example.ui.viewmodel.MainViewModel

sealed class BottomNavItem(val route: String, val titleKey: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    data object Home : BottomNavItem("home", "home", Icons.Filled.Home, Icons.Outlined.Home)
    data object Bookings : BottomNavItem("my_bookings", "bookings", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth)
    data object Profile : BottomNavItem("profile", "profile", Icons.Filled.Person, Icons.Outlined.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val appLanguage by viewModel.appLanguage.collectAsState()

            CompositionLocalProvider(LocalAppLanguage provides appLanguage) {
                LocalProTheme(darkTheme = isDarkMode) {
                    LocalProApp(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalProApp(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentUser by viewModel.currentUser.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val allWorkers by viewModel.allWorkers.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val unreadNotifCount by viewModel.unreadNotificationsCount.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val currentWorkerProfile by viewModel.currentWorkerProfile.collectAsState()

    val language = LocalAppLanguage.current

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Bookings,
        BottomNavItem.Profile
    )

    val showBottomBar = currentRoute in listOf(
        BottomNavItem.Home.route,
        BottomNavItem.Bookings.route,
        BottomNavItem.Profile.route
    )

    val nearbyWorkersWithDistance = remember(allWorkers, currentLocation) {
        allWorkers.map { worker ->
            val dist = GeoUtils.calculateDistanceKm(
                currentLocation.lat, currentLocation.lng,
                worker.latitude, worker.longitude
            )
            worker to dist
        }.sortedBy { it.second }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 0.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        val label = Strings.get(item.titleKey, language)

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = label
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontWeight = if (selected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Medium,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = com.example.ui.theme.OnPrimaryContainerPurple,
                                selectedTextColor = com.example.ui.theme.OnPrimaryContainerPurple,
                                indicatorColor = com.example.ui.theme.PrimaryContainerLight,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_${item.route}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Splash Screen
            composable("splash") {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate("auth") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            // 2. Auth Screen
            composable("auth") {
                AuthScreen(
                    onLoginSuccess = { user ->
                        viewModel.loginUser(user)
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    },
                    onNavigateToWorkerOnboarding = {
                        navController.navigate("worker_onboarding")
                    }
                )
            }

            // 3. Home Screen
            composable("home") {
                HomeScreen(
                    currentUser = currentUser,
                    nearbyWorkersWithDistance = nearbyWorkersWithDistance,
                    unreadNotificationCount = unreadNotifCount,
                    onSearchClick = { navController.navigate("search") },
                    onCategoryClick = { catId -> navController.navigate("search?categoryId=$catId") },
                    onViewAllCategories = { navController.navigate("categories") },
                    onWorkerClick = { workerId -> navController.navigate("worker_detail/$workerId") },
                    onBookWorkerClick = { worker -> navController.navigate("booking/${worker.id}") },
                    onEmergencyClick = {
                        // Select electrician / plumber as quick emergency request
                        val emergencyWorker = allWorkers.firstOrNull { it.isAvailable } ?: allWorkers.firstOrNull()
                        if (emergencyWorker != null) {
                            navController.navigate("booking/${emergencyWorker.id}")
                        } else {
                            navController.navigate("categories")
                        }
                    },
                    onNotificationsClick = { navController.navigate("notifications") },
                    onFavoriteToggle = { workerId -> viewModel.toggleFavorite(workerId) },
                    onLocationChanged = { location -> viewModel.setLocation(location) },
                    onVacanciesClick = { navController.navigate("vacancies") }
                )
            }

            // 4. Search Screen
            composable(
                route = "search?categoryId={categoryId}",
                arguments = listOf(navArgument("categoryId") { type = NavType.StringType; nullable = true })
            ) { backStackEntry ->
                val preselectedCategory = backStackEntry.arguments?.getString("categoryId")
                SearchScreen(
                    currentUser = currentUser,
                    allWorkers = allWorkers,
                    preselectedCategory = preselectedCategory,
                    onBackClick = { navController.popBackStack() },
                    onWorkerClick = { workerId -> navController.navigate("worker_detail/$workerId") },
                    onBookWorkerClick = { worker -> navController.navigate("booking/${worker.id}") },
                    onFavoriteToggle = { workerId -> viewModel.toggleFavorite(workerId) }
                )
            }

            // 5. Categories Grid Screen
            composable("categories") {
                CategoriesScreen(
                    onCategoryClick = { catId ->
                        navController.navigate("search?categoryId=$catId")
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 6. Worker Detail Screen
            composable(
                route = "worker_detail/{workerId}",
                arguments = listOf(navArgument("workerId") { type = NavType.StringType })
            ) { backStackEntry ->
                val workerId = backStackEntry.arguments?.getString("workerId") ?: ""
                val worker = allWorkers.firstOrNull { it.id == workerId }
                val reviewsFlow = remember(workerId) { viewModel.getReviewsForWorker(workerId) }
                val reviews by reviewsFlow.collectAsState(initial = emptyList())

                if (worker != null) {
                    WorkerDetailScreen(
                        worker = worker,
                        reviews = reviews,
                        isFavorite = currentUser.favoriteWorkerIds.contains(worker.id),
                        onFavoriteToggle = { viewModel.toggleFavorite(worker.id) },
                        onBackClick = { navController.popBackStack() },
                        onBookClick = { navController.navigate("booking/${worker.id}") },
                        onChatClick = {
                            val chatId = "${currentUser.id}_${worker.id}"
                            navController.navigate("chat/$chatId/${worker.name}")
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // 7. Booking Creation Screen
            composable(
                route = "booking/{workerId}",
                arguments = listOf(navArgument("workerId") { type = NavType.StringType })
            ) { backStackEntry ->
                val workerId = backStackEntry.arguments?.getString("workerId") ?: ""
                val worker = allWorkers.firstOrNull { it.id == workerId }

                if (worker != null) {
                    BookingScreen(
                        worker = worker,
                        currentUser = currentUser,
                        onBackClick = { navController.popBackStack() },
                        onBookingConfirmed = { newBooking ->
                            viewModel.createBooking(newBooking) {
                                navController.navigate("booking_confirmation/${newBooking.id}") {
                                    popUpTo("home")
                                }
                            }
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // 8. Booking Live Status & Confirmation Screen
            composable(
                route = "booking_confirmation/{bookingId}",
                arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
            ) { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
                val booking = allBookings.firstOrNull { it.id == bookingId }

                if (booking != null) {
                    BookingConfirmationScreen(
                        booking = booking,
                        onStatusAdvance = { nextStatus ->
                            viewModel.updateBookingStatus(booking.id, nextStatus)
                        },
                        onCancelBooking = { id ->
                            viewModel.updateBookingStatus(id, BookingStatus.CANCELLED, "Customer cancelled request")
                            navController.popBackStack()
                        },
                        onBackClick = { navController.popBackStack() },
                        onChatClick = {
                            val chatId = "${booking.customerId}_${booking.workerId}"
                            navController.navigate("chat/$chatId/${booking.workerName}")
                        },
                        onLeaveReviewClick = {
                            navController.navigate("review/${booking.workerId}/${booking.workerName}")
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // 9. My Bookings History Screen
            composable("my_bookings") {
                BookingHistoryScreen(
                    bookings = allBookings,
                    onBookingClick = { b -> navController.navigate("booking_confirmation/${b.id}") },
                    onCancelBooking = { id -> viewModel.updateBookingStatus(id, BookingStatus.CANCELLED) },
                    onReviewClick = { b -> navController.navigate("review/${b.workerId}/${b.workerName}") }
                )
            }

            // 10. Chat Message Thread Screen
            composable(
                route = "chat/{chatId}/{recipientName}",
                arguments = listOf(
                    navArgument("chatId") { type = NavType.StringType },
                    navArgument("recipientName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                val recipientName = backStackEntry.arguments?.getString("recipientName") ?: "LocalPro User"
                val chatFlow = remember(chatId) { viewModel.getChatMessages(chatId) }
                val messages by chatFlow.collectAsState(initial = emptyList())

                ChatScreen(
                    chatId = chatId,
                    recipientName = recipientName,
                    currentUserId = currentUser.id,
                    messages = messages,
                    onSendMessage = { text ->
                        viewModel.sendChatMessage(chatId, currentUser.id, currentUser.name, text)
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 11. Notifications Feed Screen
            composable("notifications") {
                NotificationsScreen(
                    notifications = notifications,
                    onMarkAllRead = { viewModel.markAllNotificationsRead() },
                    onNotificationClick = { notif ->
                        if (notif.bookingId != null) {
                            navController.navigate("booking_confirmation/${notif.bookingId}")
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 12. Profile Screen
            composable("profile") {
                ProfileScreen(
                    currentUser = currentUser,
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { viewModel.toggleDarkMode(it) },
                    onNavigateToEditProfile = { navController.navigate("edit_profile") },
                    onNavigateToFavourites = { navController.navigate("favourites") },
                    onNavigateToLanguage = { navController.navigate("language") },
                    onNavigateToHelpFaq = { navController.navigate("help_faq") },
                    onNavigateToNotificationSettings = { navController.navigate("notification_settings") },
                    onSwitchToWorkerConsole = { navController.navigate("worker_dashboard") },
                    onNavigateToAdminDashboard = { navController.navigate("admin_dashboard") },
                    onNavigateToVacancies = { navController.navigate("vacancies") },
                    onSignOut = {
                        navController.navigate("auth") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }

            // 13. Edit Profile Screen
            composable("edit_profile") {
                EditProfileScreen(
                    currentUser = currentUser,
                    onSaveProfile = { updated ->
                        viewModel.updateUserProfile(updated)
                        navController.popBackStack()
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 14. Favourites Screen
            composable("favourites") {
                val favWorkers = remember(allWorkers, currentUser.favoriteWorkerIds) {
                    allWorkers.filter { currentUser.favoriteWorkerIds.contains(it.id) }
                }
                FavouritesScreen(
                    favoriteWorkers = favWorkers,
                    onWorkerClick = { workerId -> navController.navigate("worker_detail/$workerId") },
                    onFavoriteToggle = { workerId -> viewModel.toggleFavorite(workerId) },
                    onBookWorker = { worker -> navController.navigate("booking/${worker.id}") },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 15. Language Selector Screen
            composable("language") {
                val currentLang by viewModel.appLanguage.collectAsState()
                LanguageScreen(
                    currentLanguage = currentLang,
                    onLanguageSelected = { lang ->
                        viewModel.setLanguage(lang)
                        navController.popBackStack()
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 16. Help & FAQ Screen
            composable("help_faq") {
                HelpFaqScreen(onBackClick = { navController.popBackStack() })
            }

            // 17. Notification Settings Screen
            composable("notification_settings") {
                NotificationSettingsScreen(onBackClick = { navController.popBackStack() })
            }

            // 18. Rate & Review Screen
            composable(
                route = "review/{workerId}/{workerName}",
                arguments = listOf(
                    navArgument("workerId") { type = NavType.StringType },
                    navArgument("workerName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val workerId = backStackEntry.arguments?.getString("workerId") ?: ""
                val workerName = backStackEntry.arguments?.getString("workerName") ?: "Provider"

                ReviewScreen(
                    workerId = workerId,
                    workerName = workerName,
                    currentUser = currentUser,
                    onBackClick = { navController.popBackStack() },
                    onSubmitReview = { review ->
                        viewModel.submitReview(review) {
                            navController.popBackStack()
                        }
                    }
                )
            }

            // 19. Provider Onboarding Wizard
            composable("worker_onboarding") {
                WorkerOnboardingScreen(
                    onOnboardingComplete = { newWorker ->
                        viewModel.registerNewWorker(newWorker)
                        navController.navigate("worker_dashboard") {
                            popUpTo("home")
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 20. Provider Partner Dashboard
            composable("worker_dashboard") {
                val workerBookings = remember(allBookings, currentWorkerProfile.id) {
                    allBookings.filter { it.workerId == currentWorkerProfile.id || it.categoryId == currentWorkerProfile.categoryId }
                }

                WorkerDashboardScreen(
                    currentWorker = currentWorkerProfile,
                    bookings = workerBookings,
                    onToggleAvailability = { viewModel.updateWorkerAvailability(it) },
                    onAcceptBooking = { id -> viewModel.updateBookingStatus(id, BookingStatus.ACCEPTED) },
                    onRejectBooking = { id -> viewModel.updateBookingStatus(id, BookingStatus.REJECTED) },
                    onStartJob = { id -> viewModel.updateBookingStatus(id, BookingStatus.IN_PROGRESS) },
                    onCompleteJob = { id -> viewModel.updateBookingStatus(id, BookingStatus.COMPLETED) },
                    onViewEarnings = { navController.navigate("worker_earnings") },
                    onSwitchToCustomer = { navController.popBackStack() }
                )
            }

            // 21. Provider Earnings & Analytics
            composable("worker_earnings") {
                val completed = remember(allBookings, currentWorkerProfile.id) {
                    allBookings.filter { (it.workerId == currentWorkerProfile.id || it.categoryId == currentWorkerProfile.categoryId) && it.status == BookingStatus.COMPLETED }
                }
                WorkerEarningsScreen(
                    currentWorker = currentWorkerProfile,
                    completedBookings = completed,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 22. Admin Dashboard Console
            composable("admin_dashboard") {
                AdminDashboardScreen(
                    allWorkers = allWorkers,
                    allBookings = allBookings,
                    onToggleWorkerVerification = { worker -> viewModel.toggleWorkerVerification(worker) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 23. Vacancies & Partner Hiring Screen
            composable("vacancies") {
                val vacancies by viewModel.allVacancies.collectAsState()
                val submittedApps by viewModel.submittedApplications.collectAsState()
                VacanciesScreen(
                    currentUser = currentUser,
                    vacancies = vacancies,
                    submittedApplications = submittedApps,
                    onSubmitApplication = { application ->
                        viewModel.submitJobApplication(application)
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
