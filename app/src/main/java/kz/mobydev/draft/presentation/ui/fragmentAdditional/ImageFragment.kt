package kz.mobydev.draft.presentation.ui.fragmentAdditional

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView

import kz.mobydev.draft.databinding.FragmentImageBinding
import kz.mobydev.draft.presentation.ui.VM
import kz.mobydev.draft.presentation.ui.provideNavigationHost


class ImageFragment : Fragment() {
    private var binding: FragmentImageBinding? = null
    private val vm: VM by activityViewModels()

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true,false)
            additionalToolBarConfig(true, btnExitVisible = false, titleVisible = true, title = "Cурет")
            onClickListener(kz.mobydev.draft.R.id.action_imageFragment_to_aboutMovieFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        super.onResume()
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true,false)
            additionalToolBarConfig(true, btnExitVisible = false, titleVisible = true, title = "Cурет")
            onClickListener(kz.mobydev.draft.R.id.action_imageFragment_to_aboutMovieFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = binding?.imageView as PhotoView
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(kz.mobydev.draft.R.id.action_imageFragment_to_aboutMovieFragment)
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(viewLifecycleOwner, onBackPressedCallback)


        vm.imageLink.observe(viewLifecycleOwner){
            Glide.with(requireContext())
                .load(it.link)
                .into(imageView);
        }


        imageView.maximumScale = 10f // Set maximum zoom level

        // Set an optional OnScaleChangeListener to get notified when the zoom scale changes
        imageView.setOnScaleChangeListener { scaleFactor, focusX, focusY ->
            // Handle scale change event
        }
    }




}