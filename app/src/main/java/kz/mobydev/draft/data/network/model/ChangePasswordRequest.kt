package kz.mobydev.draft.data.network.model


import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("password")
    val password: String
)