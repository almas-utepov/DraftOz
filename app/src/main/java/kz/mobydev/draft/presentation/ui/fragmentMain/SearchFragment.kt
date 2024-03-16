package kz.qazaq.qarapkor.presentation.ui.fragmentMain

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kz.qazaq.qarapkor.R
import kz.qazaq.qarapkor.data.network.api.ApiInterface
import kz.qazaq.qarapkor.data.network.api.ServiceBuilder
import kz.qazaq.qarapkor.data.network.model.CategoryId
import kz.qazaq.qarapkor.data.network.model.CategoryMovieResponse
import kz.qazaq.qarapkor.data.network.model.MovieIdModel
import kz.qazaq.qarapkor.data.network.model.NamePage
import kz.qazaq.qarapkor.data.network.model.SearchResponseModel
import kz.qazaq.qarapkor.data.preferences.PreferenceProvider
import kz.qazaq.qarapkor.databinding.FragmentSearchBinding
import kz.qazaq.qarapkor.domain.Utils.CustomDividerItemDecoration
import kz.qazaq.qarapkor.domain.adapter.CategoryMovieAdapter
import kz.qazaq.qarapkor.domain.adapter.SearchMovieAdapter
import kz.qazaq.qarapkor.presentation.ui.VM
import kz.qazaq.qarapkor.presentation.ui.provideNavigationHost

class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding? = null
    private val vm: VM by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {

            setNavigationVisibility(true)
            setNavigationToolBar(true, false)
            additionalToolBarConfig(
                false,
                btnExitVisible = false,
                titleVisible = true,
                title = "Iздеу"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {

            setNavigationVisibility(true)
            setNavigationToolBar(true, false)
            additionalToolBarConfig(
                false,
                btnExitVisible = false,
                titleVisible = true,
                title = "Тізім"
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnReaction()
        binding?.sanattarConstraintLayout?.visibility = View.VISIBLE
        binding?.searchResultConstraintLayout?.visibility = View.GONE
        binding?.btnRefreshEditText?.visibility = View.GONE


        binding?.editTextSearchMovie?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Ничего не делать перед изменением текста
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputText = s.toString()
                requestToRetrofit(inputText)
                if (s.isNullOrEmpty()){
                    binding?.btnRefreshEditText?.visibility = View.GONE
                }else{
                    binding?.btnRefreshEditText?.visibility = View.VISIBLE
                    binding?.btnRefreshEditText?.setOnClickListener {
                        binding?.editTextSearchMovie?.text?.clear()
                        binding?.sanattarConstraintLayout?.visibility = View.VISIBLE
                        binding?.searchResultConstraintLayout?.visibility = View.GONE
                    }

                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Ничего не делать после изменения текста
            }
        })


    }

    private fun requestToRetrofit(search: String) {
        lifecycleScope.launch {
            movieInCategory(search)
        }
    }

    private suspend fun movieInCategory(search: String) {
        val token = PreferenceProvider(requireContext()).getToken()!!
        val response = ServiceBuilder.buildService(ApiInterface::class.java)

        val data = runCatching {

            val credentials = "{}"
            val details = "{}"
            val principal = "{}"


            response.getSearchMovie("Bearer $token", credentials, details, principal, search)
        }.onSuccess {
            binding?.sanattarConstraintLayout?.visibility = View.GONE
            binding?.searchResultConstraintLayout?.visibility = View.VISIBLE
        }.onFailure { error ->
//            Toast.makeText(requireContext(), "Qate", Toast.LENGTH_SHORT).show()
        }.getOrNull()

        if (data != null) {
            val adapter = SearchMovieAdapter(
                data, requireContext().applicationContext,
                object : SearchMovieAdapter.SearchClickMovie {
                    override fun onItemClick(item: SearchResponseModel.SearchResponseModelItem) {
                        vm.movieIdForAbout.value = MovieIdModel(item.id)
                        findNavController().navigate(R.id.aboutMovieFragment)
                    }

                })

            binding?.rcViewSearchFragment?.adapter = adapter
            binding?.rcViewSearchFragment?.addItemDecoration(
                CustomDividerItemDecoration(
                    requireContext().getDrawable(R.drawable.divider_1dp_grey)!!
                )
            )
        }


    }

    private fun btnReaction() {
        binding?.apply {
            btnOzinshe1.setOnClickListener {
                vm.categoryMovie.value = CategoryId(1)
                vm.namePageGenre.value = NamePage("ÖZINŞE-де танымал")
                navigation()
            }
            btnTelehikaya5.setOnClickListener {
                vm.categoryMovie.value = CategoryId(5)
                vm.namePageGenre.value = NamePage("Телехикаялар")
                navigation()
            }
            btnTolyqMultFilm8.setOnClickListener {
                vm.categoryMovie.value = CategoryId(8)
                vm.namePageGenre.value = NamePage("Толықметрлі мультфильмдер")
                navigation()
            }
            btnMult9.setOnClickListener {
                vm.categoryMovie.value = CategoryId(9)
                vm.namePageGenre.value = NamePage("Мультсериалдар")
                navigation()
            }
            btnSitkomdar31.setOnClickListener {
                vm.categoryMovie.value = CategoryId(31)
                vm.namePageGenre.value = NamePage("Ситкомдар")
                navigation()
            }
        }
    }

    private fun navigation() {
        findNavController().navigate(R.id.categoriesFragment)
    }


}