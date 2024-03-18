package kz.qazaq.qarapkor.domain.Utils

import android.view.View.OnClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.qazaq.qarapkor.databinding.ActivityMainBinding
import kz.qazaq.qarapkor.presentation.ui.bottomsheet.SelectLanguage

interface NavigationHostProvider {
    fun setNavigationVisibility(visible: Boolean)
    fun setNavigationToolBar(visible: Boolean,isGone:Boolean)
    fun additionalToolBarConfig(btnBackVisible:Boolean, btnExitVisible:Boolean, titleVisible:Boolean,title:String)
    fun onClickListener(id:Int)
    fun showBottomSheetExit(unit:BottomSheetDialogFragment)
}