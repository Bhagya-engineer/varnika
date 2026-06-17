package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.viewmodel.RoomGeniusViewModel
import com.example.ui.components.VarnikaLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    viewModel: RoomGeniusViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val products by viewModel.allProducts.collectAsState()
    val wishlist by viewModel.wishlistItems.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Furniture", "Decor", "Lighting", "Paintings", "Storage")

    val filteredProducts = remember(products, selectedCategory) {
        if (selectedCategory == "All") {
            products
        } else {
            products.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }
    }

    var selectedDetailProduct by remember { mutableStateOf<Product?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VarnikaLogo(size = 32.dp, animate = false)
                        Text("Varnika Catalog", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
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
        ) {
            
            // Category Scrollable Line Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Horizontal category chips
                categories.forEach { cat ->
                    val isSel = selectedCategory == cat
                    ElevatedFilterChip(
                        selected = isSel,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) },
                        colors = SelectableChipColors(isSel)
                    )
                }
            }

            // Central item lazy Grid
            if (filteredProducts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.HourglassEmpty, contentDescription = "Empty", size = 48.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No items found in this section.", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredProducts) { p ->
                        val isFav = wishlist.any { it.productId == p.id }

                        ProductCatalogCard(
                            product = p,
                            isFavorite = isFav,
                            onProductClick = { selectedDetailProduct = p },
                            onToggleFavorite = { viewModel.toggleWishlist(p) },
                            onAddToCart = { viewModel.addToCart(p) }
                        )
                    }
                }
            }
        }

        // Detail dialog box
        if (selectedDetailProduct != null) {
            val p = selectedDetailProduct!!
            val isFav = wishlist.any { it.productId == p.id }

            AlertDialog(
                onDismissRequest = { selectedDetailProduct = null },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addToCart(p)
                            selectedDetailProduct = null
                        },
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Icon(Icons.Default.AddShoppingCart, contentDescription = "Add")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add to Cart")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedDetailProduct = null }) {
                        Text("Close")
                    }
                },
                title = { Text(p.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)) },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Category: ${p.category} | Price: ₹${p.price}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                        HorizontalDivider()
                        Text(p.description, style = MaterialTheme.typography.bodyMedium)
                        HorizontalDivider()
                        Text("💡 Design Placement Tip:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                        Text(p.placementTip, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("📐 Sizing Dimensions: ${p.suggestedSize}", style = MaterialTheme.typography.bodySmall)
                        Text("🎨 Suggested Tint: ${p.recommendedColor}", style = MaterialTheme.typography.bodySmall)
                    }
                },
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun ProductCatalogCard(
    product: Product,
    isFavorite: Boolean,
    onProductClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Visual Header Box representing image placeholders
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .background(
                        when (product.category.lowercase()) {
                            "furniture" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            "lighting" -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                            "paintings" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (product.category.lowercase()) {
                        "furniture" -> Icons.Default.Weekend
                        "lighting" -> Icons.Default.Lightbulb
                        "paintings" -> Icons.Default.Brush
                        "decor" -> Icons.Default.Yard
                        else -> Icons.Default.Inventory2
                    },
                    contentDescription = product.name,
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                // Favorite button top corner
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite toggle",
                        tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Description block
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "₹${product.price}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Star, contentDescription = "Reviews", tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                    Text(
                        text = " ${product.rating} (${product.reviewsCount})",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.fillMaxWidth().height(38.dp),
                    shape = RoundedCornerShape(28.dp),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    Text("Buy now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SelectableChipColors(isSelected: Boolean) = FilterChipDefaults.filterChipColors(
    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
)

@Composable
fun IconTextButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, size = 16.dp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 11.sp)
    }
}

@Composable
fun Icon(icon: androidx.compose.ui.graphics.vector.ImageVector, contentDescription: String?, size: androidx.compose.ui.unit.Dp) {
    Icon(icon, contentDescription = contentDescription, modifier = Modifier.size(size))
}
