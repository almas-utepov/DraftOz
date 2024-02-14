package kz.mobydev.draft.domain.Utils

import android.view.View.OnClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.mobydev.draft.databinding.ActivityMainBinding
import kz.mobydev.draft.presentation.ui.bottomsheet.SelectLanguage

interface NavigationHostProvider {
    fun setNavigationVisibility(visible: Boolean)
    fun setNavigationToolBar(visible: Boolean,isGone:Boolean)
    fun additionalToolBarConfig(btnBackVisible:Boolean, btnExitVisible:Boolean, titleVisible:Boolean,title:String)
    fun onClickListener(id:Int)
    fun showBottomSheetExit(unit:BottomSheetDialogFragment)
}