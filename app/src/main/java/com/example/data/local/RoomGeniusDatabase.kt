package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.*

@Database(
    entities = [
        User::class,
        UserProfile::class,
        Product::class,
        CartItem::class,
        WishlistItem::class,
        SavedDesign::class,
        ChatMessage::class,
        AnalysisReport::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RoomGeniusDatabase : RoomDatabase() {
    abstract fun dao(): RoomGeniusDao

    companion object {
        @Volatile
        private var INSTANCE: RoomGeniusDatabase? = null

        fun getDatabase(context: Context): RoomGeniusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomGeniusDatabase::class.java,
                    "room_genius_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
