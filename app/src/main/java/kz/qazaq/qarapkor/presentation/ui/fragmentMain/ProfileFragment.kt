package kz.qazaq.qarapkor.presentation.ui.fragmentMain

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import java.util.Locale
import kotlinx.coroutines.launch
import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.network.api.ApiInterface
import kz.qazaq.qarapkor.data.network.api.ServiceBuilder
import kz.qazaq.qarapkor.data.network.model.SelectLanguageModel
import kz.qazaq.qarapkor.data.preferences.PreferenceProvider
import kz.qazaq.qarapkor.databinding.FragmentProfileBinding
import kz.qazaq.qarapkor.presentation.ui.VM
import kz.qazaq.qarapkor.presentation.ui.bottomsheet.ExitAccount
import kz.qazaq.qarapkor.presentation.ui.bottomsheet.SelectLanguage
import kz.qazaq.qarapkor.presentation.ui.provideNavigationHost


class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null
    private val vm: VM by activityViewModels()
    private var language: String? = null

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(true)
            setNavigationToolBar(true, false)
            additionalToolBarConfig(
                false,
                btnExitVisible = true,
                titleVisible = true,
                title = "Профиль"
            )
            showBottomSheetExit(ExitAccount())
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(true)
            setNavigationToolBar(true, false)
            additionalToolBarConfig(
                false,
                btnExitVisible = true,
                titleVisible = true,
                title = getString(R.string.Profile)
            )
            showBottomSheetExit(ExitAccount())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        systemLanguage()

        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            transaction.setReorderingAllowed(false)
        }
        transaction.detach(this).attach(this).commit()

        binding?.run {
            btnChangePassword1.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_rePasswordFragment)
            }
            btnChangePassword2.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_rePasswordFragment)
            }

        }
        binding?.btnSelectLanguageIcon?.performClick()
        lifecycleScope.launch {
            userInfo()
        }
        binding?.btnImgJekeDerekter?.setOnClickListener {
            findNavController().navigate(R.id.infoFragment)
        }
        binding?.btnInfoTransaction?.setOnClickListener {
            findNavController().navigate(R.id.infoFragment)
        }
        binding?.run {
            btnSelectLanguageIcon.setOnClickListener {
                SelectLanguage().show(parentFragmentManager, "")
            }
            textTvSelectLanguageText.setOnClickListener {
                SelectLanguage().show(parentFragmentManager, "")
            }
        }
        binding?.run {
            dayNightSwitch.isChecked = PreferenceProvider(requireContext()).getDarkModeEnabledState()
            dayNightSwitch.setOnCheckedChangeListener { _, enabled ->
                PreferenceProvider(requireContext()).saveDarkModeEnabledState(enabled)
                AppCompatDelegate.setDefaultNightMode(
                    if (enabled) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    }
                )
                activity?.recreate()
            }
        }

        vm.systemLanguage.observe(viewLifecycleOwner) { language ->
            if (language != null) {
                binding?.textTvSelectLanguageText?.text = language.language
            } else {
                binding?.textTvSelectLanguageText?.text = "Қазақша"
            }

        }
    }

    override fun onDestroyView() {
        binding?.run {
            dayNightSwitch.setOnCheckedChangeListener(null)
        }
        binding = null
        super.onDestroyView()
    }


    suspend fun userInfo() {
        val response = ServiceBuilder.buildService(ApiInterface::class.java)
        val token = PreferenceProvider(requireContext()).getToken()
        kotlin.runCatching {
            response.getUserInfo("Bearer $token")
        }.onSuccess {
            vm.userInfo.value = it
            binding?.textTvEmailUser?.text = it.user.email

        }.onFailure {
            binding?.textTvEmailUser?.text = "Желіде қателік"
        }

    }


    private fun systemLanguage() {
        when (PreferenceProvider(requireContext()).getLanguage()) {
            "English" -> {
                val locale = Locale("en")
                Locale.setDefault(locale)
                val config = Configuration()
                config.setLocale(locale)
                requireContext().resources.updateConfiguration(
                    config,
                    requireContext().resources.displayMetrics
                )

                vm.systemLanguage.value = SelectLanguageModel("English")


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

                vm.systemLanguage.value = SelectLanguageModel("Қазақша")

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


                vm.systemLanguage.value = SelectLanguageModel("Русский")

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

            }
        }
    }

//    private suspend fun updateInfo(userInfo: UserInfoRequest) {
//        val token = PreferenceProvider(requireContext().applicationContext).getToken()
//
//
//        val response = ServiceBuilder.buildService(ApiInterface::class.java)
//        val data = runCatching {
//            response.updateUserInfo("Bearer $token", userInfo)
//        }
//            .onFailure {
//                Toast.makeText(requireContext(), "Ақпарат жаңармады", Toast.LENGTH_SHORT).show()
//            }
//        vm.userInfo.value = data.getOrNull()
//    }
}
