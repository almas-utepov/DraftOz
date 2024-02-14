package kz.mobydev.draft.data.network.model


import com.google.gson.annotations.SerializedName

class SeriesModel : ArrayList<SeriesModel.SeriesModelItem>(){
    data class SeriesModelItem(
        @SerializedName("id")
        val id: Int,
        @SerializedName("movieId")
        val movieId: Int,
        @SerializedName("number")
        val number: Int,
        @SerializedName("videos")
        val videos: List<Video>
    ) {
        data class Video(
            @SerializedName("id")
            val id: Int,
            @SerializedName("link")
            val link: String,
            @SerializedName("number")
            val number: Int,
            @SerializedName("seasonId")
            val seasonId: Int
        )
    }
}