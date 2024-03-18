package kz.qazaq.qarapkor.presentation.ui

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Locale
import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.preferences.PreferenceProvider
import kz.qazaq.qarapkor.databinding.ActivityMainBinding

import kz.qazaq.qarapkor.domain.Utils.NavigationHostProvider


class MainActivity : AppCompatActivity(), NavigationHostProvider {
    private var binding: ActivityMainBinding? = null
    private val vm: VM by viewModels()
    private lateinit var lifecycleRegistry: LifecycleRegistry


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)

        val isDarkModeEnabled = PreferenceProvider(this).getDarkModeEnabledState()
        if (isDarkModeEnabled) {
            setDefaultNightMode(
                if (isDarkModeEnabled) {
                    MODE_NIGHT_YES
                } else {
                    MODE_NIGHT_NO
                }
            )
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.bottomNavigationBarMainActivity?.apply {
            itemIconTintList = null
        }
        val language = PreferenceProvider(this).getLanguage()


        if (language == "English" || language == "Қазақша" || language == "Русский") {
            systemLanguage(language)
        } else {
            systemLanguage("Қазақша")

        }

        val navController = findNavController(R.id.nav_host_fragment)
        binding?.bottomNavigationBarMainActivity?.setupWithNavController(navController)

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
    }


    override fun setNavigationVisibility(visible: Boolean) {
        binding?.bottomNavigationBarMainActivity?.isVisible = visible
    }

    override fun setNavigationToolBar(
        visible: Boolean, isGone: Boolean

    ) {
        binding?.apply {
            toolbar.isVisible = visible
            linerLayoutToolbar.isGone = isGone

        }

    }

    override fun additionalToolBarConfig(
        btnBackVisible: Boolean, btnExitVisible: Boolean, titleVisible: Boolean, title: String
    ) {
        binding?.apply {
            btnBack.isVisible = btnBackVisible
            btnExit.isVisible = btnExitVisible
            titleToolbar.isVisible = titleVisible
            titleToolbar.text = title

        }
    }

    override fun onClickListener(id: Int) {
        binding?.apply {
            btnBack.setOnClickListener {
                findNavController(R.id.nav_host_fragment).navigate(id)
            }
        }

    }

    override fun showBottomSheetExit(unit: BottomSheetDialogFragment) {
        binding?.btnExit?.setOnClickListener {
            unit.show(supportFragmentManager, "")
        }

    }


    private fun systemLanguage(language: String) {
        when (language) {
            "English" -> {
                val locale = Locale("en")
                Locale.setDefault(locale)
                val config = Configuration()
                config.setLocale(locale)
                baseContext.resources.updateConfiguration(
                    config, baseContext.resources.displayMetrics
                )
            }
            "Қазақша" -> {
                val locale = Locale("kk")
                Locale.setDefault(locale)
                val config = Configuration()
                config.setLocale(locale)
                baseContext.resources.updateConfiguration(
                    config, baseContext.resources.displayMetrics
                )
            }

            "Русский" -> {
                val locale = Locale("ru")
                Locale.setDefault(locale)
                val config = Configuration()
                config.setLocale(locale)
                baseContext.resources.updateConfiguration(
                    config, baseContext.resources.displayMetrics
                )
            }
            else -> {
                val locale = Locale("kk")
                Locale.setDefault(locale)
                val config = Configuration()
                config.setLocale(locale)
                baseContext.resources.updateConfiguration(
                    config, baseContext.resources.displayMetrics
                )

            }
        }


    }
}
