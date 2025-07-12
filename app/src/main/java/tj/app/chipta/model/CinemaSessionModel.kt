package tj.app.chipta.model

import com.google.gson.annotations.SerializedName

data class CinemaSessionModel(
    @SerializedName("session_date")
    val sessionDate: String?,

    @SerializedName("session_time")
    val sessionTime: String?,

    @SerializedName("map_width")
    val mapWidth: Int?,

    @SerializedName("map_height")
    val mapHeight: Int?,

    @SerializedName("hall_name")
    val hallName: String?,

    @SerializedName("merchant_id")
    val merchantId: Int?,

    @SerializedName("has_orzu")
    val hasOrzu: Boolean?,

    @SerializedName("has_started")
    val hasStarted: Boolean?,

    @SerializedName("has_started_text")
    val hasStartedText: String?,

    @SerializedName("seats")
    val seats: List<SeatModel>?,

    @SerializedName("seats_type")
    val seatsType: List<TicketTypeModel>?
)

data class SeatModel(
    @SerializedName("seat_id")
    val seatId: Int?,

    @SerializedName("sector")
    val sector: String?,

    @SerializedName("row_num")
    val rowNum: String?,

    @SerializedName("place")
    val place: String?,

    @SerializedName("top")
    val top: Int?,

    @SerializedName("left")
    val left: Int?,

    @SerializedName("booked_seats")
    val bookedSeats: Int?,

    @SerializedName("seat_view")
    val seatView: String?,

    @SerializedName("place_name")
    val placeName: String?,

    @SerializedName("seat_type")
    val seatType: String?,

    @SerializedName("object_type")
    val objectType: String?,

    @SerializedName("object_description")
    val objectDescription: String?,

    @SerializedName("object_title")
    val objectTitle: String?
)

data class TicketTypeModel(
    @SerializedName("ticket_id")
    val ticketId: Int?,

    @SerializedName("ticket_type")
    val ticketType: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("price")
    val price: Int?,

    @SerializedName("seat_type")
    val seatType: String?
)
