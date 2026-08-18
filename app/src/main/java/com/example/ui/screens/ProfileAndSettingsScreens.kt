package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.utils.*
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentUser: UserEntity,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToFavourites: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToHelpFaq: () -> Unit,
    onNavigateToNotificationSettings: () -> Unit,
    onSwitchToWorkerConsole: () -> Unit,
    onNavigateToAdminDashboard: () -> Unit,
    onNavigateToVacancies: () -> Unit = {},
    onSignOut: () -> Unit
) {
    val language = LocalAppLanguage.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Strings.get("profile", language), fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.joinToString("").ifBlank { "U" },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentUser.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentUser.phoneNumber,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (currentUser.email.isNotBlank()) {
                            Text(
                                text = currentUser.email,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onNavigateToEditProfile) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // Hiring / Vacancies Partner Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryContainerBlue),
                modifier = Modifier.clickable(onClick = onNavigateToVacancies)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Text("💼", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Partner Jobs & Vacancies", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimarySapphire)
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = AccentAmber,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        "HIRING",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 9.sp,
                                        color = Color.Black,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text("Earn ₹35k - ₹70k/month in your area", fontSize = 11.sp, color = PrimarySapphireDark)
                        }
                    }
                    Button(
                        onClick = onNavigateToVacancies,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimarySapphire),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Explore", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Quick Role Switcher Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AmberContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Text("🔧", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Service Provider Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AccentAmberDark)
                            Text("Manage jobs, availability & earnings", fontSize = 11.sp, color = Color(0xFF78350F))
                        }
                    }
                    Button(
                        onClick = onSwitchToWorkerConsole,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentAmberDark),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Open Console", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Settings Group 1: Preferences & Content
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    ProfileMenuItem(
                        icon = Icons.Default.Favorite,
                        title = Strings.get("favourites", language),
                        subtitle = "${currentUser.favoriteWorkerIds.size} saved pros",
                        onClick = onNavigateToFavourites
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                    ProfileMenuItem(
                        icon = Icons.Default.Language,
                        title = Strings.get("language", language),
                        subtitle = when (language) {
                            AppLanguage.ENGLISH -> "English (EN)"
                            AppLanguage.HINDI -> "हिंदी (Hindi)"
                            AppLanguage.TELUGU -> "తెలుగు (Telugu)"
                        },
                        onClick = onNavigateToLanguage
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                    ProfileMenuItem(
                        icon = Icons.Default.Notifications,
                        title = "Notification Preferences",
                        subtitle = "Alerts for bookings, pros & offers",
                        onClick = onNavigateToNotificationSettings
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                    // Dark Mode Toggle Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DarkMode, contentDescription = null, tint = PrimaryIndigoLight, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("Dark Mode", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                Text("High contrast theme", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = onToggleDarkMode
                        )
                    }
                }
            }

            // Settings Group 2: Support & Admin
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    ProfileMenuItem(
                        icon = Icons.Default.HelpOutline,
                        title = Strings.get("help_support", language),
                        subtitle = "FAQs & 24x7 Customer Care",
                        onClick = onNavigateToHelpFaq
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                    ProfileMenuItem(
                        icon = Icons.Default.AdminPanelSettings,
                        title = "Admin Dashboard (Demo)",
                        subtitle = "Platform metrics & pro verifications",
                        onClick = onNavigateToAdminDashboard
                    )
                }
            }

            // Sign Out Button
            OutlinedButton(
                onClick = onSignOut,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("sign_out_btn")
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(Strings.get("sign_out", language), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryIndigoLight,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    currentUser: UserEntity,
    onSaveProfile: (UserEntity) -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf(currentUser.name) }
    var email by remember { mutableStateOf(currentUser.email) }
    var address by remember { mutableStateOf(currentUser.address) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Home / Service Address") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onSaveProfile(currentUser.copy(name = name, email = email, address = address))
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    favoriteWorkers: List<WorkerEntity>,
    onWorkerClick: (String) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onBookWorker: (WorkerEntity) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Favorites", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (favoriteWorkers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("❤️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No saved favorites yet", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Tap the heart icon on any technician or cleaner's card to quickly rebook them here.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteWorkers, key = { it.id }) { worker ->
                    WorkerCard(
                        worker = worker,
                        isFavorite = true,
                        onFavoriteToggle = { onFavoriteToggle(worker.id) },
                        onClick = { onWorkerClick(worker.id) },
                        onBookClick = { onBookWorker(worker) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onBackClick: () -> Unit
) {
    val languages = listOf(
        Triple(AppLanguage.ENGLISH, "English", "Default (English)"),
        Triple(AppLanguage.HINDI, "हिंदी", "Hindi"),
        Triple(AppLanguage.TELUGU, "తెలుగు", "Telugu (Hyderabad & Telangana)")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose Language", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            languages.forEach { (lang, title, subtitle) ->
                val isSelected = currentLanguage == lang
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLanguageSelected(lang) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) PrimaryContainer else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        RadioButton(
                            selected = isSelected,
                            onClick = { onLanguageSelected(lang) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpFaqScreen(
    onBackClick: () -> Unit
) {
    val faqs = listOf(
        "How do emergency bookings work?" to "Emergency bookings are dispatched with top priority to the nearest available professional within 15-30 minutes. A 20% priority surcharge applies.",
        "How does pricing work?" to "Professionals set their verified standard hourly rates. You only pay after work completion via UPI, Cash, or Card. No advance deposit is ever needed.",
        "Are all LocalPro service partners verified?" to "Yes! Every technician undergoes identity verification, criminal background check, and trade license verification before accepting jobs.",
        "Can I cancel a booking request?" to "You can freely cancel any request while in 'Pending' status with zero fees.",
        "How does the rolling rating work?" to "Every completed job prompts the customer for a 1-5 star review and praise tags, which updates the professional's rolling average rating immediately."
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Support", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Headphones, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("24x7 Customer Support", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryIndigo)
                            Text("Need immediate assistance with a booking? Call 1800-LOCAL-PRO", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }
            }

            item {
                Text("Frequently Asked Questions", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            items(faqs) { (q, a) ->
                var expanded by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { expanded = !expanded },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(q, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (expanded) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(a, fontSize = 13.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onBackClick: () -> Unit
) {
    var bookingUpdates by remember { mutableStateOf(true) }
    var chatMessages by remember { mutableStateOf(true) }
    var promotions by remember { mutableStateOf(true) }
    var nearbyProAlerts by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Preferences", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NotificationToggleCard(
                title = "Booking Status Updates",
                desc = "Get real-time push alerts when a pro accepts or starts your job",
                checked = bookingUpdates,
                onCheckedChange = { bookingUpdates = it }
            )
            NotificationToggleCard(
                title = "In-App Chat Messages",
                desc = "Direct messages and directions from service partners",
                checked = chatMessages,
                onCheckedChange = { chatMessages = it }
            )
            NotificationToggleCard(
                title = "Promotions & Discounts",
                desc = "Seasonal discounts on AC service, pest control, and deep cleaning",
                checked = promotions,
                onCheckedChange = { promotions = it }
            )
            NotificationToggleCard(
                title = "Nearby Pro Alerts",
                desc = "Notifications when top-rated specialists are in your neighborhood",
                checked = nearbyProAlerts,
                onCheckedChange = { nearbyProAlerts = it }
            )
        }
    }
}

@Composable
fun NotificationToggleCard(
    title: String,
    desc: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
