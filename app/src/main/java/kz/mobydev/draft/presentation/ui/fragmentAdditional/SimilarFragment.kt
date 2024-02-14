package kz.mobydev.draft.presentation.ui.fragmentAdditional

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.github.chrisbanes.photoview.PhotoView
import kz.mobydev.draft.R
import kz.mobydev.draft.databinding.FragmentSimilarBinding
import kz.mobydev.draft.presentation.ui.provideNavigationHost

class SimilarFragment : Fragment() {

    private var binding: FragmentSimilarBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSimilarBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true,false)
            additionalToolBarConfig(true, btnExitVisible = false, titleVisible = true, title = "Ұқсас телехикаялар")
            onClickListener(kz.mobydev.draft.R.id.action_similarFragment_to_aboutMovieFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        super.onResume()
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true,false)
            additionalToolBarConfig(true, btnExitVisible = false, titleVisible = true, title = "Ұқсас телехикаялар")
            onClickListener(kz.mobydev.draft.R.id.action_similarFragment_to_aboutMovieFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(kz.mobydev.draft.R.id.action_similarFragment_to_aboutMovieFragment)
            }
        }
        requireActivity().getOnBackPressedDispatcher()
            .addCallback(viewLifecycleOwner, onBackPressedCallback)
    }
}