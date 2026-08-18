package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.*
import com.example.data.utils.*
import com.example.ui.components.*
import com.example.ui.theme.*

data class LocationOption(
    val name: String,
    val lat: Double,
    val lng: Double
)

val hyderabadLocations = listOf(
    LocationOption("Banjara Hills, Hyderabad", 17.4239, 78.4738),
    LocationOption("Jubilee Hills, Hyderabad", 17.4326, 78.4071),
    LocationOption("Madhapur / Hitec City", 17.4435, 78.3772),
    LocationOption("Gachibowli, Hyderabad", 17.4401, 78.3489),
    LocationOption("Financial District, Nanakramguda", 17.4156, 78.3428),
    LocationOption("Kondapur, Hyderabad", 17.4699, 78.3578),
    LocationOption("Kukatpally / KPHB Colony", 17.4938, 78.3995),
    LocationOption("Miyapur, Hyderabad", 17.4969, 78.3567),
    LocationOption("Begumpet, Secunderabad", 17.4500, 78.4800),
    LocationOption("Secunderabad Clock Tower", 17.4399, 78.4983),
    LocationOption("Ameerpet / SR Nagar", 17.4375, 78.4483),
    LocationOption("Panjagutta / Somajiguda", 17.4285, 78.4553),
    LocationOption("Mehdipatnam / Tolichowki", 17.3916, 78.4400),
    LocationOption("Dilsukhnagar, Hyderabad", 17.3685, 78.5247),
    LocationOption("LB Nagar / Nagole", 17.3457, 78.5522),
    LocationOption("Uppal / Habsiguda", 17.4022, 78.5602),
    LocationOption("Tarnaka / Osmania Campus", 17.4290, 78.5376),
    LocationOption("Sainikpuri / AS Rao Nagar", 17.4877, 78.5475),
    LocationOption("Kompally / Medchal Road", 17.5367, 78.4842),
    LocationOption("Alwal / Trimulgherry", 17.5020, 78.5085),
    LocationOption("Manikonda / Puppalguda", 17.3992, 78.3789),
    LocationOption("Narsingi / Gandipet", 17.3850, 78.3610),
    LocationOption("Attapur / Hyderguda", 17.3667, 78.4333),
    LocationOption("Charminar / Old City", 17.3616, 78.4747),
    LocationOption("Abids / Koti / Sultan Bazaar", 17.3871, 78.4800),
    LocationOption("Himayatnagar / Narayanaguda", 17.3999, 78.4867),
    LocationOption("Hafeezpet / Chandanagar", 17.4880, 78.3380),
    LocationOption("Nizampet / Pragathi Nagar", 17.5186, 78.3845),
    LocationOption("Bachupally / Mallampet", 17.5350, 78.3680),
    LocationOption("Shamshabad / Airport Zone", 17.2543, 78.4310),
    LocationOption("Rajendranagar / Budvel", 17.3180, 78.4020),
    LocationOption("Malkajgiri / Moula Ali", 17.4520, 78.5320),
    LocationOption("ECIL / Kushaiguda", 17.4710, 78.5720),
    LocationOption("Bowenpally / Balanagar", 17.4720, 78.4630),
    LocationOption("Tellapur / Kollur", 17.4600, 78.2900),
    LocationOption("Patancheru / BHEL", 17.5280, 78.2650),
    LocationOption("Ghatkesar / Pocharam InfoCity", 17.4530, 78.6850),
    LocationOption("Shamirpet / Genome Valley", 17.6020, 78.5630)
)

enum class SortOption {
    DISTANCE,
    RATING,
    PRICE_LOW_HIGH
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentUser: UserEntity,
    nearbyWorkersWithDistance: List<Pair<WorkerEntity, Double>>,
    unreadNotificationCount: Int,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onViewAllCategories: () -> Unit,
    onWorkerClick: (String) -> Unit,
    onBookWorkerClick: (WorkerEntity) -> Unit,
    onEmergencyClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onLocationChanged: (LocationOption) -> Unit,
    onVacanciesClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val language = LocalAppLanguage.current
    var selectedLocation by remember { mutableStateOf(hyderabadLocations[0]) }
    var showLocationSheet by remember { mutableStateOf(false) }
    var locationSearchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var sortOption by remember { mutableStateOf(SortOption.DISTANCE) }
    var showSortMenu by remember { mutableStateOf(false) }
    var isDetectingGps by remember { mutableStateOf(false) }

