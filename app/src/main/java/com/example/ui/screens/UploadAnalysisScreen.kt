package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.viewmodel.RoomGeniusViewModel
import com.example.ui.components.VarnikaLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadAnalysisScreen(
    viewModel: RoomGeniusViewModel,
    onNavigateBack: () -> Unit
) {
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val activeReport by viewModel.activeReport.collectAsState()
    val reports by viewModel.analysisReports.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    var roomType by remember { mutableStateOf("Student Study Room") }
    val roomTypes = listOf("Student Study Room", "Bedroom", "Gaming Room", "Living Room", "Office Workspace")

    // Premade room templates for instant dynamic simulation
    val staticPhotosList = listOf(
        R.drawable.img_roomgenius_hero_1781672318245
    )

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Room Analyzer", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
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
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            if (activeReport == null && !isAnalyzing) {
                // Interactive Setup Form
                Text(
                    "Let's Scan Your Space",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )
                Text(
                    "Select your room type and choose a visual setup angle below to generate a smart design report.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                )

                // Image Selection Panel
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { /* Simulate capturing or uploading */ },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_roomgenius_hero_1781672318245),
                        contentDescription = "Selected Room",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.35f))
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = "Upload icon", tint = Color.White, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Room Image Picked", style = MaterialTheme.typography.bodyLarge.copy(color = Color.White, fontWeight = FontWeight.Bold))
                        Text("Tap to capture standard angle camera", style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f)))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Room Type Selector
                Text(
                    "Identify Room Category",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                FlowRow(modifier = Modifier.fillMaxWidth()) {
                    roomTypes.forEach { type ->
                        val isSelected = roomType == type
                        FilterChip(
                            selected = isSelected,
                            onClick = { roomType = type },
                            label = { Text(type) },
                            modifier = Modifier.padding(end = 6.dp, bottom = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Action Call trigger
                Button(
                    onClick = {
                        viewModel.runRoomAnalysis(roomType)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "AI Scan")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate AI Report", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }

            } else if (isAnalyzing) {
                // Analyzing status view
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    VarnikaLogo(
                        size = 140.dp,
                        animate = true
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Varnika AI is analyzing your space...",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        "Calculating color contrast, harmony index, and ergonomics placement markers...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                // Active Report Results View!
                val r = activeReport!!

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header Card
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Stars, contentDescription = "Result", tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "${r.roomType} Report Created",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "Overall Space score: ${r.overallScore}/100",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "Dimensions: ${r.estimatedDimensions}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    // Score trackers
                    Text(
                        "AI Room Quality Scores",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            ScoreSlider(label = "Productivity Flow", score = r.productivityScore)
                            ScoreSlider(label = "Comfort Index", score = r.comfortScore)
                            ScoreSlider(label = "Lighting index", score = r.lightingScore)
                        }
                        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                            ScoreSlider(label = "Wall Harmony", score = r.harmonyScore)
                            ScoreSlider(label = "Contrast Index", score = r.contrastScore)
                            ScoreSlider(label = "Aesthetic Score", score = r.aestheticScore)
                        }
                    }

                    DividerSection(modifier = Modifier.padding(vertical = 16.dp))

                    // Color Detect Harmony Panel
                    Text(
                        "Color Harmony System",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ColorSwatchCard(
                            title = "Detected Wall Surface",
                            colorLabel = r.wallColor,
                            modifier = Modifier.weight(1f)
                        )
                        ColorSwatchCard(
                            title = "Floor Base Texture",
                            colorLabel = r.floorColor,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    DividerSection()

                    // Placement Recommendations details
                    Text(
                        "Placement & Styling Guide",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = r.recommendationSummary,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Reset action
                    OutlinedButton(
                        onClick = { viewModel.runRoomAnalysis(roomType) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Re-analyze")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Re-Analyze Settings")
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreSlider(label: String, score: Int) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            Text("$score", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary))
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { score.toFloat() / 100f },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = if (score > 80) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun ColorSwatchCard(title: String, colorLabel: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Color Circle preview
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (title.contains("Wall")) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    colorLabel,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    fontSize = 13.sp,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun DividerSection(modifier: Modifier = Modifier) {
    HorizontalDivider(modifier = modifier, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
}
