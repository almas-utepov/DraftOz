package kz.mobydev.draft.domain.usecase

import android.util.Patterns

class EmailValidationUseCase(){

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    fun execute(email:String):Boolean{
        return email.trim().matches(emailPattern.toRegex())
//        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}