package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String, // email or UUID
    val name: String,
    val email: String,
    val isSessionActive: Boolean = false
)

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val userId: String,
    val budgetRange: String, // e.g. "₹10,000"
    val favoriteColors: String, // Commalist e.g. "Teal, White"
    val designStyle: String, // e.g. "Scandinavian"
    val profession: String, // e.g. "Software Developer"
    val interests: String // Commalist e.g. "Gamer, Reader"
)

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // "Furniture", "Decor", "Lighting", "Paintings", "Storage"
    val price: Double,
    val rating: Float,
    val reviewsCount: Int,
    val imageResName: String, // Name of generic icon or dynamic placeholder
    val description: String,
    val recommendedColor: String,
    val suggestedSize: String,
    val placementTip: String,
    val whyItMatches: String = "Selected to complement your style and budget."
)

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val productName: String,
    val productPrice: Double,
    val imageResName: String,
    val quantity: Int,
    val chosenColor: String
)

@Entity(tableName = "wishlist_items")
data class WishlistItem(
    @PrimaryKey val productId: Int,
    val productName: String,
    val price: Double,
    val imageResName: String
)

@Entity(tableName = "saved_designs")
data class SavedDesign(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val roomType: String, // "Bedroom", "Gaming Room", "Living Room", etc.
    val wallColor: String,
    val floorColor: String,
    val overallScore: Int,
    val budgetSpent: Double,
    val designItemsJson: String, // Saved placement markers or selected items details
    val dateString: String
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "analysis_reports")
data class AnalysisReport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roomType: String,
    val wallColor: String,
    val floorColor: String,
    val harmonyScore: Int,
    val contrastScore: Int,
    val aestheticScore: Int,
    val productivityScore: Int,
    val comfortScore: Int,
    val lightingScore: Int,
    val overallScore: Int,
    val estimatedDimensions: String,
    val recommendationSummary: String,
    val timestamp: Long = System.currentTimeMillis()
)
