package tj.app.chipta.db

import androidx.room.*

import tj.app.chipta.model.SeatEntity

@Dao
interface SeatDao {
    @Query("SELECT * FROM seats")
    suspend fun getAllSeats(): List<SeatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(seats: List<SeatEntity>)

    @Query("DELETE FROM seats")
    suspend fun clearSeats()
}
