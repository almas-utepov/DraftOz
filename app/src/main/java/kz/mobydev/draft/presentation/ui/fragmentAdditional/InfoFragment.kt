package kz.qazaq.qarapkor.presentation.ui.fragmentAdditional


import MonthChange
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.ihermandev.formatwatcher.FormatWatcher
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.network.api.ApiInterface
import kz.qazaq.qarapkor.data.network.api.ServiceBuilder
import kz.qazaq.qarapkor.data.network.model.UserInfoRequest
import kz.qazaq.qarapkor.data.preferences.PreferenceProvider
import kz.qazaq.qarapkor.databinding.FragmentInfoBinding
import kz.qazaq.qarapkor.presentation.ui.VM
import kz.qazaq.qarapkor.presentation.ui.provideNavigationHost
import java.text.SimpleDateFormat
import java.util.*
import kz.qazaq.qarapkor.data.network.model.UserInfo

class InfoFragment : Fragment() {

    private var binding: FragmentInfoBinding? = null
    private val vm: VM by activityViewModels()

    private var birthDate: String = ""
    private var id: Int? = null
    private var language: String = ""
    private var name: String = ""
    private var phoneNumber: String = ""


    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true, false)
            additionalToolBarConfig(
                true,
                btnExitVisible = false,
                titleVisible = true,
                title = "Жеке деректер"
            )
            onClickListener(R.id.action_infoFragment_to_profileFragment)
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
                title = "Жеке деректер"
            )
            onClickListener(R.id.action_infoFragment_to_profileFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            getInfo()
        }


    }

    private suspend fun getInfo() {
        val token = PreferenceProvider(requireContext().applicationContext).getToken()
        val response = ServiceBuilder.buildService(ApiInterface::class.java)
        val data = runCatching {
            response.getUserInfo("Bearer $token")
        }
            .onFailure {
                Toast.makeText(requireContext(), "Ақпарат bazadan alynbady", Toast.LENGTH_SHORT)
                    .show()
            }.getOrNull()

        action(data!!)


    }


    private fun action(userInfo: UserInfo) {
        binding?.run {
            textTvEmail.text = userInfo.user.email
            val formatter = FormatWatcher("+7 ### ###-##-##", '#')
            editTextPhoneNumber.addTextChangedListener(formatter)

            if (userInfo.phoneNumber == null || userInfo.phoneNumber == "") {
                editTextPhoneNumber.setHint("Телефон нөміріңізді енгізіңіз...")
                editTextPhoneNumber.setText(null)
            } else {
                editTextPhoneNumber.setText(userInfo.phoneNumber.toString())
                editTextPhoneNumber.setHint("Телефон нөміріңізді енгізіңіз...")
            }



            if (userInfo.name == null || userInfo.name == "") {
                editTextName.setHint("Аты-жөніңізді еңгізіңіз...")
            } else {
                editTextName.setText(userInfo.name)
            }

            if (userInfo.birthDate == null || userInfo.birthDate == "1900-01-18") {
                textTvDate.text = "Туылған күніңізді еңгізіңіз..."
            } else {
                textTvDate.text = userInfo.birthDate.toString()
            }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateString = userInfo.birthDate
            if (dateString != null) {
                val date = dateFormat.parse(dateString.toString())
                val calendar = Calendar.getInstance()
                calendar.time = date
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val monthName = MonthChange().setMonthNames(month)
                val resultString = "$year ж. $day $monthName"
                binding?.textTvDate?.setText(resultString)
            } else {
                binding?.textTvDate?.setText(getString(R.string.SelectDate))
            }

            textTvDate.setOnClickListener {
                val builder = MaterialDatePicker.Builder.datePicker()
                    .setTheme(R.style.MaterialDatePickerTheme)
                    .setTitleText(R.string.SelectDate)

                val datePicker = builder.build()

                datePicker.show(childFragmentManager, "datePicker")

                datePicker.addOnPositiveButtonClickListener {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val dateString = dateFormat.format(Date(it))
                    val calendar = Calendar.getInstance()
                    val text = Date(it).toString()
                    calendar.time = Date(it)
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val montName = MonthChange().setMonthNames(month)


                    textTvDate.text = "$year ж. $day $montName"
                    birthDate = dateString ?: "1900-01-18"

                }
            }
            btnUpdateInformation.setOnClickListener {
                if (editTextName.text == null || editTextName.text.toString() == "") {
                    name = ""
                } else {
                    name = editTextName.text.toString()
                }
                phoneNumber = editTextPhoneNumber.text.toString()

                lifecycleScope.launch {
                    updateInfo(
                        UserInfoRequest(
                            birthDate = birthDate,
                            id = userInfo.id,
                            language = userInfo.language.toString(),
                            name = name,
                            phoneNumber = phoneNumber
                        )
                    )
                }
                findNavController().navigate(R.id.action_infoFragment_to_profileFragment)
            }
        }
    }

    private suspend fun updateInfo(userInfo: UserInfoRequest) {
        val token = PreferenceProvider(requireContext().applicationContext).getToken()
        val response = ServiceBuilder.buildService(ApiInterface::class.java)
        val data = runCatching {
            response.updateUserInfo("Bearer $token", userInfo)
        }
            .onFailure {
                Toast.makeText(requireContext(), "Ақпарат Update etilmedi", Toast.LENGTH_SHORT)
                    .show()
            }
        vm.userInfo.value = data.getOrNull()
    }
}



