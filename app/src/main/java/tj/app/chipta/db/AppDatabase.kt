package tj.app.chipta.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tj.app.chipta.db.dao.CinemaSessionDao
import tj.app.chipta.db.dao.SeatDao
import tj.app.chipta.db.dao.TicketTypeDao
import tj.app.chipta.model.entity.CinemaSessionEntity
import tj.app.chipta.model.entity.SeatEntity
import tj.app.chipta.model.entity.TicketTypeEntity

@Database(
    entities = [SeatEntity::class, TicketTypeEntity::class, CinemaSessionEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun seatDao(): SeatDao
    abstract fun ticketTypeDao(): TicketTypeDao
    abstract fun cinemaSessionDao(): CinemaSessionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chipta_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
