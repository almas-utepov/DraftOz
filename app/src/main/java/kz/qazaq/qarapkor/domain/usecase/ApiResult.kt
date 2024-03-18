package kz.qazaq.qarapkor.domain.usecase

import kz.qazaq.qarapkor.data.network.model.FavoriteModel
import kz.qazaq.qarapkor.data.network.model.SimpleMovieModel

sealed class ApiResult {
    data class Success(val model: FavoriteModel): ApiResult()
    data class Error(val errorMessage: String?): ApiResult()
    object Loading : ApiResult()
}
