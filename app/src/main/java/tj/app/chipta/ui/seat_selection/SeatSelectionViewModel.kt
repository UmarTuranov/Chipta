package tj.app.chipta.ui.seat_selection

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tj.app.chipta.R
import tj.app.chipta.model.CinemaSessionModel
import tj.app.chipta.model.SeatModel
import tj.app.chipta.model.TicketTypeModel
import tj.app.chipta.repository.cinema.CinemaRepositoryImpl
import tj.app.chipta.network.NetworkUtils

class SeatSelectionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CinemaRepositoryImpl(application)

    private val _sessionData = MutableLiveData<CinemaSessionModel?>()
    val sessionData: LiveData<CinemaSessionModel?> = _sessionData

    private val _error = MutableLiveData<Pair<String, String>?>()
    val error: LiveData<Pair<String, String>?> = _error

    fun loadSession() {
        CoroutineScope(Dispatchers.IO).launch {
            val isOnline = NetworkUtils.isInternetAvailable(getApplication())
            try {
                if (!isOnline) {
                    val cachedSeats = repository.getCachedSeats()
                    val cachedSession = repository.getCachedSession()
                    val cachedTicketTypes = repository.getCachedTicketTypes()
                    if (cachedSession != null && cachedSeats.isNotEmpty() && cachedTicketTypes.isNotEmpty()) {
                        _sessionData.postValue(
                            CinemaSessionModel(
                                sessionDate = cachedSession.session_date,
                                sessionTime = cachedSession.session_time,
                                mapWidth = cachedSession.map_width,
                                mapHeight = cachedSession.map_height,
                                hallName = cachedSession.hall_name,
                                merchantId = cachedSession.merchant_id,
                                hasOrzu = cachedSession.has_orzu,
                                hasStarted = cachedSession.has_started,
                                hasStartedText = cachedSession.has_started_text,
                                seats = cachedSeats.map {
                                    SeatModel(
                                        seatId = it.seat_id,
                                        sector = it.sector,
                                        rowNum = it.row_num,
                                        place = it.place,
                                        top = it.top,
                                        left = it.left,
                                        bookedSeats = it.booked_seats,
                                        seatView = it.seat_view,
                                        placeName = it.place_name,
                                        seatType = it.seat_type,
                                        objectType = it.object_type,
                                        objectDescription = it.object_description,
                                        objectTitle = it.object_title,
                                        isSelectedSeat = false
                                    )
                                },
                                seatsType = cachedTicketTypes.map {
                                    TicketTypeModel(
                                        ticketId = it.ticket_id,
                                        ticketType = it.ticket_type,
                                        name = it.name,
                                        price = it.price,
                                        seatType = it.seat_type
                                    )
                                }
                            )
                        )
                    } else {
                        _error.postValue(
                            Pair(getString(R.string.alert_network_unavailable_title), getString(R.string.alert_network_unavailable_message))
                        )
                    }
                } else {
                    val response = repository.getCinemaSession()
                    _sessionData.postValue(response.body())
                }
            } catch (e: Exception) {
                _error.postValue(
                    Pair(getString(R.string.alert_title_error), getString(R.string.alert_message_generic))
                )
            }
        }
    }

    private fun getString(resId: Int): String {
        return getApplication<Application>().getString(resId)
    }
}