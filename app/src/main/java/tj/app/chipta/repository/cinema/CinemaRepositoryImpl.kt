package tj.app.chipta.repository.cinema

import android.content.Context
import retrofit2.Response
import tj.app.chipta.db.AppDatabase
import tj.app.chipta.model.CinemaSessionModel
import tj.app.chipta.model.entity.CinemaSessionEntity
import tj.app.chipta.model.entity.SeatEntity
import tj.app.chipta.model.entity.TicketTypeEntity
import tj.app.chipta.network.CinemaService
import tj.app.chipta.network.RetrofitClient

class CinemaRepositoryImpl(context: Context) : CinemaRepository {

    private val request = RetrofitClient.sendRequest()
    private val service = request.create(CinemaService::class.java)

    private val database = AppDatabase.getDatabase(context)
    private val seatDao = database.seatDao()
    private val sessionDao = database.cinemaSessionDao()
    private val ticketTypeDao = database.ticketTypeDao()

    override suspend fun getCinemaSession(): Response<CinemaSessionModel> {
        val response = service.getCinemaSession()
        if (response.isSuccessful && response.body() != null) {
            val body = response.body()!!

            val seats = body.seats?.mapNotNull {
                it.seatId?.let { id ->
                    SeatEntity(
                        seat_id = id,
                        sector = it.sector ?: "",
                        row_num = it.rowNum ?: "",
                        place_name = it.placeName ?: "",
                        top = it.top ?: 0,
                        place = it.place ?: "",
                        left = it.left ?: 0,
                        booked_seats = it.bookedSeats ?: 0,
                        seat_view = it.seatView ?: "",
                        seat_type = it.seatType,
                        object_type = it.objectType ?: "",
                        object_description = it.objectDescription ?: "",
                        object_title = it.objectTitle ?: ""
                    )
                }
            } ?: emptyList()

            val ticketTypes = body.seatsType?.mapNotNull {
                it.ticketId?.let { id ->
                    TicketTypeEntity(
                        ticket_id = id,
                        ticket_type = it.ticketType,
                        name = it.name,
                        price = it.price,
                        seat_type = it.seatType
                    )
                }
            } ?: emptyList()


            val session = CinemaSessionEntity(
                session_date = body.sessionDate,
                session_time = body.sessionTime,
                map_width = body.mapWidth,
                map_height = body.mapHeight,
                hall_name = body.hallName,
                merchant_id = body.merchantId,
                has_orzu = body.hasOrzu,
                has_started = body.hasStarted,
                has_started_text = body.hasStartedText
            )
            seatDao.clearSeats()
            seatDao.insertAll(seats)

            ticketTypeDao.clearTypes()
            ticketTypeDao.insertAll(ticketTypes)

            sessionDao.clear()
            sessionDao.insert(session)
        }

        return response
    }

    suspend fun getCachedSeats(): List<SeatEntity> {
        return seatDao.getAllSeats()
    }

    suspend fun getCachedSession(): CinemaSessionEntity? {
        return sessionDao.getSession()
    }

    suspend fun getCachedTicketTypes(): List<TicketTypeEntity> {
        return ticketTypeDao.getAllTypes()
    }
}
