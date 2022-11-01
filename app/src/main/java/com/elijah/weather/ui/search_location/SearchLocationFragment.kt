package com.elijah.weather.ui.search_location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elijah.weather.app
import com.elijah.weather.databinding.FragmentSearchLocationBinding
import com.elijah.weather.domain.entity.Location
import com.elijah.weather.ui.LocationViewModel
import com.elijah.weather.ui.LocationViewState
import com.elijah.weather.ui.SearchedLocationViewState
import com.elijah.weather.ui.search_location.access_city_recycler_view.AccessCityAdapterResView
import com.elijah.weather.ui.search_location.searched_city_recycler_view.SearchedCityAdapterResView
import kotlinx.coroutines.launch

class SearchLocationFragment : Fragment() {

    private lateinit var binding: FragmentSearchLocationBinding
    private lateinit var linearLayoutManagerAccessCity: LinearLayoutManager
    private lateinit var accessCityAdapterResView: AccessCityAdapterResView
    private lateinit var linearLayoutManagerSearchedCity: LinearLayoutManager
    private lateinit var searchedCityAdapterResView: SearchedCityAdapterResView

    init {
        initialiseAccessCity()
        initialiseSearchedCity()
    }

    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            app.viewModelFactory
        )[LocationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseBackNavigation()
        initialiseAccessCityRecyclerView()
        initialiseTextInputSearchedCity()
        initialiseSearchedCityRecyclerView()
    }

    private fun initialiseAccessCity() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationViewModel.viewStateLocation.collect {
                    when (it) {
                        is LocationViewState.LocationListLoaded -> createListAccessCity(it.locations)
                        is LocationViewState.FailedGetListLocation -> Toast.makeText(
                            requireContext(),
                            "Не удалось получить список городов",
                            Toast.LENGTH_LONG
                        ).show()
                        is LocationViewState.FailedAddLocation -> Toast.makeText(
                            requireContext(),
                            "Не удалось добавить город",
                            Toast.LENGTH_LONG
                        ).show()
                        is LocationViewState.FailedDeleteCity -> Toast.makeText(
                            requireContext(),
                            "Не удалось удалить город",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun initialiseSearchedCity() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationViewModel.viewStateSearched.collect {
                    when (it) {
                        is SearchedLocationViewState.SearchedListLoaded -> {
                            showSearchedListCity(it.locations)
                        }
                        is SearchedLocationViewState.NotSearched -> {
                            binding.searchCityTextInputLayout.error = null
                            hideSearchedPanel()
                        }
                        is SearchedLocationViewState.FailedSearchedLocations -> {
                            showFailedSearched()
                        }
                    }
                }
            }
        }
    }

    private fun initialiseAccessCityRecyclerView() {
        linearLayoutManagerAccessCity =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.accessCityResView.layoutManager = linearLayoutManagerAccessCity
        accessCityAdapterResView = AccessCityAdapterResView()
        accessCityAdapterResView.setOnDeleteCityClickButton(
            object : AccessCityAdapterResView.OnClickDeleteCityButtonListener {
                override fun deleteCity(location: Location) {
                    locationViewModel.deleteCity(location)
                }
            }
        )
        binding.accessCityResView.adapter = accessCityAdapterResView
    }

    private fun initialiseSearchedCityRecyclerView() {
        linearLayoutManagerSearchedCity =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.searchedCityResView.layoutManager = linearLayoutManagerSearchedCity
        searchedCityAdapterResView = SearchedCityAdapterResView()
        searchedCityAdapterResView.setOnAddNewCityClickButton(
            object : SearchedCityAdapterResView.OnAddCityButtonListener {
                override fun addNewLocation(location: Location) {
                    binding.searchCityTextInputLayout.editText?.text = null
                    binding.searchCityTextInputLayout.error = null
                    hideSearchedPanel()
                    locationViewModel.addNewLocation(location)
                }
            })
        binding.searchedCityResView.adapter = searchedCityAdapterResView
    }

    private fun initialiseTextInputSearchedCity() {
        binding.searchCityTextInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.isNotEmpty()) {
                    locationViewModel.searchedLocation(text)
                } else {
                    binding.searchCityTextInputLayout.error = null
                    hideSearchedPanel()
                }
            } else {
                hideSearchedPanel()
                binding.searchCityTextInputLayout.error = null
            }
        }
    }

    private fun createListAccessCity(locations: List<Location>) {
        accessCityAdapterResView.submitList(locations)
    }

    private fun showSearchedListCity(locations: List<Location>) {
        binding.searchCityTextInputLayout.error = null
        binding.searchedPanelCardView.visibility = View.VISIBLE
        searchedCityAdapterResView.submitList(locations)
    }

    private fun showFailedSearched() {
        binding.searchCityTextInputLayout.error = "Не удалось найти город"
        hideSearchedPanel()

    }

    private fun hideSearchedPanel() {
        binding.searchedPanelCardView.visibility = View.GONE
    }

    private fun initialiseBackNavigation() {
        binding.topEditLocationBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}