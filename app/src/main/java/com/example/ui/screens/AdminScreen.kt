package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.viewmodel.RoomGeniusViewModel
import com.example.ui.components.VarnikaLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: RoomGeniusViewModel,
    onNavigateBack: () -> Unit
) {
    val products by viewModel.allProducts.collectAsState()

    // Form states for creating a new product
    var newProductName by remember { mutableStateOf("") }
    var newProductPrice by remember { mutableStateOf("") }
    var newProductCategory by remember { mutableStateOf("Furniture") }
    var newProductDescription by remember { mutableStateOf("") }
    var newProductPlacementTip by remember { mutableStateOf("") }
    var newProductSizeSuggest by remember { mutableStateOf("") }

    val categoriesList = listOf("Furniture", "Decor", "Lighting", "Paintings", "Storage")

    val scrollState = rememberScrollState()

    var activeTab by remember { mutableStateOf("Analytics") } // "Analytics", "Inventory"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VarnikaLogo(size = 32.dp, animate = false)
                        Text("Varnika Admin Hub", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            
            // Tab Selector Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Analytics", "Inventory").forEach { t ->
                    val isSel = activeTab == t
                    Button(
                        onClick = { activeTab = t },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(t, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (activeTab == "Analytics") {
                // Analytics dashboard view
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Marketplace Platform Insights",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    )

                    // Cards breakdown grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCounterCard(title = "Total Sales Volumes", value = "₹1,84,300", icon = Icons.Default.TrendingUp, colorLabel = "Green", modifier = Modifier.weight(1f))
                        StatCounterCard(title = "Room Makeovers Ordered", value = "24 Runs", icon = Icons.Default.DoneAll, colorLabel = "Blue", modifier = Modifier.weight(1f))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCounterCard(title = "Active Live Users", value = "128 Accounts", icon = Icons.Default.Groups, colorLabel = "Orange", modifier = Modifier.weight(1f))
                        StatCounterCard(title = "Global Design Score Avg", value = "87 / 100", icon = Icons.Default.Stars, colorLabel = "Yellow", modifier = Modifier.weight(1f))
                    }

                    // Simulated Bar chart of metrics using Jetpack Compose canvas or styled cards
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                "Popular Categories Demand",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            DemandChartBar(label = "Study Desks / Chairs", percentage = 0.85f, amountText = "85% popularity")
                            DemandChartBar(label = "Modular Shelves", percentage = 0.60f, amountText = "60% popularity")
                            DemandChartBar(label = "Mood RGB Lighting", percentage = 0.94f, amountText = "94% popularity")
                            DemandChartBar(label = "Framed Paintings", percentage = 0.45f, amountText = "45% popularity")
                        }
                    }
                }
            } else {
                // Inventory & Adding Products view
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Form: Add Custom Design Item",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    )

                    OutlinedTextField(
                        value = newProductName,
                        onValueChange = { newProductName = it },
                        label = { Text("Product Name") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newProductPrice,
                            onValueChange = { newProductPrice = it },
                            label = { Text("Price (₹)") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = newProductSizeSuggest,
                            onValueChange = { newProductSizeSuggest = it },
                            label = { Text("Suggested Size") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Category picker
                    Text("Design category filter", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        categoriesList.forEach { cat ->
                            val isSel = newProductCategory == cat
                            FilterChip(
                                selected = isSel,
                                onClick = { newProductCategory = cat },
                                label = { Text(cat) },
                                modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = newProductDescription,
                        onValueChange = { newProductDescription = it },
                        label = { Text("Detailed product description") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newProductPlacementTip,
                        onValueChange = { newProductPlacementTip = it },
                        label = { Text("Aesthetic Placement Tip") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Add button trigger
                    Button(
                        onClick = {
                            val priceVal = newProductPrice.toDoubleOrNull() ?: 999.0
                            if (newProductName.isNotBlank()) {
                                viewModel.insertProduct(
                                    Product(
                                        name = newProductName,
                                        category = newProductCategory,
                                        price = priceVal,
                                        rating = 4.5f,
                                        reviewsCount = 1,
                                        imageResName = when (newProductCategory.lowercase()) {
                                            "furniture" -> "table"
                                            "lighting" -> "hex_lights"
                                            "paintings" -> "wall_painting"
                                            else -> "bookshelf"
                                        },
                                        description = newProductDescription.ifBlank { "Modern bespoke layout enhancer." },
                                        recommendedColor = "Custom Match",
                                        suggestedSize = newProductSizeSuggest.ifBlank { "Standard Sizing" },
                                        placementTip = newProductPlacementTip.ifBlank { "Place at center empty wall zone." }
                                    )
                                )
                                // Reset inputs
                                newProductName = ""
                                newProductPrice = ""
                                newProductDescription = ""
                                newProductPlacementTip = ""
                                newProductSizeSuggest = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Insert Design Product Inside Store")
                    }

                    // Existing inventory items overview table
                    Text(
                        "Delete Items From Marketplace Catalog",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    products.forEach { p ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(p.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                    Text("Category: ${p.category} | price: ₹${p.price.toInt()}", fontSize = 11.sp)
                                }
                                IconButton(onClick = { viewModel.deleteProductById(p.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Item", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCounterCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    colorLabel: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                Icon(
                    icon,
                    contentDescription = null,
                    tint = when (colorLabel) {
                        "Green" -> Color(0xFF2E7D32)
                        "Blue" -> Color(0xFF1565C0)
                        "Orange" -> Color(0xFFE65100)
                        else -> Color(0xFFD84315)
                    },
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black))
        }
    }
}

@Composable
fun DemandChartBar(
    label: String,
    percentage: Float,
    amountText: String
) {
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodySmall)
            Text(amountText, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
