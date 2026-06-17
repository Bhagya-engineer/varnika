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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.RoomGeniusViewModel
import com.example.ui.components.VarnikaLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: RoomGeniusViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    var designStyle by remember { mutableStateOf("Scandinavian") }
    var budgetRange by remember { mutableStateOf("₹10,000") }
    var favoriteColors by remember { mutableStateOf("Sage, Muted Grey") }
    var profession by remember { mutableStateOf("Software Developer") }
    var interests by remember { mutableStateOf("Focus, Reading, Gaming") }

    // Sync state variables once profile loaded
    LaunchedEffect(profile) {
        profile?.let {
            designStyle = it.designStyle
            budgetRange = it.budgetRange
            favoriteColors = it.favoriteColors
            profession = it.profession
            interests = it.interests
        }
    }

    val stylesList = listOf("Minimalist", "Scandinavian", "Luxury", "Traditional", "Industrial", "Gaming Neon")
    val budgetsList = listOf("₹5,000", "₹10,000", "₹25,000", "₹50,000+")

    val scrollState = rememberScrollState()
    var editMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VarnikaLogo(size = 32.dp, animate = false)
                        Text("Varnika Spec Profile", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                },
                actions = {
                    TextButton(onClick = onNavigateToAdmin) {
                        Text("Admin Dashboard")
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
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            
            // Header card
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "Avatar",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            currentUser?.name ?: "Designer Partner",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black)
                        )
                        Text(
                            currentUser?.email ?: "bhagyalakshmi2584@gmail.com",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Edit Profile Block
            Text(
                "My Curations Specifications",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (!editMode) {
                // Info Mode details list
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Theme Style:", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(designStyle, style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Max Budget Limit:", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(budgetRange, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Favorite Colors Selection:", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(favoriteColors, style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Primary Flow Focus:", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(profession, style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Active Interests:", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(interests, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                Button(
                    onClick = { editMode = true },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Modify Specifications")
                }

            } else {
                // Form edit elements
                Column(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = profession,
                        onValueChange = { profession = it },
                        label = { Text("Profession / Core Profile") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = favoriteColors,
                        onValueChange = { favoriteColors = it },
                        label = { Text("Favorite Harmony Colors (Comma separated)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = interests,
                        onValueChange = { interests = it },
                        label = { Text("Active Interests") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Theme Style Swatches
                    Text("Design Theme Style", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        stylesList.forEach { s ->
                            val isSel = designStyle == s
                            FilterChip(
                                selected = isSel,
                                onClick = { designStyle = s },
                                label = { Text(s) },
                                modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
                            )
                        }
                    }

                    // Budget list Chips
                    Text("Maximum Budget Range Limit", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        budgetsList.forEach { b ->
                            val isSel = budgetRange == b
                            FilterChip(
                                selected = isSel,
                                onClick = { budgetRange = b },
                                label = { Text(b) }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { editMode = false },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Text("Discard")
                        }
                        Button(
                            onClick = {
                                viewModel.updateProfilePrefs(
                                    style = designStyle,
                                    budget = budgetRange,
                                    colors = favoriteColors,
                                    profession = profession,
                                    interests = interests
                                )
                                editMode = false
                            },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Text("Save Specs")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Logout row button
            OutlinedButton(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Exit App Session")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out Session Partner")
            }
        }
    }
}
