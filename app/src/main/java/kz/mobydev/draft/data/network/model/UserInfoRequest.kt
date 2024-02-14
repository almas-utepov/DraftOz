package kz.mobydev.draft.data.network.model


import com.google.gson.annotations.SerializedName

data class UserInfoRequest(
    @SerializedName("birthDate")
    val birthDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("language")
    val language: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)