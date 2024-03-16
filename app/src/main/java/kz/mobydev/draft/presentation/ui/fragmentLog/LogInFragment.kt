package kz.qazaq.qarapkor.presentation.ui.fragmentLog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController


import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.network.api.ApiInterface
import kz.qazaq.qarapkor.data.network.api.ServiceBuilder
import kz.qazaq.qarapkor.data.network.model.LoginRequest
import kz.qazaq.qarapkor.data.network.model.LoginResponse
import kz.qazaq.qarapkor.data.preferences.PreferenceProvider
import kz.qazaq.qarapkor.databinding.FragmentLogInBinding
import kz.qazaq.qarapkor.domain.usecase.EmailValidationUseCase
import kz.qazaq.qarapkor.presentation.ui.provideNavigationHost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInFragment : Fragment() {


    private lateinit var binding: FragmentLogInBinding

    private val emailValidationUseCase = EmailValidationUseCase()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(layoutInflater, container, false)
        return binding.root
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val passwordPlace = binding.editTextPasswordLogIn


        binding.btnShowPassword.setOnClickListener {

            if (passwordPlace.transformationMethod == HideReturnsTransformationMethod.getInstance()){
                passwordPlace.transformationMethod = PasswordTransformationMethod.getInstance()
            }else{
                passwordPlace.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }

        }



        binding.btnBackWelcomeFragment.setOnClickListener {
            findNavController().navigate(R.id.welcomeFragment)
        }
        binding.btnTextTransitionForRegIn.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_registrationInFragment)
        }

        binding.btnLogInApp.setOnClickListener {
            val email = binding.editTextEmailLogIn.text
            val password = binding.editTextPasswordLogIn.text

            //Validation Email
            val validResultEmail: Boolean = emailValidationUseCase.execute(email.toString())
            validationEmail(validResultEmail)
            //Password Validation
            if (validationPassword(password = password.toString())) {
                if (email.toString().isNotEmpty()) {
                   logInOzinshe(email.toString(), password.toString())

                } else {
                    validationEmail(validResultEmail)
                }
            }

        }


    }

                         /**               Additional  code                    */

    private fun validationEmail(validResultEmail: Boolean) {
        if (validResultEmail) {
            if (binding.textTvErrorEmailLogIn.text.toString() == getString(R.string.errorFormatKZ)) {
                binding.textTvErrorEmailLogIn.text = "default"
                binding.textTvErrorEmailLogIn.visibility = View.GONE
                binding.editTextEmailLogIn.background =
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.style_edittext_on_any_touch,
                        null
                    )
            }
        } else {
            binding.textTvErrorEmailLogIn.visibility = View.VISIBLE
            binding.textTvErrorEmailLogIn.text = "Қате формат"
            binding.editTextEmailLogIn.background =
                ResourcesCompat.getDrawable(resources, R.drawable.style_edittext_error, null)
        }
    }

    private fun validationPassword(password: String): Boolean {
        return if (password.length < 6) {
            binding.textTvErrorResultLogIn.text = "Құпия сөздің ұзындығы 6 таңбадан кем емес"
            binding.textTvErrorResultLogIn.visibility = View.VISIBLE
            false
        } else {

            binding.textTvErrorResultLogIn.visibility = View.GONE
            true
        }

    }

    private fun logInOzinshe(email:String, password: String){

        val response = ServiceBuilder.buildService(ApiInterface::class.java)

        response.login(LoginRequest(email, password)).enqueue(
            object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful){
                        findNavController().navigate(R.id.mainFragment)
                        PreferenceProvider(requireContext()).saveToken(response.body()?.accessToken!!)
                        PreferenceProvider(requireContext()).saveLanguage("Қазақша")
                    }else{
                        binding.textTvErrorResultLogIn.visibility = View.VISIBLE
                        binding.textTvErrorResultLogIn.text ="Қате email немесе Құпия сөз"
                    }


                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                }

            }
        )
    }


}