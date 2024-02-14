package kz.mobydev.draft.data.network.model


import com.google.gson.annotations.SerializedName

class GenreResponse : ArrayList<GenreResponse.GenreResponseItem>(){
    data class GenreResponseItem(
        @SerializedName("fileId")
        val fileId: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("movieCount")
        val movieCount: Int,
        @SerializedName("name")
        val name: String
    )
}