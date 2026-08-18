package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    currentUser: UserEntity,
    allWorkers: List<WorkerEntity>,
    preselectedCategory: String? = null,
    onBackClick: () -> Unit,
    onWorkerClick: (String) -> Unit,
    onBookWorkerClick: (WorkerEntity) -> Unit,
    onFavoriteToggle: (String) -> Unit
) {
    val language = LocalAppLanguage.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf(preselectedCategory) }
    var selectedBudgetFilter by remember { mutableStateOf("All") }

    val filteredWorkers = remember(allWorkers, searchQuery, selectedCategoryId, selectedBudgetFilter, currentUser) {
        allWorkers
            .filter { worker ->
                val categoryMatch = (selectedCategoryId == null || worker.categoryId == selectedCategoryId)
                val budgetMatch = when (selectedBudgetFilter) {
                    "Economy" -> worker.hourlyRate <= 200.0
                    "Standard" -> worker.hourlyRate in 201.0..350.0
                    "Premium" -> worker.hourlyRate > 350.0
                    else -> true
                }
                val searchMatch = (searchQuery.isBlank() ||
                        worker.name.contains(searchQuery, ignoreCase = true) ||
                        worker.categoryName.contains(searchQuery, ignoreCase = true) ||
                        worker.skills.any { it.contains(searchQuery, ignoreCase = true) } ||
                        worker.address.contains(searchQuery, ignoreCase = true))
                categoryMatch && budgetMatch && searchMatch
            }
            .map { worker ->
                val distance = GeoUtils.calculateDistanceKm(
                    currentUser.latitude, currentUser.longitude,
                    worker.latitude, worker.longitude
                )
                worker to distance
            }
            .sortedBy { (_, dist) -> dist }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(Strings.get("search_placeholder", language), fontSize = 14.sp) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = PrimaryIndigoLight)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = PrimaryIndigoLight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("search_text_input")
                    )
                },
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
        ) {
            // Horizontal Categories Filter Chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategoryId == null,
                        onClick = { selectedCategoryId = null },
                        label = { Text("All (${CategoriesData.allCategories.size})") }
                    )
                }
                items(CategoriesData.allCategories) { category ->
                    val isSelected = selectedCategoryId == category.id
                    val displayName = when (language) {
                        AppLanguage.HINDI -> category.nameHindi
                        AppLanguage.TELUGU -> category.nameTelugu
                        AppLanguage.ENGLISH -> category.name
                    }
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedCategoryId = if (isSelected) null else category.id
                        },
                        label = { Text("${category.iconEmoji} $displayName") }
                    )
                }
            }

            // Budget Tier Filter Chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val budgetOptions = listOf(
                    "All" to "All Budgets",
                    "Economy" to "🏷️ Economy (≤ ₹200)",
                    "Standard" to "⭐ Standard (₹200 - ₹350)",
                    "Premium" to "👑 Premium (₹350+)"
                )
                items(budgetOptions) { (key, label) ->
                    val isSelected = selectedBudgetFilter == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedBudgetFilter = key },
                        label = { Text(label, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryContainerBlue,
                            selectedLabelColor = PrimarySapphire
                        )
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            if (filteredWorkers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔍", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No professionals matched your search",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try searching with broader terms like 'plumber', 'wiring', 'cleaning', or clear filters.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            searchQuery = ""
                            selectedCategoryId = null
                        }) {
                            Text("Reset Search Filters")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Found ${filteredWorkers.size} available experts",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    items(filteredWorkers, key = { (worker, _) -> worker.id }) { (worker, distance) ->
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    onCategoryClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val language = LocalAppLanguage.current
    var categorySearchQuery by remember { mutableStateOf("") }
    var selectedGroup by remember { mutableStateOf("All") }

    val categoryGroups = listOf(
        "All" to "All",
        "Care" to "Caretaker & Medical",
        "Home" to "Maid & Housekeeping",
        "Repairs" to "Repairs & Tech",
        "Wellness" to "Wellness & Lifestyle",
        "Transport" to "Vehicles & Moving"
    )

    val filteredCategories = remember(categorySearchQuery, selectedGroup, language) {
        CategoriesData.allCategories.filter { cat ->
            val matchesGroup = when (selectedGroup) {
                "Care" -> cat.id in listOf("caretaker", "babysitter", "home_nurse", "special_needs_care", "pet_care")
                "Home" -> cat.id in listOf("house_maid", "cook", "home_cleaner", "sofa_cleaning", "laundry", "ironing", "gardener", "pest_control")
                "Repairs" -> cat.id in listOf("electrician", "plumber", "carpenter", "painter", "mason", "locksmith", "ac_repair", "fridge_repair", "washing_machine", "ro_service", "geyser_repair", "gas_stove_repair")
                "Wellness" -> cat.id in listOf("beautician", "mehendi", "yoga_trainer", "home_tutor", "event_helper", "tailor")
                "Transport" -> cat.id in listOf("driver", "mechanic", "car_wash", "packers_movers", "milk_delivery", "water_delivery", "security_guard")
                else -> true
            }

            val displayName = when (language) {
                AppLanguage.HINDI -> cat.nameHindi
                AppLanguage.TELUGU -> cat.nameTelugu
                AppLanguage.ENGLISH -> cat.name
            }

            val matchesQuery = categorySearchQuery.isBlank() ||
                    cat.name.contains(categorySearchQuery, ignoreCase = true) ||
                    cat.nameHindi.contains(categorySearchQuery, ignoreCase = true) ||
                    cat.nameTelugu.contains(categorySearchQuery, ignoreCase = true) ||
                    displayName.contains(categorySearchQuery, ignoreCase = true) ||
                    cat.id.contains(categorySearchQuery, ignoreCase = true)

            matchesGroup && matchesQuery
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = Strings.get("categories", language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp
                        )
                        Text(
                            text = "${filteredCategories.size} of ${CategoriesData.allCategories.size} Services",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Category Search Box
            OutlinedTextField(
                value = categorySearchQuery,
                onValueChange = { categorySearchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("category_search_input"),
                placeholder = {
                    Text("Search caretaker, maid, nurse, plumber...", fontSize = 13.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (categorySearchQuery.isNotEmpty()) {
                        IconButton(onClick = { categorySearchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear search")
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            // Group Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categoryGroups) { (key, title) ->
                    val isSelected = selectedGroup == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedGroup = key },
                        label = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryContainerLight,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (filteredCategories.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔍", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No category matched '$categorySearchQuery'",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try searching with other terms or switch filter groups.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = {
                            categorySearchQuery = ""
                            selectedGroup = "All"
                        }) {
                            Text("Reset Category Search")
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .testTag("categories_grid"),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredCategories, key = { it.id }) { category ->
                        val displayName = when (language) {
                            AppLanguage.HINDI -> category.nameHindi
                            AppLanguage.TELUGU -> category.nameTelugu
                            AppLanguage.ENGLISH -> category.name
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onCategoryClick(category.id) }
                                .testTag("all_category_${category.id}"),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(Color(category.colorHex).copy(alpha = 0.16f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = category.iconEmoji,
                                        fontSize = 26.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = displayName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    maxLines = 2,
                                    minLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 15.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Surface(
                                    color = PrimaryContainerLight,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${category.avgRate}${category.unit}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
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
