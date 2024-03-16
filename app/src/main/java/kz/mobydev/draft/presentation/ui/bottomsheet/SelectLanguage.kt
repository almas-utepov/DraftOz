package kz.qazaq.qarapkor.presentation.ui.bottomsheet

import android.app.Dialog
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Locale
import kotlinx.coroutines.launch
import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.network.api.ApiInterface
import kz.qazaq.qarapkor.data.network.api.ServiceBuilder
import kz.qazaq.qarapkor.data.network.model.SelectLanguageModel
import kz.qazaq.qarapkor.data.network.model.UserInfoRequest
import kz.qazaq.qarapkor.data.preferences.PreferenceProvider
import kz.qazaq.qarapkor.databinding.BottomSheetLanguageBinding
import kz.qazaq.qarapkor.presentation.ui.VM
import kz.qazaq.qarapkor.presentation.ui.fragmentMain.ProfileFragment

class SelectLanguage : BottomSheetDialogFragment() {
    private var binding: BottomSheetLanguageBinding? = null
    private val vm: VM by activityViewModels()
    private var languageSelect: String = "null"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetLanguageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val systemLanguage = PreferenceProvider(requireContext()).getLanguage()
        systemLanguage(systemLanguage!!)
        binding?.run {
            btnSelectEnglish.setOnClickListener {
                updateLanguage("English")
                systemLanguage("English")


            }
            btnSelectQazaq.setOnClickListener {
                systemLanguage("Қазақша")
                updateLanguage("Қазақша")


            }
            btnSelectRussian.setOnClickListener {
                systemLanguage("Русский")
                updateLanguage("Русский")


            }
        }



    }


    private fun updateLanguage(language: String) {
        PreferenceProvider(requireContext()).saveLanguage(language)
    }

    fun systemLanguage(language: String) {
        when (language) {
            "English" -> {
                val locale = Locale("en")
                Locale.setDefault(locale)
                val config = Configuration()
                config.setLocale(locale)
                requireContext().resources.updateConfiguration(
                    config,
                    requireContext().resources.displayMetrics
                )

                binding?.imgIconBtnSelectQazaq?.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bottom_sheet_uncheck_icon
                    )
                binding?.imgIconBtnSelectEnglish?.background =
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bottom_sheet_check_icon
                )
                binding?.imgIconBtnSelectRussian?.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bottom_sheet_uncheck_icon
                    )
                vm.systemLanguage.value = SelectLanguageModel("English")
                findNavController().navigate(R.id.profileFragment ,  arguments,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.profileFragment, true)
                        .build()
                )

            }
            "Қазақша" -> {
                val locale = Locale("kk")
                Locale.setDefault(locale)
                val config = Configuration()
                config.setLocale(locale)
                requireContext().resources.updateConfiguration(
                    config,
                    requireContext().resources.displayMetrics
                )
                binding?.imgIconBtnSelectEnglish?.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bottom_sheet_uncheck_icon
                    )
                binding?.imgIconBtnSelectQazaq?.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bottom_sheet_check_icon
                    )
                binding?.imgIconBtnSelectRussian?.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bottom_sheet_uncheck_icon
                    )
                vm.systemLanguage.value = SelectLanguageModel("Қазақша")
                findNavController().navigate(R.id.profileFragment ,  arguments,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.profileFragment, true)
                        .build()
                )
            }
            "Русский" -> {
                val locale = Locale("ru")
                Locale.setDefault(locale)
                val config = Configuration()
                config.setLocale(locale)
                requireContext().resources.updateConfiguration(
                    config,
                    requireContext().resources.displayMetrics
                )
                binding?.imgIconBtnSelectEnglish?.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bottom_sheet_uncheck_icon
                    )
                binding?.imgIconBtnSelectRussian?.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bottom_sheet_check_icon
                    )
                binding?.imgIconBtnSelectQazaq?.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bottom_sheet_uncheck_icon
                    )

                vm.systemLanguage.value = SelectLanguageModel("Русский")
                findNavController().navigate(R.id.profileFragment ,  arguments,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.profileFragment, true)
                        .build()
                )
            }
            else -> {
                val locale = Locale("kk")
                Locale.setDefault(locale)
                val config = Configuration()
                config.setLocale(locale)
                requireContext().resources.updateConfiguration(
                    config,
                    requireContext().resources.displayMetrics
                )
                binding?.imgIconBtnSelectEnglish?.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bottom_sheet_uncheck_icon
                    )
                binding?.imgIconBtnSelectRussian?.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bottom_sheet_uncheck_icon
                    )
            }
        }
    }

}


