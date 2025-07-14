package tj.app.chipta.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cinema_sessions")
data class CinemaSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val session_date: String?,
    val session_time: String?,
    val map_width: Int?,
    val map_height: Int?,
    val hall_name: String?,
    val merchant_id: Int?,
    val has_orzu: Boolean?,
    val has_started: Boolean?,
    val has_started_text: String?
)
