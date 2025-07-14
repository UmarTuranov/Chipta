package tj.app.chipta.network

import retrofit2.Response
import retrofit2.http.GET
import tj.app.chipta.model.CinemaSessionModel

interface CinemaService {
    @GET("/test11/seat.json")
    suspend fun getCinemaSession(): Response<CinemaSessionModel>
}