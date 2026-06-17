package com.example.data.local

import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class RoomGeniusRepository(private val dao: RoomGeniusDao) {

    // User Session
    val activeSession: Flow<User?> = dao.getActiveSession()

    suspend fun registerUser(name: String, email: String): User {
        val user = User(id = email, name = name, email = email, isSessionActive = true)
        dao.clearActiveSessions()
        dao.insertUser(user)
        return user
    }

    suspend fun loginUser(email: String): User? {
        val user = dao.getUserByEmail(email) ?: return null
        dao.clearActiveSessions()
        val loggedInUser = user.copy(isSessionActive = true)
        dao.insertUser(loggedInUser)
        return loggedInUser
    }

    suspend fun logout() {
        dao.clearActiveSessions()
    }

    // Profile preferences
    fun getProfile(userId: String): Flow<UserProfile?> = dao.getProfile(userId)

    suspend fun updateProfile(profile: UserProfile) {
        dao.insertProfile(profile)
    }

    // Products (E-Commerce)
    val allProducts: Flow<List<Product>> = dao.getAllProducts()

    fun getProductsByCategory(category: String): Flow<List<Product>> = dao.getProductsByCategory(category)

    suspend fun insertProduct(product: Product) = dao.insertProduct(product)

    suspend fun deleteProductById(id: Int) = dao.deleteProductById(id)

    // Seed Initial Products if empty
    suspend fun seedDatabaseIfEmpty() {
        val currentProducts = dao.getAllProducts().firstOrNull()
        if (currentProducts.isNullOrEmpty()) {
            val initialProducts = listOf(
                Product(
                    name = "Nordic Oak Study Table",
                    category = "Furniture",
                    price = 4599.00,
                    rating = 4.8f,
                    reviewsCount = 124,
                    imageResName = "table",
                    description = "Solid oak study desk featuring clean Scandinavian lines, visual cable management slot, and dual matte storage drawers.",
                    recommendedColor = "White Oak",
                    suggestedSize = "120cm x 60cm",
                    placementTip = "Place near window at a 90-degree angle for glare-free daytime natural lighting.",
                    whyItMatches = "Perfect fit for an organized, minimalist study setup or developer nook."
                ),
                Product(
                    name = "ErgoAdapt Floating Task Chair",
                    category = "Furniture",
                    price = 7499.00,
                    rating = 4.7f,
                    reviewsCount = 89,
                    imageResName = "chair",
                    description = "Ergonomic mesh-back support chair featuring structural lumbar adjustment, 3D soft armrests, and dynamic tension control.",
                    recommendedColor = "Slate Black / Cool Grey",
                    suggestedSize = "Standard Adjustable",
                    placementTip = "Slide underneath study table to reclaim room circulation space when not working.",
                    whyItMatches = "Provides critical lumbar longevity for programming, intense study, or gaming."
                ),
                Product(
                    name = "Slim modular Book Forest Shelf",
                    category = "Furniture",
                    price = 2899.00,
                    rating = 4.5f,
                    reviewsCount = 42,
                    imageResName = "bookshelf",
                    description = "Five-tier vertical metal and wood bookshelf designed to maximize wall space for folders, novels, and decorative succulents.",
                    recommendedColor = "Industrial Wood / Matte Black",
                    suggestedSize = "180cm x 45cm",
                    placementTip = "Anchor secure flat against any corner wall. Pairs ideally beside a reading nook chair.",
                    whyItMatches = "Adds height and elegant vertical texture to cozy reading or reference areas."
                ),
                Product(
                    name = "Nordic Cozy Bedside Table",
                    category = "Storage",
                    price = 1499.00,
                    rating = 4.6f,
                    reviewsCount = 65,
                    imageResName = "bedside_cabinet",
                    description = "Charming round dynamic nightstand with an integrated side-sliding cabinet door and solid pinewood supporting legs.",
                    recommendedColor = "Soft Pastel Cream / Ash Wash",
                    suggestedSize = "40cm x 50cm",
                    placementTip = "Stand directly adjacent to sleeping headboards.",
                    whyItMatches = "Elegant, low-profile item to store current books and smart devices overnight."
                ),
                Product(
                    name = "Geometric RGB Hexagon Panels",
                    category = "Lighting",
                    price = 1290.00,
                    rating = 4.9f,
                    reviewsCount = 196,
                    imageResName = "hex_lights",
                    description = "Modular smart LED interlocking panels. Sound-reactive visual themes, fully tunable through a phone app with 16 million colors.",
                    recommendedColor = "Rainbow / Neon Cyan",
                    suggestedSize = "Pack of 6 Panels",
                    placementTip = "Mount in symmetric honeycomb patterns directly on the wall behind monitor setups.",
                    whyItMatches = "Provides incredible mood backlighting and completes a gaming or workspace ambiance."
                ),
                Product(
                    name = "Brushed Brass Minimalist Lamp",
                    category = "Lighting",
                    price = 1899.00,
                    rating = 4.7f,
                    reviewsCount = 53,
                    imageResName = "brass_lamp",
                    description = "Slender gold metal desktop lamp with an adjustable heavy swivel head. Supports warm daylight color temperatures.",
                    recommendedColor = "Brushed Champagne Gold",
                    suggestedSize = "Height 45cm",
                    placementTip = "Position top-right of desks to minimize shadows while writing and sketching.",
                    whyItMatches = "Brings mid-century luxury detail and focused task light to functional desks."
                ),
                Product(
                    name = "Calm Horizon Framed Abstract Art",
                    category = "Paintings",
                    price = 999.00,
                    rating = 4.4f,
                    reviewsCount = 31,
                    imageResName = "wall_painting",
                    description = "Fine canvas art poster displaying a soothing, abstract color blocks horizon of deep navy, ochre, and sand beach tones.",
                    recommendedColor = "Textured Canvas / Black Frame",
                    suggestedSize = "60cm x 80cm",
                    placementTip = "Hang at absolute eye level (generally 145cm from floor) on empty accent walls.",
                    whyItMatches = "Grounds busy rooms with peaceful focal points and elegant visual harmony."
                ),
                Product(
                    name = "Lush Hanging Satin Pothos",
                    category = "Decor",
                    price = 399.00,
                    rating = 4.8f,
                    reviewsCount = 112,
                    imageResName = "hanging_plant",
                    description = "Real, low-maintenance evergreen trailing indoor plant in a lightweight macramé hanger. Purifies indoor bedroom air.",
                    recommendedColor = "Deep Forest Green",
                    suggestedSize = "Medium with Hanging Cord",
                    placementTip = "Suspend elegantly from ceiling hooks or top-most bookshelf shelves.",
                    whyItMatches = "Softens sharp furniture borders and inserts soothing biophilic nature lines."
                ),
                Product(
                    name = "Retro Dynamic Flip Desk Clock",
                    category = "Decor",
                    price = 850.00,
                    rating = 4.6f,
                    reviewsCount = 47,
                    imageResName = "flip_clock",
                    description = "Mechanical gear-driven auto flip clock with high contrast white-on-black retro numerals.",
                    recommendedColor = "Matte Midnight Black",
                    suggestedSize = "20cm x 10cm",
                    placementTip = "Display centered on shelves or sideboards.",
                    whyItMatches = "Adds gorgeous textural complexity and satisfies analog industrial design tastes."
                ),
                Product(
                    name = "Boho Geometric Soft Floor Rug",
                    category = "Decor",
                    price = 2299.00,
                    rating = 4.6f,
                    reviewsCount = 78,
                    imageResName = "geometric_rug",
                    description = "High-pile, luxurious plush area rug featuring modern diamond patterns. Noise-dampening underneath chairs.",
                    recommendedColor = "Off-White & Charcoal Grey",
                    suggestedSize = "150cm x 200cm",
                    placementTip = "Lay partially slipped underneath tables or modern beds to visually unite separate pieces.",
                    whyItMatches = "Enhances bare floor premium comfort and dramatically elevates spatial warmth."
                )
            )
            dao.insertProducts(initialProducts)
        }
    }

    // Cart
    val cartItems: Flow<List<CartItem>> = dao.getCartItems()

    suspend fun addToCart(product: Product, chosenColor: String) {
        val current = dao.getCartItems().firstOrNull()?.find { it.productId == product.id && it.chosenColor == chosenColor }
        if (current != null) {
            dao.updateCartQuantity(product.id, current.quantity + 1)
        } else {
            dao.addToCart(
                CartItem(
                    productId = product.id,
                    productName = product.name,
                    productPrice = product.price,
                    imageResName = product.imageResName,
                    quantity = 1,
                    chosenColor = chosenColor
                )
            )
        }
    }

    suspend fun removeFromCart(productId: Int) = dao.removeFromCart(productId)

    suspend fun updateCartQuantity(productId: Int, quantity: Int) = dao.updateCartQuantity(productId, quantity)

    suspend fun clearCart() = dao.clearCart()

    // Wishlist
    val wishlistItems: Flow<List<WishlistItem>> = dao.getWishlistItems()

    suspend fun toggleWishlist(product: Product) {
        val exists = dao.getWishlistItems().firstOrNull()?.any { it.productId == product.id } ?: false
        if (exists) {
            dao.removeFromWishlist(product.id)
        } else {
            dao.addToWishlist(
                WishlistItem(
                    productId = product.id,
                    productName = product.name,
                    price = product.price,
                    imageResName = product.imageResName
                )
            )
        }
    }

    fun isProductInWishlist(productId: Int): Flow<Boolean> = dao.isInWishlist(productId)

    // Saved Designs
    val savedDesigns: Flow<List<SavedDesign>> = dao.getSavedDesigns()

    suspend fun saveDesign(design: SavedDesign) = dao.saveDesign(design)

    suspend fun deleteDesign(id: Int) = dao.deleteDesign(id)

    // Chat History
    val chatMessages: Flow<List<ChatMessage>> = dao.getChatMessages()

    suspend fun addChatMessage(message: ChatMessage) = dao.insertChatMessage(message)

    suspend fun clearChat() = dao.clearChatMessages()

    // Analysis Reports
    val analysisReports: Flow<List<AnalysisReport>> = dao.getAnalysisReports()

    suspend fun saveAnalysisReport(report: AnalysisReport) = dao.insertAnalysisReport(report)
}
