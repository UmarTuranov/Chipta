package tj.app.chipta.repository.cinema

import retrofit2.Response
import tj.app.chipta.model.CinemaSessionModel

interface CinemaRepository {
    suspend fun getCinemaSession(): Response<CinemaSessionModel>
}