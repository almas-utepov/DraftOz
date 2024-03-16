package kz.qazaq.qarapkor.presentation.ui.bottomsheet

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.preferences.PreferenceProvider
import kz.qazaq.qarapkor.databinding.BottomSheetExitBinding

class ExitAccount :BottomSheetDialogFragment() {

    private var binding:BottomSheetExitBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetExitBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding?.run {

            btnLogOut.setOnClickListener {
                PreferenceProvider(requireContext()).saveDarkModeEnabledState(true)
                PreferenceProvider(requireContext()).saveToken("without_token")
                PreferenceProvider(requireContext()).clearShared()
                findNavController().navigate(R.id.action_profileFragment_to_splashFragment)
                dialog?.dismiss()
            }
            btnCancel.setOnClickListener {
                    dialog?.dismiss()

            }
        }

    }

}