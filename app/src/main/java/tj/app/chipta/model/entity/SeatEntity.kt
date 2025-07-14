package tj.app.chipta.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seats")
data class SeatEntity(
    @PrimaryKey
    val seat_id: Int,
    val sector: String?,
    val row_num: String?,
    val place: String?,
    val top: Int,
    val left: Int,
    val booked_seats: Int,
    val seat_view: String,
    val place_name: String?,
    val seat_type: String?,
    val object_type: String,
    val object_description: String,
    val object_title: String
)
