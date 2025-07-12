package tj.app.chipta.repository.cinema

import android.content.Context
import retrofit2.Response
import tj.app.chipta.db.AppDatabase
import tj.app.chipta.model.CinemaSessionModel
import tj.app.chipta.model.SeatEntity
import tj.app.chipta.network.CinemaService
import tj.app.chipta.network.RetrofitClient

class CinemaRepositoryImpl(context: Context) : CinemaRepository {

    private val request = RetrofitClient.sendRequest()
    private val service = request.create(CinemaService::class.java)

    private val seatDao = AppDatabase.getDatabase(context).seatDao()

    override suspend fun getCinemaSession(): Response<CinemaSessionModel> {
        val response = service.getCinemaSession()
        if (response.isSuccessful && response.body() != null) {
            val seats = response.body()!!.seats?.map {
                SeatEntity(
                    seat_id = it.seatId!!,
                    sector = it.sector,
                    row_num = it.rowNum,
                    place = it.place,
                    top = it.top ?: 0,
                    left = it.left ?: 0,
                    seat_view = it.seatView ?: "",
                    seat_type = it.seatType,
                    object_type = it.objectType ?: "",
                    object_title = it.objectTitle ?: "",
                    object_description = it.objectDescription ?: "",
                    booked_seats = it.bookedSeats ?: 0
                )
            }
            seatDao.clearSeats()
            seatDao.insertAll(seats ?: listOf())
        }
        return response
    }

    suspend fun getCachedSeats(): List<SeatEntity> {
        return seatDao.getAllSeats()
    }
}