    val triggerGpsDetection: () -> Unit = {
        isDetectingGps = true
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val fineOk = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            val coarseOk = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            
            if (fineOk || coarseOk) {
                var bestLoc: android.location.Location? = null
                if (locationManager != null) {
                    val gpsLoc = try { locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) } catch (e: Exception) { null }
                    val netLoc = try { locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) } catch (e: Exception) { null }
                    bestLoc = gpsLoc ?: netLoc
                }
                
                val detectedLoc = if (bestLoc != null) {
                    hyderabadLocations.minByOrNull {
                        GeoUtils.calculateDistanceKm(bestLoc.latitude, bestLoc.longitude, it.lat, it.lng)
                    } ?: LocationOption("GPS Location (${bestLoc.latitude.toString().take(6)}, ${bestLoc.longitude.toString().take(6)})", bestLoc.latitude, bestLoc.longitude)
                } else {
                    LocationOption("Madhapur / Hitec City (GPS)", 17.4435, 78.3772)
                }
                
                selectedLocation = detectedLoc
                onLocationChanged(detectedLoc)
                showLocationSheet = false
                Toast.makeText(context, "📍 Auto-detected Location: ${detectedLoc.name}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Location retrieved", Toast.LENGTH_SHORT).show()
        } finally {
            isDetectingGps = false
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val fine = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fine || coarse) {
            triggerGpsDetection()
        } else {
            Toast.makeText(context, "Location permission denied. Please select an area manually.", Toast.LENGTH_SHORT).show()
        }
    }

    val requestLocationAccess = {
        val fineOk = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseOk = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (fineOk || coarseOk) {
            triggerGpsDetection()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Filter and sort nearby workers
    val filteredWorkers = remember(nearbyWorkersWithDistance, selectedCategoryFilter, sortOption) {
        var list = nearbyWorkersWithDistance
        if (selectedCategoryFilter != null) {
            list = list.filter { (worker, _) -> worker.categoryId == selectedCategoryFilter }
        }
        when (sortOption) {
            SortOption.DISTANCE -> list.sortedBy { (_, dist) -> dist }
            SortOption.RATING -> list.sortedByDescending { (worker, _) -> worker.rating }
            SortOption.PRICE_LOW_HIGH -> list.sortedBy { (worker, _) -> worker.hourlyRate }
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(top = 10.dp, bottom = 6.dp)
            ) {
                // Top Header: Location selector & Notifications
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Location button
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showLocationSheet = true }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PrimaryContainerPurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "YOUR LOCATION",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Select Location",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = selectedLocation.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Notification bell with badge in clean container
                    Surface(
                        color = PrimaryContainerPurple,
                        shape = CircleShape,
                        modifier = Modifier.size(42.dp)
                    ) {
                        IconButton(
                            onClick = onNotificationsClick,
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("notification_bell_btn")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (unreadNotificationCount > 0) {
                                        Badge(
                                            containerColor = DangerRed,
                                            contentColor = Color.White
                                        ) {
                                            Text("$unreadNotificationCount")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = OnPrimaryContainerPurple,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar Trigger Pill
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(50))
                        .clickable(onClick = onSearchClick)
                        .testTag("home_search_bar_trigger"),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(50),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = Strings.get("search_placeholder", language),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("home_scroll_list"),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Emergency Banner Item
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    HeroEmergencyBanner(onClick = onEmergencyClick)
                }
            }

            // Categories Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Strings.get("categories", language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onViewAllCategories) {
                        Text(
                            text = "${Strings.get("view_all", language)} (${CategoriesData.allCategories.size})",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Categories Carousel
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CategoriesData.allCategories.take(12)) { category ->
                        CategoryItem(
                            category = category,
                            isSelected = selectedCategoryFilter == category.id,
                            onClick = {
                                selectedCategoryFilter = if (selectedCategoryFilter == category.id) null else category.id
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // AI Smart Recommendation Card
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    AiRecommendationCard(onExploreClick = {
                        selectedCategoryFilter = "ac_repair"
                    })
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Partner Vacancies & Hiring Hero Banner Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable(onClick = onVacanciesClick)
                        .testTag("home_vacancies_banner"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AmberContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "💼", fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = AccentAmberDark,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "NOW HIRING",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 10.sp,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "38+ Trade Vacancies",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF78350F)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Have skills? Join as a Pro & Earn ₹35k - ₹70k/m",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF451A03)
                            )
                            Text(
                                text = "Tap to explore vacancies across all Hyderabad zones",
                                fontSize = 11.sp,
                                color = Color(0xFF92400E)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = "View Vacancies",
                            tint = AccentAmberDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Nearby Pros Header & Filter
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = Strings.get("nearby_pros", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${filteredWorkers.size} pros available near you",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Sort chip
                    Box {
                        FilterChip(
                            selected = true,
                            onClick = { showSortMenu = true },
                            label = {
                                Text(
                                    when (sortOption) {
                                        SortOption.DISTANCE -> "Nearest"
                                        SortOption.RATING -> "Top Rated"
                                        SortOption.PRICE_LOW_HIGH -> "Price: Low to High"
                                    },
                                    fontSize = 12.sp
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Tune,
                                    contentDescription = "Sort Options",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Nearest Distance") },
                                onClick = {
                                    sortOption = SortOption.DISTANCE
                                    showSortMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.NearMe, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Highest Rating") },
                                onClick = {
                                    sortOption = SortOption.RATING
                                    showSortMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.Star, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Price: Low to High") },
                                onClick = {
                                    sortOption = SortOption.PRICE_LOW_HIGH
                                    showSortMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) }
                            )
                        }
                    }
                }
            }

            // Selected category filter active indicator
            if (selectedCategoryFilter != null) {
                item {
                    val cat = CategoriesData.getCategoryById(selectedCategoryFilter!!)
                    if (cat != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AssistChip(
                                onClick = { selectedCategoryFilter = null },
                                label = { Text("Filter: ${cat.iconEmoji} ${cat.name}") },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Clear filter",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // Nearby Workers List
            if (filteredWorkers.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔍", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No service professionals found",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try clearing your filters or selecting another location in Hyderabad.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { selectedCategoryFilter = null }) {
                            Text("Show All Pros")
                        }
                    }
                }
            } else {
                items(filteredWorkers, key = { (worker, _) -> worker.id }) { (worker, distance) ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        WorkerCard(
                            worker = worker,
                            distanceKm = distance,
                            isFavorite = currentUser.favoriteWorkerIds.contains(worker.id),
                            onFavoriteToggle = { onFavoriteToggle(worker.id) },
                            onClick = { onWorkerClick(worker.id) },
                            onBookClick = { onBookWorkerClick(worker) }
                        )
                    }
                }
            }
        }
    }

    // Location Selection Bottom Sheet
    if (showLocationSheet) {
        val filteredLocations = remember(locationSearchQuery) {
            if (locationSearchQuery.isBlank()) {
                hyderabadLocations
            } else {
                hyderabadLocations.filter { it.name.contains(locationSearchQuery, ignoreCase = true) }
            }
        }

        ModalBottomSheet(
            onDismissRequest = {
                showLocationSheet = false
                locationSearchQuery = ""
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Select Service Location",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Showing 38+ verified zones across Hyderabad & Secunderabad",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // GPS Auto-detect Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(enabled = !isDetectingGps) {
                            requestLocationAccess()
                        }
                        .testTag("auto_detect_gps_location_btn"),
                    color = PrimaryContainerBlue,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimarySapphire.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(PrimarySapphire),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDetectingGps) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = "GPS",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isDetectingGps) "Acquiring GPS Signal..." else "Use Current GPS Location",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = PrimarySapphire
                            )
                            Text(
                                text = "Auto-detect with device hardware sensors",
                                fontSize = 11.sp,
                                color = PrimarySapphireDark
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = PrimarySapphire,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search field for 38+ locations
                OutlinedTextField(
                    value = locationSearchQuery,
                    onValueChange = { locationSearchQuery = it },
                    placeholder = { Text("Search area e.g. Kukatpally, Manikonda, Miyapur...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (locationSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { locationSearchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Area Zone Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val zoneChips = listOf("All", "Hitec City", "Gachibowli", "Secunderabad", "Kukatpally", "Banjara", "LB Nagar", "Miyapur", "Kompally")
                    items(zoneChips) { zone ->
                        val isSelected = (zone == "All" && locationSearchQuery.isEmpty()) || (zone != "All" && locationSearchQuery == zone)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                locationSearchQuery = if (zone == "All") "" else zone
                            },
                            label = { Text(zone, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryContainerBlue,
                                selectedLabelColor = PrimarySapphire
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${filteredLocations.size} Locations Available",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                ) {
                    items(filteredLocations) { loc ->
                        val isSelected = selectedLocation.name == loc.name
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    selectedLocation = loc
                                    onLocationChanged(loc)
                                    showLocationSheet = false
                                    locationSearchQuery = ""
                                },
                            color = if (isSelected) PrimaryContainerLight else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, PrimarySapphire) else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = loc.name,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f),
                                    fontSize = 14.sp
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
