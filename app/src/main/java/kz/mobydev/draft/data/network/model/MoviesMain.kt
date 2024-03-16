package kz.qazaq.qarapkor.data.network.model


import com.google.gson.annotations.SerializedName

class MoviesMain : ArrayList<MoviesMain.MoviesMainItem>(){
    data class MoviesMainItem(
        @SerializedName("fileId")
        val fileId: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("movie")
        val movie: Movie,
        @SerializedName("sortOrder")
        val sortOrder: Int
    ) {
        data class Movie(
            @SerializedName("categories")
            val categories: List<Category>,
            @SerializedName("categoryAges")
            val categoryAges: List<CategoryAge>,
            @SerializedName("createdDate")
            val createdDate: String,
            @SerializedName("description")
            val description: String,
            @SerializedName("director")
            val director: String,
            @SerializedName("favorite")
            val favorite: Boolean,
            @SerializedName("genres")
            val genres: List<Genre>,
            @SerializedName("id")
            val id: Int,
            @SerializedName("keyWords")
            val keyWords: String,
            @SerializedName("lastModifiedDate")
            val lastModifiedDate: String,
            @SerializedName("movieType")
            val movieType: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("poster")
            val poster: Poster,
            @SerializedName("producer")
            val producer: String,
            @SerializedName("screenshots")
            val screenshots: List<Screenshot>,
            @SerializedName("seasonCount")
            val seasonCount: Int,
            @SerializedName("seriesCount")
            val seriesCount: Int,
            @SerializedName("timing")
            val timing: Int,
            @SerializedName("trend")
            val trend: Boolean,
            @SerializedName("video")
            val video: Video,
            @SerializedName("watchCount")
            val watchCount: Int,
            @SerializedName("year")
            val year: Int
        ) {
            data class Category(
                @SerializedName("fileId")
                val fileId: Any,
                @SerializedName("id")
                val id: Int,
                @SerializedName("link")
                val link: Any,
                @SerializedName("movieCount")
                val movieCount: Any,
                @SerializedName("name")
                val name: String
            )
    
            data class CategoryAge(
                @SerializedName("fileId")
                val fileId: Int,
                @SerializedName("id")
                val id: Int,
                @SerializedName("link")
                val link: String,
                @SerializedName("movieCount")
                val movieCount: Any,
                @SerializedName("name")
                val name: String
            )
    
            data class Genre(
                @SerializedName("fileId")
                val fileId: Int,
                @SerializedName("id")
                val id: Int,
                @SerializedName("link")
                val link: String,
                @SerializedName("movieCount")
                val movieCount: Any,
                @SerializedName("name")
                val name: String
            )
    
            data class Poster(
                @SerializedName("fileId")
                val fileId: Int,
                @SerializedName("id")
                val id: Int,
                @SerializedName("link")
                val link: String,
                @SerializedName("movieId")
                val movieId: Int
            )
    
            data class Screenshot(
                @SerializedName("fileId")
                val fileId: Int,
                @SerializedName("id")
                val id: Int,
                @SerializedName("link")
                val link: String,
                @SerializedName("movieId")
                val movieId: Int
            )
    
            data class Video(
                @SerializedName("id")
                val id: Int,
                @SerializedName("link")
                val link: String,
                @SerializedName("number")
                val number: Int,
                @SerializedName("seasonId")
                val seasonId: Any
            )
        }
    }
}