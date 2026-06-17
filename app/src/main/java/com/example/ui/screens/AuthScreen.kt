package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.RoomGeniusViewModel
import com.example.ui.components.VarnikaLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: RoomGeniusViewModel,
    onNavigateToDashboard: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()

    // Redirect if logged in
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onNavigateToDashboard()
        }
    }

    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf("Scandinavian") }
    var selectedBudget by remember { mutableStateOf("₹10,000") }
    var profession by remember { mutableStateOf("Software Developer") }
    var favoriteColors by remember { mutableStateOf("Warm Sage, White Walnut, Charcoal") }

    val styles = listOf("Minimalist", "Scandinavian", "Luxury", "Traditional", "Industrial", "Gaming Neon")
    val budgets = listOf("₹5,000", "₹10,000", "₹25,000", "₹50,000+")
    val professions = listOf("Software Developer", "Student", "Gamer", "Content Creator", "Reading Enthusiast")

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Workspace Profile Onboarding",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VarnikaLogo(
                size = 100.dp,
                animate = true,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Heading
            Text(
                "Personalize Your Experience",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                textAlign = TextAlign.Center
            )
            Text(
                "Varnika configures optimal furniture sizing, matches colors to wall paints, and aligns budgets based on your lifestyle.",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            // Form inputs
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "User Icon") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = "Email Icon") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(bottom = 24.dp))

            // Preferred Design Style
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Palette, contentDescription = "Palette Icon", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Favorite Design Style", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            }
            // Carousel or grid of style buttons
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                maxItemsInEachRow = 3
            ) {
                styles.forEach { style ->
                    val isSelected = selectedStyle == style
                    SuggestionChip(
                        onClick = { selectedStyle = style },
                        label = { Text(style) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            labelColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(end = 6.dp, bottom = 6.dp)
                    )
                }
            }

            // Budget target
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("💲 Target Room Budget", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                budgets.forEach { budget ->
                    val isSelected = selectedBudget == budget
                    OutlinedButton(
                        onClick = { selectedBudget = budget },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    ) {
                        Text(budget, fontSize = 12.sp, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }

            // Professional Profile (Personality selection)
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Work, contentDescription = "Work Icon", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Occupation / Core Flow", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            }
            // Professions selection list
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                maxItemsInEachRow = 2
            ) {
                professions.forEach { prof ->
                    val isSelected = profession == prof
                    SuggestionChip(
                        onClick = { profession = prof },
                        label = { Text(prof) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                            labelColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(end = 6.dp, bottom = 6.dp)
                    )
                }
            }

            // Custom Colors Input
            Text(
                "Accent Colors (Comma separated)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = favoriteColors,
                onValueChange = { favoriteColors = it },
                label = { Text("Colors") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
            )

            // Create Profile trigger button
            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        viewModel.register(
                            name = name,
                            email = email,
                            style = selectedStyle,
                            budget = selectedBudget,
                            profession = profession,
                            colors = favoriteColors
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Launch Varnika",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    maxItemsInEachRow: Int = 3,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        maxItemsInEachRow = maxItemsInEachRow,
        content = { content() }
    )
}
