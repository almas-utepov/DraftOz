package kz.mobydev.draft.data.network.api

import kz.mobydev.draft.data.network.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("/core/V1/movies/page")
    suspend fun getCategoryMovie(
        @Header("Authorization") token: String,
        @Query("categoryId") categoryId: Int,
        @Query("direction") direction: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sortField") sortField: String
    ):CategoryMovieResponse

    @GET("/core/V1/movies/search")
    suspend fun getSearchMovie(
        @Header("Authorization") token: String,
        @Query("credentials") credentials: String,
        @Query("details") details: String,
        @Query("principal") principal: String,
        @Query("search") search: String
    ):SearchResponseModel

    @HTTP(method = "PUT", path = "/core/V1/user/profile/", hasBody = true)
    suspend fun updateUserInfo(
        @Header("Authorization") token: String,
        @Body requestUserInfo: UserInfoRequest
    ): UserInfo

    @HTTP(method = "PUT", path = "/core/V1/user/profile/", hasBody = true)
    suspend fun updateUserPassword(
        @Header("Authorization") token: String,
        @Body requestUserInfo: ChangePasswordRequest
    ): ChangePasswordResponse


    @POST("/auth/V1/signin")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/auth/V1/signup")
    fun regIn(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/core/V1/favorite")
    suspend fun addToFavorite(
        @Header("Authorization") token: String,
        @Body movieId: MovieIdModel
    ): MovieIdModel

    @HTTP(method = "DELETE", path = "/core/V1/favorite/", hasBody = true)
    suspend fun deleteAtFavorite(
        @Header("Authorization") token: String,
        @Body movieId: MovieIdModel
    )


    @GET("/core/V1/movies/{id}")
    suspend fun movieWithId(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): MovieInfoResponse

    @GET("/core/V1/seasons/{movieId}")
    suspend fun getSeries(
        @Header("Authorization") token: String,
        @Path("movieId") id: Int
    ): SeriesModel


    @GET("/core/V1/favorite/")
    suspend fun getFavoriteMovies(@Header("Authorization") token: String): FavoriteModel

    @GET("/core/V1/user/profile")
    suspend fun getUserInfo(@Header("Authorization") token: String): UserInfo

    @GET("/core/V1/movies_main")
    suspend fun moviesMain(@Header("Authorization") token: String): MoviesMain

    @GET("/core/V1/movies/main")
    suspend fun moviesMainPage(@Header("Authorization") token: String): MainPageModel

    @GET("/core/V1/genres")
    suspend fun getGenre(@Header("Authorization") token: String): GenreResponse


}