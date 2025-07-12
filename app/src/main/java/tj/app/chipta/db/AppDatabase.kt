package tj.app.chipta.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tj.app.chipta.model.SeatEntity

@Database(entities = [SeatEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun seatDao(): SeatDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cinema_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
