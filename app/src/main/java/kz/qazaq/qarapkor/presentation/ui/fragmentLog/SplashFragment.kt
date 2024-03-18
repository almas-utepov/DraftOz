package kz.qazaq.qarapkor.presentation.ui.fragmentLog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.preferences.PreferenceProvider
import kz.qazaq.qarapkor.databinding.FragmentSplashBinding
import kz.qazaq.qarapkor.presentation.ui.provideNavigationHost

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = PreferenceProvider(requireContext()).getToken()

        lifecycleScope.launch {
            delay(1.seconds)
            if ("without_token" == token) {
                findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
            } else if (token != null && token.isNotEmpty()) {
                findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
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


}