/**
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
super.onViewCreated(view, savedInstanceState)
defaultLanguage()
vm.userInfo.observe(viewLifecycleOwner){ info->
binding?.apply {
btnSelectEnglish.setOnClickListener {
languageSelect = "English"
selectedLanguage(languageSelect)
lifecycleScope.launch {
updateLanguage(    userInfo = UserInfoRequest(
name = info.name, id = info.id, language = languageSelect, birthDate = info.birthDate.toString(), phoneNumber = info.phoneNumber.toString()
))
}
}
btnSelectQazaq.setOnClickListener {
languageSelect = "Қазақша"
selectedLanguage(languageSelect)
lifecycleScope.launch {
updateLanguage(    userInfo = UserInfoRequest(
name = info.name, id = info.id, language = languageSelect, birthDate = info.birthDate.toString(), phoneNumber = info.phoneNumber.toString()
))
}
}
btnSelectRussian.setOnClickListener {
languageSelect = "Русский"
selectedLanguage(languageSelect)
lifecycleScope.launch {
updateLanguage(    userInfo = UserInfoRequest(
name = info.name, id = info.id, language = languageSelect, birthDate = info.birthDate.toString(), phoneNumber = info.phoneNumber.toString()
))
}
}
}
}
}


private suspend fun updateLanguage(userInfo: UserInfoRequest){
val token = PreferenceProvider(requireContext().applicationContext).getToken()

val response = ServiceBuilder.buildService(ApiInterface::class.java)
val data = runCatching {
response.updateUserInfo("Bearer $token", userInfo)
}
.onFailure {
Toast.makeText(requireContext(), "Ақпарат жаңармады", Toast.LENGTH_SHORT).show()
}
vm.userInfo.value = data.getOrNull()
}
private fun defaultLanguage(){
vm.userInfo.observe(viewLifecycleOwner){ info->
languageSelect = info.language.toString()
selectedLanguage(languageSelect)
}

}
private fun selectedLanguage(language:String){
binding?.apply {
when (language) {
"null","kaz" -> {
imgIconBtnSelectEnglish.background = resources.getDrawable(R.drawable.bottom_sheet_uncheck_icon,null)
imgIconBtnSelectRussian.background = resources.getDrawable(R.drawable.bottom_sheet_uncheck_icon,null)
}
"English","eng" -> {
imgIconBtnSelectQazaq.background = resources.getDrawable(R.drawable.bottom_sheet_uncheck_icon,null)
imgIconBtnSelectRussian.background = resources.getDrawable(R.drawable.bottom_sheet_uncheck_icon,null)
}
"Қазақша" -> {
imgIconBtnSelectEnglish.background = resources.getDrawable(R.drawable.bottom_sheet_uncheck_icon,null)
imgIconBtnSelectRussian.background = resources.getDrawable(R.drawable.bottom_sheet_uncheck_icon,null)
}
"Русский","rus" -> {
imgIconBtnSelectEnglish.background = resources.getDrawable(R.drawable.bottom_sheet_uncheck_icon,null)
imgIconBtnSelectQazaq.background = resources.getDrawable(R.drawable.bottom_sheet_uncheck_icon,null)
}

}
}

}

private fun saveAction() {
dismiss()
}
 */