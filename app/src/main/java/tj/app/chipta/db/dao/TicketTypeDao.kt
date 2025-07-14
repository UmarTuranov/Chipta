package tj.app.chipta.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tj.app.chipta.model.entity.TicketTypeEntity

@Dao
interface TicketTypeDao {
    @Query("SELECT * FROM ticket_types")
    suspend fun getAllTypes(): List<TicketTypeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(types: List<TicketTypeEntity>)

    @Query("DELETE FROM ticket_types")
    suspend fun clearTypes()
}
