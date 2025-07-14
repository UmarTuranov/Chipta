package tj.app.chipta.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tj.app.chipta.model.entity.CinemaSessionEntity

@Dao
interface CinemaSessionDao {
    @Query("SELECT * FROM cinema_sessions LIMIT 1")
    suspend fun getSession(): CinemaSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: CinemaSessionEntity)

    @Query("DELETE FROM cinema_sessions")
    suspend fun clear()
}
