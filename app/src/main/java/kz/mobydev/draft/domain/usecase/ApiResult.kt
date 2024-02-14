package kz.mobydev.draft.domain.usecase

import kz.mobydev.draft.data.network.model.FavoriteModel
import kz.mobydev.draft.data.network.model.SimpleMovieModel

sealed class ApiResult {
    data class Success(val model: FavoriteModel): ApiResult()
    data class Error(val errorMessage: String?): ApiResult()
    object Loading : ApiResult()
}
