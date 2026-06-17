package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.Screen
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.RoomGeniusViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: RoomGeniusViewModel = viewModel()
                VarnikaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun VarnikaApp(viewModel: RoomGeniusViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentUser by viewModel.currentUser.collectAsState()
    val isUserLoggedIn = currentUser != null

    // Determine if we should display the Material 3 Bar
    val barScreens = listOf(
        Screen.Dashboard.route,
        Screen.UploadRoom.route,
        Screen.VirtualPreview.route,
        Screen.Marketplace.route,
        Screen.AssistantChat.route
    )
    val shouldShowBottomBar = currentRoute in barScreens

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (shouldShowBottomBar) {
                // Floating bottom navigation wrapper matching HTML layout
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(start = 20.dp, end = 20.dp, bottom = 16.dp, top = 4.dp),
                    shape = RoundedCornerShape(40.dp),
                    color = WarmBeige,
                    shadowElevation = 8.dp,
                    border = BorderStroke(1.dp, BorderStone)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // 1. Home
                        val isHome = currentRoute == Screen.Dashboard.route
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = if (isHome) DeepForestGreen else TaupeStone,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        // 2. Shop
                        val isShop = currentRoute == Screen.Marketplace.route
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.Marketplace.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = "Shop",
                                tint = if (isShop) DeepForestGreen else TaupeStone,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        // 3. Central AI Camera Scan Trigger (Prominent FAB)
                        val isScan = currentRoute == Screen.UploadRoom.route
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(DeepForestGreen, shape = RoundedCornerShape(28.dp))
                                .clickable {
                                    navController.navigate(Screen.UploadRoom.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Scan Space",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        // 4. Sandbox
                        val isSandbox = currentRoute == Screen.VirtualPreview.route
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.VirtualPreview.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ViewInAr,
                                contentDescription = "Sandbox",
                                tint = if (isSandbox) DeepForestGreen else TaupeStone,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        // 5. Chat AI
                        val isChat = currentRoute == Screen.AssistantChat.route
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.AssistantChat.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = "Chat AI",
                                tint = if (isChat) DeepForestGreen else TaupeStone,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Landing.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Landing.route) {
                LandingScreen(
                    onNavigateToAuth = { navController.navigate(Screen.Auth.route) },
                    onNavigateToDashboard = { navController.navigate(Screen.Dashboard.route) },
                    isUserLoggedIn = isUserLoggedIn
                )
            }

            composable(Screen.Auth.route) {
                AuthScreen(
                    viewModel = viewModel,
                    onNavigateToDashboard = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Landing.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = viewModel,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            composable(Screen.UploadRoom.route) {
                UploadAnalysisScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.VirtualPreview.route) {
                VirtualPreviewScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCart = { navController.navigate(Screen.Cart.route) }
                )
            }

            composable(Screen.Marketplace.route) {
                MarketplaceScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCart = { navController.navigate(Screen.Cart.route) }
                )
            }

            composable(Screen.AssistantChat.route) {
                AssistantChatScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Cart.route) {
                CartScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToMarketplace = { navController.navigate(Screen.Marketplace.route) }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate(Screen.Landing.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    },
                    onNavigateToAdmin = { navController.navigate(Screen.Admin.route) }
                )
            }

            composable(Screen.Admin.route) {
                AdminScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
