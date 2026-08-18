package com.example.ui.screens

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobApplicationEntity
import com.example.data.model.UserEntity
import com.example.data.model.VacancyEntity
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

val sampleVacancies = listOf(
    VacancyEntity(
        id = "vac_000",
        title = "Master Boutique Tailor & Maggam Artisan",
        categoryId = "stitching_tailoring",
        categoryName = "Stitching & Boutique Tailor",
        iconEmoji = "🧵",
        location = "Jubilee Hills, Banjara & Madhapur Zone",
        jobType = "Full-Time / Freelance",
        estimatedEarnings = "₹40,000 - ₹75,000 / mo",
        openingsCount = 15,
        urgency = "Immediate Hiring",
        experienceRequired = "2+ years",
        skillsRequired = listOf("Blouse Stitching", "Maggam Work", "Salwar Suits", "Lehenga Fitting", "Doorstep Measurements"),
        perks = listOf("Daily Payouts", "High Value Orders", "Flexible Work Radius", "Doorstep Pickup Support"),
        description = "Huge customer demand for home measurements and customized blouse/suit stitching across Hyderabad. Earn premium rates per stitched piece with zero platform fees for 30 days.",
        isHotVacancy = true
    ),
    VacancyEntity(
        id = "vac_001",
        title = "Master Electrician & Inverter Specialist",
        categoryId = "electrician",
        categoryName = "Electrician",
        iconEmoji = "⚡",
        location = "Banjara Hills & Jubilee Hills Zone",
        jobType = "Full-Time / Flexible",
        estimatedEarnings = "₹35,000 - ₹55,000 / mo",
        openingsCount = 12,
        urgency = "Immediate Hiring",
        experienceRequired = "2+ years",
        skillsRequired = listOf("Inverter Setup", "MCB Repair", "Appliance Wiring", "3-Phase Fitting"),
        perks = listOf("Daily Payouts", "₹3L Health Insurance", "Free Safety Kit", "High Demand Area"),
        description = "Join our top-earning electrical partner fleet serving luxury residences and tech corridors. Guaranteed booking volume and instant daily withdrawals.",
        isHotVacancy = true
    ),
    VacancyEntity(
        id = "vac_002",
        title = "Plumbing Technician & Leakage Expert",
        categoryId = "plumber",
        categoryName = "Plumber",
        iconEmoji = "🔧",
        location = "Madhapur & Hitec City Hub",
        jobType = "Full-Time Partner",
        estimatedEarnings = "₹30,000 - ₹48,000 / mo",
        openingsCount = 8,
        urgency = "High Demand",
        experienceRequired = "1+ years",
        skillsRequired = listOf("Pipe Leak Fix", "Bathroom Sanitary", "Motor Installation", "Drain Unclogging"),
        perks = listOf("Daily Direct Payouts", "₹5,000 Joining Bonus", "Fuel Allowance"),
        description = "High customer demand in high-rise gated communities. Flexible booking slots with zero platform commission for the first 30 days.",
        isHotVacancy = true
    ),
    VacancyEntity(
        id = "vac_003",
        title = "AC Service & Gas Charging Tech",
        categoryId = "ac_repair",
        categoryName = "AC Repair",
        iconEmoji = "❄️",
        location = "Gachibowli & Financial District",
        jobType = "Seasonal / Full-Time",
        estimatedEarnings = "₹45,000 - ₹70,000 / mo",
        openingsCount = 15,
        urgency = "Immediate Hiring",
        experienceRequired = "2+ years",
        skillsRequired = listOf("Split/Window AC Service", "Gas Top-Up", "Compressor Diagnostics", "PCB Repair"),
        perks = listOf("Peak Season Surge Bonus", "Free Uniforms", "Free Tool Assistance"),
        description = "Top seasonal demand across corporate apartments and villas. Earn up to ₹70k with overtime and multi-unit installation incentives.",
        isHotVacancy = true
    ),
    VacancyEntity(
        id = "vac_004",
        title = "Home Deep Cleaning Team Lead",
        categoryId = "cleaning",
        categoryName = "Cleaning",
        iconEmoji = "✨",
        location = "Kukatpally & Miyapur Cluster",
        jobType = "Full-Time / Shifts",
        estimatedEarnings = "₹28,000 - ₹42,000 / mo",
        openingsCount = 10,
        urgency = "Active Openings",
        experienceRequired = "1+ years",
        skillsRequired = listOf("Sofa Shampooing", "Kitchen Deep Clean", "Floor Polishing", "Sanitization"),
        perks = listOf("Eco Machine Training", "Team Lead Incentives", "Weekly Tips Pool"),
        description = "Lead premium residential cleaning orders. We supply heavy-duty industrial vacuum machines and specialized eco-friendly chemicals."
    ),
    VacancyEntity(
        id = "vac_005",
        title = "Custom Furniture & Modular Carpenter",
        categoryId = "carpenter",
        categoryName = "Carpenter",
        iconEmoji = "🪚",
        location = "Secunderabad & Begumpet Zone",
        jobType = "Project / Contract Basis",
        estimatedEarnings = "₹32,000 - ₹50,000 / mo",
        openingsCount = 6,
        urgency = "High Demand",
        experienceRequired = "3+ years",
        skillsRequired = listOf("Modular Wardrobe", "Lock Replacement", "Door Alignment", "Plywood Crafting"),
        perks = listOf("Material Sourcing Discounts", "Flexible Radius", "Direct Customer Tips"),
        description = "Work on modular kitchen fittings, door hinges, office table setups, and custom wood restoration with high ticket values."
    ),
    VacancyEntity(
        id = "vac_006",
        title = "Interior & Exterior Wall Painter",
        categoryId = "painter",
        categoryName = "Painter",
        iconEmoji = "🎨",
        location = "Dilsukhnagar & LB Nagar Hub",
        jobType = "Contract / Full-Time",
        estimatedEarnings = "₹30,000 - ₹46,000 / mo",
        openingsCount = 9,
        urgency = "Active Openings",
        experienceRequired = "2+ years",
        skillsRequired = listOf("Wall Putty", "Texture Design", "Waterproofing", "Spray Painting"),
        perks = listOf("Paint Company Certificate", "Scaffolding Safety Gear", "Direct Weekly Payout"),
        description = "Great opportunity for experienced painters and polishers with ongoing residential painting contracts across South-East Hyderabad."
    ),
    VacancyEntity(
        id = "vac_007",
        title = "CCTV, Biometric & Smart Home Tech",
        categoryId = "cctv_security",
        categoryName = "CCTV & Security",
        iconEmoji = "📹",
        location = "Kondapur & Hafeezpet Zone",
        jobType = "Full-Time Partner",
        estimatedEarnings = "₹36,000 - ₹58,000 / mo",
        openingsCount = 7,
        urgency = "Immediate Hiring",
        experienceRequired = "1+ years",
        skillsRequired = listOf("IP Camera Setup", "DVR/NVR Config", "Video Doorbell", "Smart Lock"),
        perks = listOf("Tech Certification Support", "Monthly Device Allowance", "High Conversion Rates"),
        description = "Install smart cameras, video doorbells, and wifi security systems in tech townships with high commission on accessory upgrades."
    ),
    VacancyEntity(
        id = "vac_008",
        title = "Two-Wheeler & Car Mobile Mechanic",
        categoryId = "mechanic",
        categoryName = "Mechanic",
        iconEmoji = "🔩",
        location = "Mehdipatnam & Tolichowki Corridor",
        jobType = "Flexible Shifts",
        estimatedEarnings = "₹32,000 - ₹52,000 / mo",
        openingsCount = 11,
        urgency = "High Demand",
        experienceRequired = "2+ years",
        skillsRequired = listOf("Battery Jumpstart", "Brake Pad Change", "Oil Flushing", "Puncture Assistance"),
        perks = listOf("Emergency Surge Bonus 1.5x", "Roadside Kit Provided", "Fuel Subsidies"),
        description = "Provide doorstep minor servicing and breakdown assistance for bikes and cars with immediate surge payouts for emergency bookings."
    ),
    VacancyEntity(
        id = "vac_009",
        title = "Eco Pest Control Operator",
        categoryId = "pest_control",
        categoryName = "Pest Control",
        iconEmoji = "🐜",
        location = "Kompally & Alwal Zone",
        jobType = "Full-Time Partner",
        estimatedEarnings = "₹26,000 - ₹40,000 / mo",
        openingsCount = 5,
        urgency = "Active Openings",
        experienceRequired = "1+ years",
        skillsRequired = listOf("Termite Treatment", "Cockroach Gel", "Bedbug Heat Treatment", "Fogging"),
        perks = listOf("Govt Certified Chemicals", "Full PPE Kit", "Quarterly Retention Bonus"),
        description = "Perform odor-free pest control and termite treatments for residential and commercial spaces with complete chemical safety supplies."
    ),
    VacancyEntity(
        id = "vac_010",
        title = "Landscape & Balcony Garden Specialist",
        categoryId = "gardener",
        categoryName = "Gardener",
        iconEmoji = "🌿",
        location = "Manikonda & Narsingi Zone",
        jobType = "Flexible Part-Time / Full-Time",
        estimatedEarnings = "₹25,000 - ₹38,000 / mo",
        openingsCount = 6,
        urgency = "Active Openings",
        experienceRequired = "1+ years",
        skillsRequired = listOf("Lawn Mowing", "Pruning & Trimming", "Soil Fertilization", "Balcony Planters"),
        perks = listOf("Recurring Monthly Client Retainers", "Organic Seed Supply Support"),
        description = "Maintain premium terrace gardens, villa lawns, and balcony plantations with steady monthly subscription clientele."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacanciesScreen(
    currentUser: UserEntity,
    vacancies: List<VacancyEntity> = sampleVacancies,
    submittedApplications: List<JobApplicationEntity> = emptyList(),
    onSubmitApplication: (JobApplicationEntity) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var selectedLocationFilter by remember { mutableStateOf("All Hyderabad") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Open Vacancies, 1: My Applications

    var applyingVacancy by remember { mutableStateOf<VacancyEntity?>(null) }
    var applicationSubmittedDialog by remember { mutableStateOf<JobApplicationEntity?>(null) }

    val categories = remember(vacancies) {
        listOf("All Trades") + vacancies.map { it.categoryName }.distinct()
    }

    val locationZones = listOf(
        "All Hyderabad",
        "Banjara Hills / Jubilee Hills",
        "Madhapur / Hitec City",
        "Gachibowli / Financial Dist",
        "Kukatpally / Miyapur",
        "Secunderabad / Begumpet",
        "Dilsukhnagar / LB Nagar",
        "Kondapur / Hafeezpet",
        "Mehdipatnam / Tolichowki",
        "Kompally / Medchal",
        "Manikonda / Narsingi"
    )

    val filteredVacancies = remember(vacancies, selectedCategoryFilter, selectedLocationFilter, searchQuery) {
        vacancies.filter { vac ->
            val matchesCategory = selectedCategoryFilter == null || selectedCategoryFilter == "All Trades" || vac.categoryName.equals(selectedCategoryFilter, ignoreCase = true)
            val matchesLocation = selectedLocationFilter == "All Hyderabad" || vac.location.contains(selectedLocationFilter.take(8), ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    vac.title.contains(searchQuery, ignoreCase = true) ||
                    vac.categoryName.contains(searchQuery, ignoreCase = true) ||
                    vac.location.contains(searchQuery, ignoreCase = true) ||
                    vac.skillsRequired.any { it.contains(searchQuery, ignoreCase = true) }
            matchesCategory && matchesLocation && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Partner Vacancies & Jobs",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${vacancies.sumOf { it.openingsCount }}+ Active Openings in Hyderabad",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Help info */ }) {
                        Icon(Icons.Outlined.Info, contentDescription = "Info", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Tab Header (Vacancies vs My Applications)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Work, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open Vacancies (${vacancies.size})", fontWeight = FontWeight.Bold)
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("My Applications (${submittedApplications.size})", fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }

            if (selectedTab == 0) {
                // Open Vacancies Tab
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("vacancies_list"),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    // Hero Callout Banner
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                PrimarySapphire,
                                                Color(0xFF1E3A8A),
                                                Color(0xFF0F172A)
                                            )
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Surface(
                                            color = AccentAmber,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "🔥 HIRING PARTNERS",
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 11.sp,
                                                color = Color.Black,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = "Zero Joining Fee",
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = "Earn ₹30,000 to ₹70,000 / month",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Join 1,200+ verified professionals across Hyderabad with flexible hours, ₹3 Lakh insurance, and instant daily payouts.",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        HeroPerkPill("⚡ Daily Payouts")
                                        HeroPerkPill("🛡️ ₹3L Insurance")
                                        HeroPerkPill("📍 Work in Your Area")
                                    }
                                }
                            }
                        }
                    }

                    // Search & Zone Filter Section
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            // Search Bar
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Search by skill, title, e.g. Inverter, AC, Hitec City") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("vacancy_search_field")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Location Zone Selector Chips
                            Text(
                                text = "Filter by Location Zone in Hyderabad:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                items(locationZones) { zone ->
                                    val isSelected = selectedLocationFilter == zone
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedLocationFilter = zone },
                                        label = { Text(zone, fontSize = 12.sp) },
                                        leadingIcon = if (isSelected) {
                                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                        } else null,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = PrimaryContainerBlue,
                                            selectedLabelColor = PrimarySapphire
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Trade Skill Chips
                            Text(
                                text = "Filter by Trade / Skill:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                items(categories) { cat ->
                                    val isSelected = (selectedCategoryFilter == null && cat == "All Trades") || selectedCategoryFilter == cat
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            selectedCategoryFilter = if (cat == "All Trades") null else cat
                                        },
                                        label = { Text(cat, fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = PrimaryContainerBlue,
                                            selectedLabelColor = PrimarySapphire
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Showing ${filteredVacancies.size} Available Partner Roles",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Vacancy Cards
                    if (filteredVacancies.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Default.WorkOff,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "No vacancies found matching your filters",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Try clearing filters to see all available trade roles.",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(onClick = {
                                        selectedCategoryFilter = null
                                        selectedLocationFilter = "All Hyderabad"
                                        searchQuery = ""
                                    }) {
                                        Text("Reset Filters")
                                    }
                                }
                            }
                        }
                    } else {
                        items(filteredVacancies, key = { it.id }) { vacancy ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                                VacancyCard(
                                    vacancy = vacancy,
                                    onApplyClick = { applyingVacancy = vacancy }
                                )
                            }
                        }
                    }
                }
            } else {
                // My Applications Tab
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (submittedApplications.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.AssignmentInd,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(52.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "No Job Applications Yet",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Browse open vacancies and apply in 60 seconds with zero paperwork to get hired!",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { selectedTab = 0 },
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Explore Openings")
                                    }
                                }
                            }
                        }
                    } else {
                        item {
                            Text(
                                text = "Your Submitted Applications (${submittedApplications.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        items(submittedApplications, key = { it.id }) { app ->
                            ApplicationCard(application = app)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }

    // Apply Modal Bottom Sheet
    if (applyingVacancy != null) {
        val vacancy = applyingVacancy!!
        ApplyVacancyBottomSheet(
            vacancy = vacancy,
            currentUser = currentUser,
            onDismiss = { applyingVacancy = null },
            onSubmit = { application ->
                onSubmitApplication(application)
                applyingVacancy = null
                applicationSubmittedDialog = application
            }
        )
    }

    // Application Success Dialog
    if (applicationSubmittedDialog != null) {
        val app = applicationSubmittedDialog!!
        AlertDialog(
            onDismissRequest = { applicationSubmittedDialog = null },
            icon = {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(EmeraldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = SuccessEmerald,
                        modifier = Modifier.size(36.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Application Submitted! 🎉",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Your application for ${app.vacancyTitle} has been received.",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Ref ID:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(app.id, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Applicant:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(app.applicantName, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Preferred Area:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(app.preferredLocation, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "📞 Our Hyderabad Partner Support will call you at ${app.applicantPhone} within 2 business hours for verification and free onboarding.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        applicationSubmittedDialog = null
                        selectedTab = 1 // Switch to My Applications
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View My Applications")
                }
            }
        )
    }
}

@Composable
fun HeroPerkPill(text: String) {
    Surface(
        color = Color.White.copy(alpha = 0.18f),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun VacancyCard(
    vacancy: VacancyEntity,
    onApplyClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("vacancy_card_${vacancy.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Emoji, Title, Urgency Badge
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryContainerBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = vacancy.iconEmoji, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            color = if (vacancy.isHotVacancy) DangerContainer else AmberContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (vacancy.isHotVacancy) "🔥 ${vacancy.urgency}" else "⚡ ${vacancy.urgency}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (vacancy.isHotVacancy) DangerRed else AccentAmberDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            color = EmeraldContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${vacancy.openingsCount} Openings",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessEmerald,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = vacancy.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "📍 ${vacancy.location} • ${vacancy.jobType}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Earnings Highlight Banner
            Surface(
                color = PrimaryContainerLight,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Estimated Monthly Earnings",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = vacancy.estimatedEarnings,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = PrimarySapphire
                        )
                    }

                    Text(
                        text = "Exp: ${vacancy.experienceRequired}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = vacancy.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Skills required tags
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(vacancy.skillsRequired) { skill ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = skill,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onApplyClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Apply Now (Free)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun ApplicationCard(application: JobApplicationEntity) {
    val dateStr = remember(application.appliedAt) {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        sdf.format(Date(application.appliedAt))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = application.id,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = PrimarySapphire
                )

                Surface(
                    color = EmeraldContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = application.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessEmerald,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = application.vacancyTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = "Trade: ${application.categoryName} • Experience: ${application.experienceYears} Years",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "📍 Preferred Area: ${application.preferredLocation}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Applied on $dateStr",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Verification In Progress ⏳",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AccentAmberDark
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyVacancyBottomSheet(
    vacancy: VacancyEntity,
    currentUser: UserEntity,
    onDismiss: () -> Unit,
    onSubmit: (JobApplicationEntity) -> Unit
) {
    var applicantName by remember { mutableStateOf(currentUser.name.ifBlank { "Ravi Kumar" }) }
    var applicantPhone by remember { mutableStateOf(currentUser.phoneNumber.ifBlank { "9876543210" }.replace("+91", "").trim()) }
    var applicantEmail by remember { mutableStateOf(currentUser.email.ifBlank { "partner@example.com" }) }
    var experienceYears by remember { mutableIntStateOf(2) }
    var preferredLocation by remember { mutableStateOf(vacancy.location) }
    var hasTools by remember { mutableStateOf(true) }
    var hasVehicle by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(PrimaryContainerBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = vacancy.iconEmoji, fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Apply: ${vacancy.title}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${vacancy.estimatedEarnings} • ${vacancy.location}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(16.dp))

            Text("Full Name", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = applicantName,
                onValueChange = { applicantName = it },
                placeholder = { Text("Your full name") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Mobile Phone Number", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = applicantPhone,
                onValueChange = { applicantPhone = it.filter { c -> c.isDigit() } },
                leadingIcon = { Text(" +91 ", fontWeight = FontWeight.Bold) },
                placeholder = { Text("10-digit number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Trade Experience in Years", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(1, 2, 3, 5, 8).forEach { yrs ->
                    val isSelected = experienceYears == yrs
                    FilterChip(
                        selected = isSelected,
                        onClick = { experienceYears = yrs },
                        label = { Text("$yrs+ Years") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryContainerBlue,
                            selectedLabelColor = PrimarySapphire
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Preferred Working Area in Hyderabad", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = preferredLocation,
                onValueChange = { preferredLocation = it },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Checkboxes for Tool kit and 2-wheeler
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { hasTools = !hasTools }
            ) {
                Checkbox(checked = hasTools, onCheckedChange = { hasTools = it })
                Spacer(modifier = Modifier.width(6.dp))
                Text("I have basic tools / equipment for this trade", fontSize = 13.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { hasVehicle = !hasVehicle }
            ) {
                Checkbox(checked = hasVehicle, onCheckedChange = { hasVehicle = it })
                Spacer(modifier = Modifier.width(6.dp))
                Text("I have a two-wheeler / bike for local travel", fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val app = JobApplicationEntity(
                        id = "LP-HYD-${(10000..99999).random()}",
                        vacancyId = vacancy.id,
                        vacancyTitle = vacancy.title,
                        categoryName = vacancy.categoryName,
                        applicantName = applicantName.ifBlank { "Applicant" },
                        applicantPhone = "+91 $applicantPhone",
                        applicantEmail = applicantEmail,
                        experienceYears = experienceYears,
                        preferredLocation = preferredLocation,
                        hasTools = hasTools,
                        hasVehicle = hasVehicle,
                        appliedAt = System.currentTimeMillis(),
                        status = "Under Review"
                    )
                    onSubmit(app)
                },
                enabled = applicantName.isNotBlank() && applicantPhone.length >= 10,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Submit Application (Free Joining)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
