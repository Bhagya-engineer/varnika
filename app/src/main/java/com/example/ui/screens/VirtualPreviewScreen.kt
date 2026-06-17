package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Product
import com.example.ui.viewmodel.RoomGeniusViewModel
import com.example.ui.components.VarnikaLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualPreviewScreen(
    viewModel: RoomGeniusViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val products by viewModel.allProducts.collectAsState()

    // Interactive customization state variables
    var currentMode by remember { mutableStateOf("Split Mode") } // "Before", "After", "Split Mode"
    val modes = listOf("Before", "After", "Split Mode")

    var selectedItemIndex by remember { mutableStateOf(0) }
    val activeProduct = products.getOrNull(selectedItemIndex)

    var chosenSize by remember { mutableStateOf("Medium") }
    val sizesList = listOf("Compact", "Medium", "Expanded")

    var chosenColor by remember { mutableStateOf("Scandinavian White") }
    val colorsList = listOf("Scandinavian White", "Satin Walnut", "Pastel Sage", "Matte Charcoal")

    // Interactive placement coordinates values (simulated moving)
    var alignmentOffsetX by remember { mutableStateOf(-30f) }
    var alignmentOffsetY by remember { mutableStateOf(40f) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VarnikaLogo(size = 32.dp, animate = false)
                        Text("Varnika Sandbox", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToCart) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "My Cart")
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
                .verticalScroll(scrollState)
                .padding(bottom = 24.dp)
        ) {
            
            // View mode headers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                modes.forEach { m ->
                    val isSelected = currentMode == m
                    OutlinedButton(
                        onClick = { currentMode = m },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f) else Color.Transparent,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        Text(m, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Central Canvas Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(2.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            ) {
                // Background photo representation representing base space
                Image(
                    painter = painterResource(id = R.drawable.img_roomgenius_hero_1781672318245),
                    contentDescription = "Active Room Preview Mode",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Before mode filters overlay
                if (currentMode == "Before") {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Original Empty / Clean Layout",
                            style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Black)
                        )
                    }
                }

                // After furniture placements layer representations
                if (currentMode == "After" || currentMode == "Split Mode") {
                    // Custom Floating Item Marker pins
                    ItemMarkerPin(
                        label = activeProduct?.name ?: "Desk",
                        color = chosenColor,
                        xOffset = alignmentOffsetX.dp,
                        yOffset = alignmentOffsetY.dp,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // Decor items placement tags
                    ItemMarkerPin(
                        label = "Fairy Wall string lights",
                        color = "Warm White",
                        xOffset = (-80).dp,
                        yOffset = (-60).dp,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    ItemMarkerPin(
                        label = "Satin Hanging Plant",
                        color = "Forest Green",
                        xOffset = 100.dp,
                        yOffset = (-40).dp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                if (currentMode == "Split Mode") {
                    // Split Divider line
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(3.dp)
                            .background(Color.White)
                            .align(Alignment.Center)
                    ) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text("SPLIT", fontSize = 9.sp, color = Color.White, modifier = Modifier.padding(2.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Controls panel
            if (activeProduct != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Placed Item Configurator",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                activeProduct.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                            )
                            Text(
                                "Placement tip: ${activeProduct.placementTip}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // Size picker
                            Text("Chosen Size Scale", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                sizesList.forEach { s ->
                                    val isSel = chosenSize == s
                                    SuggestionChip(
                                        onClick = { chosenSize = s },
                                        label = { Text(s) },
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                        )
                                    )
                                }
                            }

                            // Color picker Swatches
                            Text("Dynamic Wood & Fabric Finish", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(top = 8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                colorsList.forEach { c ->
                                    val isSel = chosenColor == c
                                    SuggestionChip(
                                        onClick = { chosenColor = c },
                                        label = { Text(c) },
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = if (isSel) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                                        )
                                    )
                                }
                            }

                            // Placement Position Joy-pad (Simulating Move Item)
                            Text("Nudge Position Coordinates", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(top = 10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { alignmentOffsetX -= 15f }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Left", tint = MaterialTheme.colorScheme.primary)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(onClick = { alignmentOffsetY -= 15f }) {
                                        Icon(Icons.Default.ArrowDropUp, contentDescription = "Up", tint = MaterialTheme.colorScheme.primary)
                                    }
                                    Text("Position X/Y", fontSize = 10.sp, style = MaterialTheme.typography.bodySmall)
                                    IconButton(onClick = { alignmentOffsetY += 15f }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Down", tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                IconButton(onClick = { alignmentOffsetX += 15f }) {
                                    Icon(Icons.Default.ArrowForward, contentDescription = "Right", tint = MaterialTheme.colorScheme.primary)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Marketplace add action in model
                            Button(
                                onClick = {
                                    viewModel.addToCart(activeProduct, chosenColor)
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(28.dp)
                            ) {
                                Icon(Icons.Default.AddShoppingCart, contentDescription = "Add")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Add Custom Setup to Cart (₹${activeProduct.price})", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Choose Recommended Catalog Items to preview
                    Text(
                        "Swap Preview Subject",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        products.forEachIndexed { idx, p ->
                            val isSel = selectedItemIndex == idx
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedItemIndex = idx },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSel) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                                ),
                                border = if (isSel) borderStroke() else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.AddHomeWork,
                                        contentDescription = "Decor placement",
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(p.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                                        Text("${p.category} | Custom suggested size: ${p.suggestedSize}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    if (isSel) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Placed", tint = MaterialTheme.colorScheme.primary)
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

@Composable
fun ItemMarkerPin(
    label: String,
    color: String,
    xOffset: androidx.compose.ui.unit.Dp,
    yOffset: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .offset(x = xOffset, y = yOffset)
            .background(Color.Black.copy(alpha = 0.85f), RoundedCornerShape(20.dp))
            .border(2.dp, Color.White, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(Icons.Default.Place, contentDescription = "Marker", tint = Color.Red, modifier = Modifier.size(16.dp))
            Column {
                Text(label, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Text(color, fontSize = 9.sp, color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun borderStroke(): androidx.compose.foundation.BorderStroke {
    return androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
}
