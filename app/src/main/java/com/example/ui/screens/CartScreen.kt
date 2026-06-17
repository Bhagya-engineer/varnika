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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.ui.viewmodel.RoomGeniusViewModel
import com.example.ui.components.VarnikaLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: RoomGeniusViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToMarketplace: () -> Unit
) {
    val cart by viewModel.cartItems.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    val totalCost = remember(cart) { cart.fold(0.0) { acc, item -> acc + (item.productPrice * item.quantity) } }
    val profileBudgetMax = remember(profile) {
        val raw = profile?.budgetRange?.replace("₹", "")?.replace(",", "")?.replace("+", "")?.trim() ?: "10000"
        raw.toDoubleOrNull() ?: 10000.0
    }

    val remainingBudget = profileBudgetMax - totalCost
    val valueScore = when {
        totalCost == 0.0 -> 0
        totalCost <= profileBudgetMax -> 96
        totalCost <= profileBudgetMax * 1.5 -> 78
        else -> 45
    }

    val scrollState = rememberScrollState()

    var showCompletedOrderDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VarnikaLogo(size = 32.dp, animate = false)
                        Text("Varnika Cart Specs", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
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
            
            if (cart.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        VarnikaLogo(
                            size = 120.dp,
                            animate = false,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text("Your makeover cart is empty", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("Add study desks, LED panels, or art posters to build a layout preview.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onNavigateToMarketplace,
                            shape = RoundedCornerShape(28.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text("Shop Design Catalog")
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Budget Plan details
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CardTravel, contentDescription = "Planner", tint = MaterialTheme.colorScheme.tertiary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Smart Budget Optimization", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("User allocated cap:", style = MaterialTheme.typography.bodySmall)
                                Text("₹${profileBudgetMax.toInt()}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Curated setup cost:", style = MaterialTheme.typography.bodySmall)
                                Text("₹${totalCost.toInt()}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                            }

                            // Progress Slider indicator representing budget bounds
                            val budgetFactor = (totalCost / profileBudgetMax).toFloat().coerceIn(0f, 1f)
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { budgetFactor },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                color = if (remainingBudget >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    if (remainingBudget >= 0) "Remaining Surplus:" else "Budget Surplus (Exceeded):",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    "₹${remainingBudget.toInt()}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = if (remainingBudget >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                    )
                                )
                            }
                        }
                    }

                    // Value Scores card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Savings, contentDescription = "Score", tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Layout Value Index: $valueScore/100", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                                Text(
                                    if (valueScore > 80) "Optimal aesthetic gains on standard budget limits!"
                                    else "Recommendations exceed profile limit bounds. Nudge sizes/quantities.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Text("Selected Setup Items", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black), modifier = Modifier.padding(top = 8.dp))

                    cart.forEach { item ->
                        CartListItemRow(
                            item = item,
                            onIncrement = { viewModel.updateCartQuantity(item.productId, item.quantity + 1) },
                            onDecrement = { viewModel.updateCartQuantity(item.productId, item.quantity - 1) },
                            onDelete = { viewModel.removeFromCart(item.productId) }
                        )
                    }
                }

                // Check out Panel
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Total Makeover Cost", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("₹${totalCost.toInt()}", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary))
                            }
                            Button(
                                onClick = {
                                    viewModel.checkoutCart()
                                    showCompletedOrderDialog = true
                                },
                                shape = RoundedCornerShape(28.dp),
                                modifier = Modifier.height(50.dp)
                            ) {
                                Text("Complete Makeover", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        if (showCompletedOrderDialog) {
            AlertDialog(
                onDismissRequest = { showCompletedOrderDialog = false },
                confirmButton = {
                    Button(onClick = { showCompletedOrderDialog = false }, shape = RoundedCornerShape(28.dp)) {
                        Text("Great!")
                    }
                },
                title = { Text("Makeover Setup Ordered!", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                text = {
                    Text("Your aesthetic setup specifications have been validated. Varnika is dispatching your layout elements to your local registered home hub shipping line.")
                },
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun CartListItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.productName, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Price: ₹${item.productPrice.toInt()} | Specs: ${item.chosenColor}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrement) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrement", modifier = Modifier.size(20.dp))
                }
                Text("x${item.quantity}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                IconButton(onClick = onIncrement) {
                    Icon(Icons.Default.Add, contentDescription = "Increment", modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove item", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
