package tj.app.chipta.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ticket_types")
data class TicketTypeEntity(
    @PrimaryKey
    val ticket_id: Int,
    val ticket_type: String?,
    val name: String?,
    val price: Int?,
    val seat_type: String?
)
