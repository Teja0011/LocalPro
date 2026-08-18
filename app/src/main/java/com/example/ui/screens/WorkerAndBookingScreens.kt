package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.utils.*
import com.example.ui.components.*
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerDetailScreen(
    worker: WorkerEntity,
    reviews: List<ReviewEntity>,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    onChatClick: () -> Unit
) {
    val context = LocalContext.current
    val language = LocalAppLanguage.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(worker.name, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onFavoriteToggle) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) DangerRed else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "Check out ${worker.name} (${worker.categoryName}) on LocalPro! Rating: ${worker.rating}★")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Pro Profile"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Call Button
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${worker.phoneNumber}")
                            }
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Calling ${worker.phoneNumber}", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(0.8f),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Call", fontWeight = FontWeight.Bold)
                    }

                    // Chat Button
                    OutlinedButton(
                        onClick = onChatClick,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(0.8f),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Chat", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Chat", fontWeight = FontWeight.Bold)
                    }

                    // Book Now Button
                    Button(
                        onClick = onBookClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1.4f)
                            .testTag("worker_detail_book_btn"),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text(Strings.get("book_now", language), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("worker_detail_scroll"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Profile Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape)
                                .background(PrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            val initials = worker.name.split(" ")
                                .take(2)
                                .mapNotNull { it.firstOrNull()?.toString() }
                                .joinToString("")
                            Text(
                                text = if (initials.isNotEmpty()) initials else "LP",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = worker.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            if (worker.isVerified) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = PrimaryIndigoLight,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Text(
                            text = worker.categoryName,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            color = if (worker.isAvailable) EmeraldContainer else Color(0xFFF3F4F6),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (worker.isAvailable) "● ${Strings.get("available_now", language)}" else "● ${Strings.get("busy", language)}",
                                color = if (worker.isAvailable) SuccessEmerald else Color(0xFF6B7280),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // 3 Stat Metrics
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("${worker.rating}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                                Text("${worker.totalReviews} reviews", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            Box(modifier = Modifier.height(30.dp).width(1.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)))

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${worker.experienceYears} Years", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(Strings.get("experience", language), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            Box(modifier = Modifier.height(30.dp).width(1.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)))

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${worker.completedJobs}+", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(Strings.get("completed_jobs", language), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // Pricing Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Standard Hourly Rate",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₹${worker.hourlyRate.toInt()} / hour",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Surface(
                            color = PrimaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "No Advance Required",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // About & Bio
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "About Professional",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = worker.bio,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Skills & Specialties
            if (worker.skills.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = Strings.get("skills", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                worker.skills.forEach { skill ->
                                    Surface(
                                        color = PrimaryContainer,
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = "✓ $skill",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Service Area Map Preview
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = Strings.get("service_area", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Servicing within ${worker.serviceRadiusKm.toInt()}km of ${worker.address}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Visual Map Representation
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.linearGradient(listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryIndigo),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Place, contentDescription = null, tint = Color.White)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = worker.address,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LightTextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Customer Reviews Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${Strings.get("reviews", language)} (${reviews.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("${worker.rating}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            if (reviews.isEmpty()) {
                item {
                    Text(
                        text = "No reviews yet for this professional.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(reviews) { review ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = review.customerName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    for (i in 1..5) {
                                        Icon(
                                            imageVector = if (i <= review.rating) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = null,
                                            tint = if (i <= review.rating) AccentAmber else MaterialTheme.colorScheme.outline,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = review.comment,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )

                            if (review.praiseTags.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    review.praiseTags.forEach { tag ->
                                        Surface(
                                            color = EmeraldContainer,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "✓ $tag",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF065F46),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    worker: WorkerEntity,
    currentUser: UserEntity,
    onBackClick: () -> Unit,
    onBookingConfirmed: (BookingEntity) -> Unit
) {
    val language = LocalAppLanguage.current
    var selectedDateIndex by remember { mutableIntStateOf(0) }
    var selectedTimeSlot by remember { mutableStateOf("10:00 AM") }
    var durationHours by remember { mutableDoubleStateOf(2.0) }
    var address by remember { mutableStateOf(currentUser.address) }
    var notes by remember { mutableStateOf("") }
    var isEmergency by remember { mutableStateOf(false) }

    // Service offerings & budget selection
    val availableOfferings = remember(worker.categoryId) {
        ServiceOfferingsData.getOfferingsForCategory(worker.categoryId)
    }
    var selectedBudgetFilter by remember { mutableStateOf("All") }
    val selectedItemQuantities = remember { mutableStateMapOf<String, Int>() }

    val dateOptions = remember {
        val list = mutableListOf<Pair<String, String>>()
        val sdfDay = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
        val cal = Calendar.getInstance()
        list.add("Today" to sdfDay.format(cal.time))
        cal.add(Calendar.DAY_OF_YEAR, 1)
        list.add("Tomorrow" to sdfDay.format(cal.time))
        for (i in 2..6) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
            list.add(sdfDay.format(cal.time) to sdfDay.format(cal.time))
        }
        list
    }

    val timeSlots = listOf("09:00 AM", "10:00 AM", "12:00 PM", "02:00 PM", "04:00 PM", "06:00 PM")

    // Dynamic cost calculation based on chosen services vs hourly
    val itemizedSubtotal = selectedItemQuantities.entries.sumOf { (itemId, qty) ->
        val item = availableOfferings.find { it.id == itemId }
        (item?.baseCost ?: 0.0) * qty
    }

    val hasSelectedItems = itemizedSubtotal > 0
    val rawServiceCharge = if (hasSelectedItems) itemizedSubtotal else (worker.hourlyRate * durationHours)
    val emergencySurcharge = if (isEmergency) rawServiceCharge * 0.20 else 0.0
    val subtotalWithEmergency = rawServiceCharge + emergencySurcharge
    val platformFee = subtotalWithEmergency * 0.05
    val totalCalculatedCost = subtotalWithEmergency + platformFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Strings.get("booking_details", language), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (hasSelectedItems) "Total (${selectedItemQuantities.values.sum()} services)" else Strings.get("total_amount", language),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = CostCalculator.formatCurrency(totalCalculatedCost),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = {
                            val itemsSummary = if (hasSelectedItems) {
                                selectedItemQuantities.filter { it.value > 0 }.map { (id, qty) ->
                                    val item = availableOfferings.find { it.id == id }
                                    "${item?.name ?: id} (x$qty)"
                                }.joinToString(", ")
                            } else ""

                            val fullNotes = if (itemsSummary.isNotBlank()) {
                                "Services: $itemsSummary. ${notes.trim()}".trim()
                            } else {
                                notes.trim()
                            }

                            val newBooking = BookingEntity(
                                id = "bk_" + UUID.randomUUID().toString().take(8),
                                customerId = currentUser.id,
                                customerName = currentUser.name,
                                customerPhone = currentUser.phoneNumber,
                                workerId = worker.id,
                                workerName = worker.name,
                                categoryId = worker.categoryId,
                                categoryName = worker.categoryName,
                                status = BookingStatus.PENDING,
                                scheduledAt = System.currentTimeMillis() + (selectedDateIndex * 86400000L),
                                durationHours = durationHours,
                                address = address.ifBlank { "Banjara Hills, Hyderabad" },
                                notes = fullNotes,
                                estimatedCost = totalCalculatedCost,
                                isEmergency = isEmergency
                            )
                            onBookingConfirmed(newBooking)
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("confirm_booking_btn")
                    ) {
                        Text(
                            text = Strings.get("confirm_booking", language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
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
            // Worker Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = worker.name.firstOrNull()?.toString() ?: "W",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = worker.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(text = worker.categoryName, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        text = "₹${worker.hourlyRate.toInt()}/hr",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp
                    )
                }
            }

            // Select Specific Services & Budget Tier (Dynamic Costing)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Select Services & Budget",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Price adjusts based on exact services picked",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (hasSelectedItems) {
                            TextButton(onClick = { selectedItemQuantities.clear() }) {
                                Text("Reset", fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Budget filter chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val filters = listOf("All", "Budget", "Standard", "Premium")
                        items(filters) { f ->
                            val isSel = selectedBudgetFilter == f
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedBudgetFilter = f },
                                label = {
                                    Text(
                                        when (f) {
                                            "Budget" -> "🏷️ Budget"
                                            "Standard" -> "⭐ Standard"
                                            "Premium" -> "👑 Premium"
                                            else -> "All Services"
                                        },
                                        fontSize = 11.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PrimaryContainerBlue,
                                    selectedLabelColor = PrimarySapphire
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val filteredOfferings = availableOfferings.filter {
                        when (selectedBudgetFilter) {
                            "Budget" -> it.budgetTier == BudgetTier.BUDGET
                            "Standard" -> it.budgetTier == BudgetTier.STANDARD
                            "Premium" -> it.budgetTier == BudgetTier.PREMIUM
                            else -> true
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        filteredOfferings.forEach { serviceItem ->
                            val qty = selectedItemQuantities[serviceItem.id] ?: 0
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (qty > 0) PrimaryContainerLight else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = if (qty > 0) androidx.compose.foundation.BorderStroke(1.dp, PrimarySapphire) else null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(serviceItem.iconEmoji, fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = serviceItem.name,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 13.sp
                                            )
                                        }
                                        if (serviceItem.description.isNotBlank()) {
                                            Text(
                                                text = serviceItem.description,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Text(
                                            text = "₹${serviceItem.baseCost.toInt()} ${serviceItem.unitLabel}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    // Quantity selector
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        if (qty > 0) {
                                            IconButton(
                                                onClick = {
                                                    if (qty > 1) {
                                                        selectedItemQuantities[serviceItem.id] = qty - 1
                                                    } else {
                                                        selectedItemQuantities.remove(serviceItem.id)
                                                    }
                                                },
                                                modifier = Modifier.size(30.dp)
                                            ) {
                                                Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Decrease", tint = MaterialTheme.colorScheme.primary)
                                            }
                                            Text(
                                                text = "$qty",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                modifier = Modifier.padding(horizontal = 4.dp)
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                selectedItemQuantities[serviceItem.id] = qty + 1
                                            },
                                            modifier = Modifier.size(30.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (qty > 0) Icons.Default.AddCircle else Icons.Default.AddCircleOutline,
                                                contentDescription = "Add",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Date Selector
            Column {
                Text(
                    text = Strings.get("select_date", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(dateOptions.indices.toList()) { index ->
                        val (label, sublabel) = dateOptions[index]
                        val isSelected = selectedDateIndex == index
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedDateIndex = index }
                                .testTag("date_option_$index"),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = label,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = sublabel,
                                    fontSize = 11.sp,
                                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Time Slot Selector
            Column {
                Text(
                    text = Strings.get("select_time", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(timeSlots) { slot ->
                        val isSelected = selectedTimeSlot == slot
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTimeSlot = slot },
                            label = { Text(slot, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }
                }
            }

            // Duration Slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Strings.get("duration", language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${String.format("%.1f", durationHours)} ${Strings.get("hours", language)}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp
                    )
                }
                Slider(
                    value = durationHours.toFloat(),
                    onValueChange = { durationHours = (it * 2).toInt() / 2.0 },
                    valueRange = 1.0f..8.0f,
                    steps = 13,
                    modifier = Modifier.testTag("duration_slider")
                )
            }

            // Emergency Dispatch Toggle
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = if (isEmergency) AmberContainer else MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⚡", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Strings.get("emergency_surcharge", language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Priority dispatch within 15-30 minutes",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isEmergency,
                        onCheckedChange = { isEmergency = it }
                    )
                }
            }

            // Service Address
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(Strings.get("address", language)) },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Job Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text(Strings.get("notes", language)) },
                placeholder = { Text("e.g. Bring extra fuse wire or ceiling fan clamp") },
                shape = RoundedCornerShape(14.dp),
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            // Price Breakdown Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = Strings.get("price_breakdown", language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (hasSelectedItems) {
                            Text(text = "Selected Services (${selectedItemQuantities.values.sum()} items)", fontSize = 13.sp)
                            Text(text = CostCalculator.formatCurrency(rawServiceCharge), fontSize = 13.sp)
                        } else {
                            Text(text = "Service (${String.format("%.1f", durationHours)} hrs × ₹${worker.hourlyRate.toInt()})", fontSize = 13.sp)
                            Text(text = CostCalculator.formatCurrency(rawServiceCharge), fontSize = 13.sp)
                        }
                    }
                    if (isEmergency) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Emergency 15-min Surcharge (20%)", fontSize = 13.sp, color = AccentAmberDark)
                            Text(text = "+${CostCalculator.formatCurrency(emergencySurcharge)}", fontSize = 13.sp, color = AccentAmberDark)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = Strings.get("platform_fee", language), fontSize = 13.sp)
                        Text(text = CostCalculator.formatCurrency(platformFee), fontSize = 13.sp)
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Estimated", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(
                            text = CostCalculator.formatCurrency(totalCalculatedCost),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingConfirmationScreen(
    booking: BookingEntity,
    onStatusAdvance: (BookingStatus) -> Unit,
    onCancelBooking: (String) -> Unit,
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onLeaveReviewClick: () -> Unit
) {
    val context = LocalContext.current
    val language = LocalAppLanguage.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Strings.get("booking_status", language), fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Status Stepper Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Booking #${booking.id.takeLast(6).uppercase()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        StatusBadge(status = booking.status)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 4 Step Visual Progression
                    val steps = listOf(
                        BookingStatus.PENDING to "Requested",
                        BookingStatus.ACCEPTED to "Pro Accepted",
                        BookingStatus.IN_PROGRESS to "On The Way",
                        BookingStatus.COMPLETED to "Completed"
                    )

                    val currentStepIndex = when (booking.status) {
                        BookingStatus.PENDING -> 0
                        BookingStatus.ACCEPTED -> 1
                        BookingStatus.IN_PROGRESS -> 2
                        BookingStatus.COMPLETED -> 3
                        BookingStatus.CANCELLED, BookingStatus.REJECTED -> -1
                    }

                    steps.forEachIndexed { index, (_, title) ->
                        val isDone = currentStepIndex >= index
                        val isCurrent = currentStepIndex == index

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isDone -> PrimaryIndigo
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                } else {
                                    Text("${index + 1}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = title,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isDone) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        if (index < steps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 15.dp)
                                    .height(24.dp)
                                    .width(2.dp)
                                    .background(if (currentStepIndex > index) PrimaryIndigo else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                            )
                        }
                    }
                }
            }

            // Quick Simulation Bar for Demonstration
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryContainer.copy(alpha = 0.7f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🧪 Interactive Simulator (Test Lifecycle):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (booking.status == BookingStatus.PENDING) {
                            Button(
                                onClick = { onStatusAdvance(BookingStatus.ACCEPTED) },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(vertical = 6.dp)
                            ) {
                                Text("Pro Accepts", fontSize = 12.sp)
                            }
                        }
                        if (booking.status == BookingStatus.ACCEPTED) {
                            Button(
                                onClick = { onStatusAdvance(BookingStatus.IN_PROGRESS) },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(vertical = 6.dp)
                            ) {
                                Text("Start Job", fontSize = 12.sp)
                            }
                        }
                        if (booking.status == BookingStatus.IN_PROGRESS) {
                            Button(
                                onClick = { onStatusAdvance(BookingStatus.COMPLETED) },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald)
                            ) {
                                Text("Finish Job ✅", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Pro Contact Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(PrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = booking.workerName.firstOrNull()?.toString() ?: "W",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(booking.workerName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(booking.categoryName, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:+919876543210")
                            }
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Dialing pro...", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = onChatClick) {
                            Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Chat", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            // Booking Details Summary
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Job Details", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = PrimaryIndigoLight, modifier = Modifier.size(18.dp))
                        Text(booking.address, fontSize = 13.sp)
                    }
                    if (booking.notes.isNotBlank()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = PrimaryIndigoLight, modifier = Modifier.size(18.dp))
                            Text(booking.notes, fontSize = 13.sp)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.AttachMoney, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(18.dp))
                        Text("Total Estimated: ₹${booking.estimatedCost.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            // Actions Based on Status
            if (booking.status == BookingStatus.COMPLETED) {
                Button(
                    onClick = onLeaveReviewClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmberDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("leave_review_btn")
                ) {
                    Icon(Icons.Default.Star, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(Strings.get("leave_review", language), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            if (booking.status == BookingStatus.PENDING) {
                OutlinedButton(
                    onClick = { onCancelBooking(booking.id) },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Cancel Request", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    workerId: String,
    workerName: String,
    currentUser: UserEntity,
    onBackClick: () -> Unit,
    onSubmitReview: (ReviewEntity) -> Unit
) {
    var rating by remember { mutableDoubleStateOf(5.0) }
    var comment by remember { mutableStateOf("") }
    val availablePraiseTags = listOf("On Time", "Clean Work", "Fair Pricing", "Expert Work", "Polite & Courteous", "Brought Spares")
    val selectedTags = remember { mutableStateListOf<String>("On Time", "Clean Work") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rate & Review", fontWeight = FontWeight.Bold) },
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "How was your experience with $workerName?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            StarRatingBar(
                rating = rating,
                onRatingChanged = { rating = it }
            )

            Text(
                text = when {
                    rating >= 5.0 -> "Outstanding! 🌟"
                    rating >= 4.0 -> "Very Good! 👍"
                    rating >= 3.0 -> "Average 🙂"
                    else -> "Needs Improvement"
                },
                fontWeight = FontWeight.Bold,
                color = AccentAmberDark,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "What did you like most?",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            // Praise tags
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                availablePraiseTags.chunked(2).forEach { rowTags ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowTags.forEach { tag ->
                            val isSelected = selectedTags.contains(tag)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                                },
                                label = { Text(tag) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { if (it.length <= 500) comment = it },
                label = { Text("Write your feedback (Optional)") },
                placeholder = { Text("Share details about the quality of service, punctuality, and pricing...") },
                shape = RoundedCornerShape(14.dp),
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${comment.length} / 500 characters",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End)
            )

            Button(
                onClick = {
                    val review = ReviewEntity(
                        id = "rev_" + UUID.randomUUID().toString().take(8),
                        workerId = workerId,
                        customerId = currentUser.id,
                        customerName = currentUser.name,
                        bookingId = "bk_completed",
                        rating = rating,
                        comment = comment.ifBlank { "Great and timely service by the professional." },
                        praiseTags = selectedTags.toList()
                    )
                    onSubmitReview(review)
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_review_btn")
            ) {
                Text("Submit Review", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
