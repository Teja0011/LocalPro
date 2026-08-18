package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.utils.*
import com.example.ui.components.*
import com.example.ui.theme.*
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerDashboardScreen(
    currentWorker: WorkerEntity,
    bookings: List<BookingEntity>,
    onToggleAvailability: (Boolean) -> Unit,
    onAcceptBooking: (String) -> Unit,
    onRejectBooking: (String) -> Unit,
    onStartJob: (String) -> Unit,
    onCompleteJob: (String) -> Unit,
    onViewEarnings: () -> Unit,
    onSwitchToCustomer: () -> Unit
) {
    val pendingBookings = remember(bookings) { bookings.filter { it.status == BookingStatus.PENDING } }
    val activeBookings = remember(bookings) { bookings.filter { it.status == BookingStatus.ACCEPTED || it.status == BookingStatus.IN_PROGRESS } }
    val completedBookings = remember(bookings) { bookings.filter { it.status == BookingStatus.COMPLETED } }
    val totalEarnings = remember(completedBookings) { completedBookings.sumOf { (it.finalCost ?: it.estimatedCost) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Provider Partner Console", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(currentWorker.name, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    TextButton(onClick = onSwitchToCustomer) {
                        Text("Switch to Customer Mode", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("worker_dashboard_scroll"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Availability Online/Offline Toggle Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentWorker.isAvailable) EmeraldContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(if (currentWorker.isAvailable) SuccessEmerald else Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (currentWorker.isAvailable) "You are Online & Receiving Jobs" else "You are Offline",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (currentWorker.isAvailable) Color(0xFF065F46) else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (currentWorker.isAvailable) "Nearby customers can book your service" else "Turn on to get instant service calls",
                                    fontSize = 12.sp,
                                    color = if (currentWorker.isAvailable) Color(0xFF047857) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = currentWorker.isAvailable,
                            onCheckedChange = onToggleAvailability,
                            modifier = Modifier.testTag("worker_availability_switch")
                        )
                    }
                }
            }

            // Quick Stats Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onViewEarnings),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = PrimaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Total Earned", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("₹${totalEarnings.toInt()}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("View Analytics →", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AmberContainer)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Active Requests", fontSize = 12.sp, color = AccentAmberDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${pendingBookings.size + activeBookings.size}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = AccentAmberDark)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${pendingBookings.size} pending confirmation", fontSize = 10.sp, color = AccentAmberDark)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Rating", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("${currentWorker.rating}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${currentWorker.completedJobs} jobs", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // Pending Job Requests (Immediate action required)
            item {
                Text(
                    text = "Incoming Job Requests (${pendingBookings.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (pendingBookings.isEmpty()) {
                item {
                    Text(
                        text = "No pending requests at the moment. When a customer in your radius books, it will show here.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(pendingBookings, key = { it.id }) { booking ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (booking.isEmergency) {
                                        Surface(
                                            color = DangerRed,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "⚡ EMERGENCY",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Text(
                                        text = booking.customerName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                                Text(
                                    text = "₹${booking.estimatedCost.toInt()}",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "📍 ${booking.address}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (booking.notes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "📝 Problem: ${booking.notes}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onRejectBooking(booking.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Decline", color = DangerRed)
                                }

                                Button(
                                    onClick = { onAcceptBooking(booking.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Accept Job", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // In Progress / Active Jobs
            item {
                Text(
                    text = "Active In-Progress Jobs (${activeBookings.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (activeBookings.isEmpty()) {
                item {
                    Text(
                        text = "No jobs currently in progress.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(activeBookings, key = { it.id }) { booking ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(booking.customerName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                StatusBadge(status = booking.status)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("📍 ${booking.address}", fontSize = 13.sp)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (booking.status == BookingStatus.ACCEPTED) {
                                    Button(
                                        onClick = { onStartJob(booking.id) },
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("I've Arrived / Start Job")
                                    }
                                } else if (booking.status == BookingStatus.IN_PROGRESS) {
                                    Button(
                                        onClick = { onCompleteJob(booking.id) },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        val cost = (booking.finalCost ?: booking.estimatedCost).toInt()
                                        Text("Mark Complete & Collect ₹$cost ✅")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerEarningsScreen(
    currentWorker: WorkerEntity,
    completedBookings: List<BookingEntity>,
    onBackClick: () -> Unit
) {
    val totalEarnings = remember(completedBookings) { completedBookings.sumOf { (it.finalCost ?: it.estimatedCost) } }
    val thisWeekEarnings = remember(completedBookings) {
        val weekAgo = System.currentTimeMillis() - 7 * 86400000L
        completedBookings.filter { (it.completedAt ?: 0L) >= weekAgo }.sumOf { (it.finalCost ?: it.estimatedCost) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Earnings & Analytics", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Total Revenue Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryIndigo)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Total Lifetime Revenue", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("₹${totalEarnings.toInt()}", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("This Week", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                Text("₹${thisWeekEarnings.toInt()}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Completed Jobs", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                Text("${completedBookings.size}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Avg Job Value", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                val avg = if (completedBookings.isNotEmpty()) totalEarnings / completedBookings.size else currentWorker.hourlyRate
                                Text("₹${avg.toInt()}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                    }
                }
            }

            // Interactive Weekly Bar Visualizer
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Daily Payout Activity (Last 7 Days)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                            val heights = listOf(0.4f, 0.7f, 0.2f, 0.9f, 0.6f, 1.0f, 0.8f)

                            days.forEachIndexed { i, day ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(24.dp)
                                            .height((heights[i] * 80).dp)
                                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                            .background(if (i == 5) AccentAmber else PrimaryIndigoLight)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(day, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }

            // Completed Invoices History
            item {
                Text("Recent Completed Invoices (${completedBookings.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            if (completedBookings.isEmpty()) {
                item {
                    Text("No completed jobs yet. Complete booking requests to build earnings history.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                items(completedBookings) { b ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(b.customerName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(b.categoryName, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text(
                                text = "+₹${(b.finalCost ?: b.estimatedCost).toInt()}",
                                fontWeight = FontWeight.ExtraBold,
                                color = SuccessEmerald,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerOnboardingScreen(
    onOnboardingComplete: (WorkerEntity) -> Unit,
    onBackClick: () -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(CategoriesData.allCategories[0]) }
    var hourlyRateText by remember { mutableStateOf("350") }
    var experienceYearsText by remember { mutableStateOf("5") }
    var bio by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("Banjara Hills, Hyderabad") }
    var serviceRadius by remember { mutableFloatStateOf(15f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Provider Partner Registration (Step $step of 3)", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { if (step > 1) step-- else onBackClick() }) {
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LinearProgressIndicator(
                progress = { step / 3.0f },
                modifier = Modifier.fillMaxWidth()
            )

            when (step) {
                1 -> {
                    Text("Basic Information", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Enter your contact details so customers can reach you.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        placeholder = { Text("e.g. Suresh Reddy") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        placeholder = { Text("+91 98480 12345") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Base Area / Locality") },
                        placeholder = { Text("e.g. Madhapur, Hyderabad") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { step = 2 },
                        enabled = name.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Next: Category & Rates →", fontWeight = FontWeight.Bold)
                    }
                }

                2 -> {
                    Text("Service Category & Pricing", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                    Text("Select your primary trade", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(CategoriesData.allCategories) { cat ->
                            val isSelected = selectedCategory.id == cat.id
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategory = cat },
                                label = { Text("${cat.iconEmoji} ${cat.name}") }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = hourlyRateText,
                        onValueChange = { hourlyRateText = it.filter { c -> c.isDigit() } },
                        label = { Text("Hourly Rate (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = experienceYearsText,
                        onValueChange = { experienceYearsText = it.filter { c -> c.isDigit() } },
                        label = { Text("Years of Experience") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { step = 3 },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Next: Bio & Coverage →", fontWeight = FontWeight.Bold)
                    }
                }

                3 -> {
                    Text("Profile Bio & Service Radius", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Bio / Description") },
                        placeholder = { Text("Tell customers about your expertise, background and tools...") },
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Service Coverage Radius", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("${serviceRadius.toInt()} km", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Slider(
                            value = serviceRadius,
                            onValueChange = { serviceRadius = it },
                            valueRange = 5f..35f,
                            steps = 6
                        )
                    }

                    Button(
                        onClick = {
                            val newWorker = WorkerEntity(
                                id = "w_" + UUID.randomUUID().toString().take(6),
                                userId = "usr_w_new",
                                name = name.ifBlank { "Pro Partner" },
                                nameLower = name.lowercase().ifBlank { "pro partner" },
                                phoneNumber = phone.ifBlank { "+91 98765 00000" },
                                categoryId = selectedCategory.id,
                                categoryName = selectedCategory.name,
                                bio = bio.ifBlank { "Professional ${selectedCategory.name} with ${experienceYearsText} years experience." },
                                experienceYears = experienceYearsText.toIntOrNull() ?: 3,
                                hourlyRate = hourlyRateText.toDoubleOrNull() ?: 350.0,
                                rating = 5.0,
                                totalReviews = 1,
                                completedJobs = 1,
                                isAvailable = true,
                                isVerified = true,
                                latitude = 17.4239,
                                longitude = 78.4738,
                                address = address.ifBlank { "Hyderabad" },
                                serviceRadiusKm = serviceRadius.toDouble(),
                                skills = listOf("Rapid Dispatch", "General Repairs", "Quality Guarantee")
                            )
                            onOnboardingComplete(newWorker)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("submit_provider_onboarding_btn")
                    ) {
                        Text("Complete & Launch Console 🚀", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    allWorkers: List<WorkerEntity>,
    allBookings: List<BookingEntity>,
    onToggleWorkerVerification: (WorkerEntity) -> Unit,
    onBackClick: () -> Unit
) {
    val totalRevenue = remember(allBookings) { allBookings.sumOf { (it.finalCost ?: it.estimatedCost) } }
    val platformCut = totalRevenue * 0.05

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LocalPro Admin Console", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Platform KPI Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = PrimaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Total GMV", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            Text("₹${totalRevenue.toInt()}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                            Text("Fee: ₹${platformCut.toInt()}", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = EmeraldContainer)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Total Bookings", fontSize = 12.sp, color = Color(0xFF065F46))
                            Text("${allBookings.size}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF065F46))
                            Text("Across Hyderabad", fontSize = 10.sp, color = Color(0xFF047857))
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = AmberContainer)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Active Pros", fontSize = 12.sp, color = AccentAmberDark)
                            Text("${allWorkers.size}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = AccentAmberDark)
                            Text("18 Categories", fontSize = 10.sp, color = AccentAmberDark)
                        }
                    }
                }
            }

            // Pro Verification Queue
            item {
                Text("Service Provider Verification & Status", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            items(allWorkers, key = { it.id }) { worker ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(worker.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                if (worker.isVerified) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = PrimaryIndigoLight, modifier = Modifier.size(16.dp))
                                }
                            }
                            Text("${worker.categoryName} • ${worker.address}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Button(
                            onClick = { onToggleWorkerVerification(worker) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (worker.isVerified) DangerRed else SuccessEmerald
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(if (worker.isVerified) "Revoke" else "Approve Pro", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
