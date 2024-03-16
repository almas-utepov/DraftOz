package kz.qazaq.qarapkor.data.network.model


import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("birthDate")
    val birthDate: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("language")
    val language: Any,
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: Any,
    @SerializedName("user")
    val user: User
) {
    data class User(
        @SerializedName("email")
        val email: String,
        @SerializedName("id")
        val id: Int
    )
}