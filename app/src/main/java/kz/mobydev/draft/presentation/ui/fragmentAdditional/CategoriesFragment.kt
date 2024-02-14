package kz.mobydev.draft.presentation.ui.fragmentAdditional

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kz.mobydev.draft.R
import kz.mobydev.draft.data.network.api.ApiInterface
import kz.mobydev.draft.data.network.api.ServiceBuilder
import kz.mobydev.draft.data.network.model.CategoryMovieResponse
import kz.mobydev.draft.data.network.model.FavoriteModel
import kz.mobydev.draft.data.network.model.MovieIdModel
import kz.mobydev.draft.data.preferences.PreferenceProvider
import kz.mobydev.draft.databinding.FragmentCategoriesBinding
import kz.mobydev.draft.domain.Utils.CustomDividerItemDecoration
import kz.mobydev.draft.domain.adapter.CategoryMovieAdapter
import kz.mobydev.draft.domain.adapter.FavoriteMovieAdapter
import kz.mobydev.draft.presentation.ui.VM
import kz.mobydev.draft.presentation.ui.provideNavigationHost

class CategoriesFragment : Fragment() {
    private var binding: FragmentCategoriesBinding? = null
    private val vm: VM by activityViewModels()
    private var categoryId:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true,false)
            additionalToolBarConfig(true, btnExitVisible = false, titleVisible = true, title = "Бөлімдер")
            onClickListener(R.id.action_categoriesFragment_to_searchFragment)
        }
    }
    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {

            setNavigationVisibility(false)
            setNavigationToolBar(true,false)
            additionalToolBarConfig(true, btnExitVisible = false, titleVisible = true, title = "Бөлімдер")
            onClickListener(R.id.action_categoriesFragment_to_searchFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCategoryId()
        binding?.shimmerInMainFragment?.startShimmer()
        binding?.rcCategoryFragment?.visibility = View.GONE


    }
    private fun getCategoryId(){
        vm.categoryMovie.observe(viewLifecycleOwner) { item->
            lifecycleScope.launch{
                movieInCategory(item.categoryId)
            }
        }
    }

    private suspend fun movieInCategory(id:Int) {
        val token = PreferenceProvider(requireContext()).getToken()!!
        val response = ServiceBuilder.buildService(ApiInterface::class.java)






        val data = runCatching {
            val direction = "DESC"
            val page = 0
            val size = 20
            val sortField = "createdDate"
            response.getCategoryMovie("Bearer $token",id, direction, page, size, sortField)
            }.onFailure { error ->
            Toast.makeText(requireContext(), "Qate", Toast.LENGTH_SHORT).show()
            }.getOrNull()

        if (data != null ) {
            binding?.shimmerInMainFragment?.stopShimmer()
            binding?.rcCategoryFragment?.visibility = View.VISIBLE
            binding?.shimmerInMainFragment?.visibility = View.GONE
            val adapter = CategoryMovieAdapter(
                data, requireContext().applicationContext,
                object : CategoryMovieAdapter.CategoryClickMovie {
                    override fun onItemClick(item: CategoryMovieResponse.Content) {
                        vm.movieIdForAbout.value = MovieIdModel(item.id)
                        findNavController().navigate(R.id.aboutMovieFragment)
                    }


                })

            binding?.rcCategoryFragment?.adapter = adapter
            binding?.rcCategoryFragment?.addItemDecoration(
                CustomDividerItemDecoration(
                    requireContext().getDrawable(R.drawable.divider_1dp_grey)!!
                )
            )
        }



    }

}