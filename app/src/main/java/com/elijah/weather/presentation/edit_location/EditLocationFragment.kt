package com.elijah.weather.presentation.edit_location

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.elijah.weather.databinding.FragmentEditLocationBinding
import com.elijah.weather.presentation.ViewModelFactory
import com.elijah.weather.presentation.app
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditLocationFragment : Fragment() {

    private var _binding: FragmentEditLocationBinding? = null
    private val binding: FragmentEditLocationBinding
        get() = _binding ?: throw RuntimeException("FragmentEditLocationBinding == null")

    private lateinit var accessCityAdapterResView: AccessCityAdapterResView
    private lateinit var searchedCityAdapterResView: SearchedCityAdapterResView

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val editLocationViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[EditLocationViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        app.component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseBackNavigation()
        initialiseAccessCityRecyclerView()
        initialiseSearchedCityRecyclerView()
        initialiseTextInputSearchedCity()
        observeAccessLocations()
        observedSearchedCity()
    }

    private fun initialiseBackNavigation() {
        binding.topEditLocationBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initialiseAccessCityRecyclerView() {
        accessCityAdapterResView = AccessCityAdapterResView()
        accessCityAdapterResView.listenerOnClickDeleteCityButton = {
            editLocationViewModel.deleteCity(it)
        }
        binding.accessCityResView.adapter = accessCityAdapterResView
    }

    private fun initialiseSearchedCityRecyclerView() {
        searchedCityAdapterResView = SearchedCityAdapterResView()
        searchedCityAdapterResView.onAddCityButtonClickListener = {
            editLocationViewModel.addCity(it)
        }
        binding.searchedCityResView.adapter = searchedCityAdapterResView
    }

    private fun initialiseTextInputSearchedCity() {
        binding.searchCityTextInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            editLocationViewModel.searchCity(text)
        }
        binding.searchCityTextInputLayout.setEndIconOnClickListener {
            editLocationViewModel.clickCancelSearchButton()
        }
    }

    private fun observeAccessLocations() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                editLocationViewModel.viewStateCityInfo
                    .collect {
                        when (it) {
                            is EditLocationViewModel.CityInfoState.CityListContent -> {
                                binding.locationPb.isVisible = false
                                accessCityAdapterResView.submitList(it.cities)
                            }
                            is EditLocationViewModel.CityInfoState.CityLoad -> {
                                binding.locationPb.isVisible = true
                            }
                            is EditLocationViewModel.CityInfoState.CityListError -> {
                                binding.locationPb.isVisible = false
                                Toast.makeText(
                                    requireContext(),
                                    "Error load city",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                    }
            }
        }
    }


    private fun observedSearchedCity() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                editLocationViewModel.searchedCityState
                    .collect {
                        when (it) {
                            is EditLocationViewModel.SearchCityState.SearchedListContent -> {
                                binding.searchedPanelCardView.isVisible = true
                                binding.searchPb.isVisible = false
                                binding.searchCityTextInputLayout.helperText = null
                                binding.searchCityTextInputLayout.error = null
                                searchedCityAdapterResView.submitList(it.cities)
                            }
                            is EditLocationViewModel.SearchCityState.SearchedListEmpty -> {
                                searchedCityAdapterResView.submitList(emptyList())
                                binding.searchPb.isVisible = false
                                binding.searchCityTextInputLayout.error = null
                                binding.searchCityTextInputLayout.helperText =
                                    "Нет города с таким названием"
                            }
                            is EditLocationViewModel.SearchCityState.SearchedListLoad -> {
                                binding.searchPb.isVisible = true
                                binding.searchedPanelCardView.isVisible = true
                                binding.searchCityTextInputLayout.helperText = null
                                binding.searchCityTextInputLayout.error = null
                            }
                            is EditLocationViewModel.SearchCityState.SearchListNotLoad -> {
                                searchedCityAdapterResView.submitList(emptyList())
                                binding.textInputSearchCityTv.text = null
                                binding.searchedPanelCardView.isVisible = false
                                binding.searchCityTextInputLayout.helperText = null
                                binding.searchCityTextInputLayout.error = null
                            }
                            is EditLocationViewModel.SearchCityState.SearchListError -> {
                                searchedCityAdapterResView.submitList(emptyList())
                                binding.searchedPanelCardView.isVisible = false
                                binding.searchCityTextInputLayout.helperText = null
                                binding.searchCityTextInputLayout.error = "Ошибка поиска города"
                            }
                        }
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
