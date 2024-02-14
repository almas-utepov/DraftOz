package kz.mobydev.draft.domain.model

data class UserAuth(
    var email:String,
    var password:String,
    var checkPassword:String
)
