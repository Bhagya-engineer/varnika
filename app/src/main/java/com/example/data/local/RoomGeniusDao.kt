package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomGeniusDao {
    
    // User Session
    @Query("SELECT * FROM users WHERE isSessionActive = 1 LIMIT 1")
    fun getActiveSession(): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("UPDATE users SET isSessionActive = 0")
    suspend fun clearActiveSessions()

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    // User Profile
    @Query("SELECT * FROM user_profiles WHERE userId = :userId LIMIT 1")
    fun getProfile(userId: String): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    // Products (E-Commerce)
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProductById(id: Int)

    // Cart
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: CartItem)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun removeFromCart(productId: Int)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateCartQuantity(productId: Int, quantity: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // Wishlist
    @Query("SELECT * FROM wishlist_items")
    fun getWishlistItems(): Flow<List<WishlistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistItem)

    @Query("DELETE FROM wishlist_items WHERE productId = :productId")
    suspend fun removeFromWishlist(productId: Int)

    @Query("SELECT EXISTS (SELECT 1 FROM wishlist_items WHERE productId = :productId)")
    fun isInWishlist(productId: Int): Flow<Boolean>

    // Saved Designs
    @Query("SELECT * FROM saved_designs ORDER BY id DESC")
    fun getSavedDesigns(): Flow<List<SavedDesign>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDesign(design: SavedDesign)

    @Query("DELETE FROM saved_designs WHERE id = :id")
    suspend fun deleteDesign(id: Int)

    // Chat History
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatMessages()

    // Analysis Reports
    @Query("SELECT * FROM analysis_reports ORDER BY timestamp DESC")
    fun getAnalysisReports(): Flow<List<AnalysisReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalysisReport(report: AnalysisReport)
}
