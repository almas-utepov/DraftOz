package kz.qazaq.qarapkor.presentation.ui.fragmentLog

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.network.api.ApiInterface
import kz.qazaq.qarapkor.data.network.api.ServiceBuilder
import kz.qazaq.qarapkor.data.network.model.LoginRequest
import kz.qazaq.qarapkor.data.network.model.LoginResponse
import kz.qazaq.qarapkor.databinding.FragmentRegistrationInBinding
import kz.qazaq.qarapkor.domain.usecase.EmailValidationUseCase
import kz.qazaq.qarapkor.presentation.ui.provideNavigationHost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationInFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationInBinding
    private val emailValidationUseCase = EmailValidationUseCase()
    private val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegistrationInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPassword()

        binding.apply {
            btnTextTransitionForLogIn.setOnClickListener {
                findNavController().navigate(R.id.action_registrationInFragment_to_logInFragment)
            }
            btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_registrationInFragment_to_logInFragment)
            }
            btnSendDateInDB.setOnClickListener {
                val email = binding.editTextEmailRegIn.text.toString()
                val password = binding.editTextPasswordRegIn.text.toString()
                val password2 = binding.editTextPasswordagainCheckRegIn.text.toString()

                validationEmail(
                    email = email.toString(),
                    password = password.toString(),
                    password2 = password2.toString()
                )

            }
        }



    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false, false)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false, false)
        }
    }

    private fun validationEmail(email: String, password: String, password2: String) {
        val checkEmail =  emailValidationUseCase.execute(email)
        val checkPassword = validationPassword(password, password2)
        if (checkEmail) {
            binding.textTvErrorEmailRegIn.visibility = View.GONE
            binding.editTextEmailRegIn.background = ContextCompat.getDrawable(requireContext(), R.drawable.style_edittext_on_any_touch)
                if (checkPassword) {
                    retrofitRegIn(email,password)
                } else {
                    binding.textTvErrorResultRegIn.visibility = View.VISIBLE
                    binding.textTvErrorEmailRegIn.text = "Email-мен қателіктер, басқа email қосыңыз"
                }
        } else {
            binding.textTvErrorEmailRegIn.visibility = View.VISIBLE
            binding.textTvErrorEmailRegIn.text = "Қате формат"
            binding.editTextEmailRegIn.background =
                ResourcesCompat.getDrawable(resources, R.drawable.style_edittext_error, null)
        }
    }

    private fun validationPassword(password: String, password2: String): Boolean {
        return if (password.length >= 6 && password2.length >= 6) {
            return if (password == password2) {
                binding.textTvErrorResultRegIn.visibility = View.GONE
                true
            } else {
                binding.textTvErrorResultRegIn.text = "Құпия сөздер бірдей емес"
                binding.textTvErrorResultRegIn.visibility = View.VISIBLE
                false
            }
        } else {
            binding.textTvErrorResultRegIn.text = "Құпия сөздің ұзындығы 6 таңбадан кем емес"
            binding.textTvErrorResultRegIn.visibility = View.VISIBLE
            false
        }

    }

    private fun showPassword(){
        val passwordPlace1 = binding.editTextPasswordRegIn
        val passwordPlace2 = binding.editTextPasswordagainCheckRegIn

        binding.btnShowPassword.setOnClickListener {

            if (passwordPlace1.transformationMethod == HideReturnsTransformationMethod.getInstance()){
                passwordPlace1.transformationMethod = PasswordTransformationMethod.getInstance()
            }else{
                passwordPlace1.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }

        }
        binding.btnShowPasswordAgain.setOnClickListener {

            if (passwordPlace2.transformationMethod == HideReturnsTransformationMethod.getInstance()){
                passwordPlace2.transformationMethod = PasswordTransformationMethod.getInstance()
            }else{
                passwordPlace2.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }

        }
    }
    private fun retrofitRegIn(email: String, password: String){
        val response = ServiceBuilder.buildService(ApiInterface::class.java)

        response.regIn(LoginRequest(email,password)).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    findNavController().navigate(R.id.logInFragment)
                }else{
                    binding.textTvErrorResultRegIn.visibility = View.VISIBLE
                    binding.textTvErrorEmailRegIn.text = "Email-мен қателіктер, басқа email қосыңыз"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}