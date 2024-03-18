package kz.qazaq.qarapkor.presentation.ui.fragmentAdditional

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.network.api.ApiInterface
import kz.qazaq.qarapkor.data.network.api.ServiceBuilder
import kz.qazaq.qarapkor.data.network.model.ChangePasswordRequest
import kz.qazaq.qarapkor.data.preferences.PreferenceProvider
import kz.qazaq.qarapkor.databinding.FragmentRePasswordBinding
import kz.qazaq.qarapkor.presentation.ui.provideNavigationHost

class RePasswordFragment : Fragment() {
   private var binding: FragmentRePasswordBinding? = null


    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true, false)
            additionalToolBarConfig(
                true,
                btnExitVisible = false,
                titleVisible = true,
                title = "Құпия сөзді өзгерту"
            )
            onClickListener(R.id.action_rePasswordFragment_to_profileFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true, false)
            additionalToolBarConfig(
                true,
                btnExitVisible = false,
                titleVisible = true,
                title = "Құпия сөзді өзгерту"
            )
            onClickListener(R.id.action_rePasswordFragment_to_profileFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRePasswordBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_rePasswordFragment_to_profileFragment)
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(viewLifecycleOwner, onBackPressedCallback)


        binding?.apply {
            showPassword()
            btnSendDateInDB.setOnClickListener {
                val password1 = editTextPasswordRegIn.text
                val password2 = editTextPasswordRegIn.text
                if (validationPassword(password1.toString(), password2.toString())){
                    lifecycleScope.launch{
                        updatePassword(password1.toString())
                    }
                }else{
                    textTvErrorResultRegIn.visibility = View.VISIBLE
                }



            }
        }
    }

    suspend fun updatePassword(password: String){
        val response = ServiceBuilder.buildService(ApiInterface::class.java)
        val token = PreferenceProvider(requireContext()).getToken()
        kotlin.runCatching {
            response.updateUserPassword("Bearer $token", ChangePasswordRequest(password))
        }.onSuccess {
            PreferenceProvider(requireContext()).saveToken(it.accessToken)
            findNavController().navigate(R.id.action_rePasswordFragment_to_profileFragment)
        }.onFailure {  }.getOrNull()
    }
    private fun showPassword(){
        val passwordPlace1 = binding?.editTextPasswordRegIn
        val passwordPlace2 = binding?.editTextPasswordagainCheckRegIn

        binding?.btnShowPassword?.setOnClickListener {

            if (passwordPlace1?.transformationMethod == HideReturnsTransformationMethod.getInstance()){
                passwordPlace1?.transformationMethod = PasswordTransformationMethod.getInstance()
            }else{
                passwordPlace1?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }

        }
        binding?.btnShowPasswordAgain?.setOnClickListener {

            if (passwordPlace2?.transformationMethod == HideReturnsTransformationMethod.getInstance()){
                passwordPlace2?.transformationMethod = PasswordTransformationMethod.getInstance()
            }else{
                passwordPlace2?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }

        }
    }
    private fun validationPassword(password: String, password2: String): Boolean {
        return if (password.length >= 6 && password2.length >= 6) {
            return if (password == password2) {
                binding?.textTvErrorResultRegIn?.visibility = View.GONE
                true
            } else {
                binding?.textTvErrorResultRegIn?.text = "Құпия сөздер бірдей емес"
                binding?.textTvErrorResultRegIn?.visibility = View.VISIBLE
                false
            }
        } else {
            binding?.textTvErrorResultRegIn?.text = "Құпия сөздің ұзындығы 6 таңбадан кем емес"
            binding?.textTvErrorResultRegIn?.visibility = View.VISIBLE
            false
        }

    }
}