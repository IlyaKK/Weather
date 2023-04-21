package com.elijah.weather.presentation.slider_weather_of_cities

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.elijah.weather.R
import com.elijah.weather.databinding.FragmentSliderWeatherCitiesBinding
import com.elijah.weather.presentation.*
import com.elijah.weather.util.hasPermission
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

class SliderWeatherCitiesFragment : Fragment() {
    private var _binding: FragmentSliderWeatherCitiesBinding? = null
    private val binding: FragmentSliderWeatherCitiesBinding
        get() = _binding ?: throw RuntimeException("FragmentSliderWeatherCitiesBinding is null")

    private lateinit var pagerAdapter: WeatherOfCityPagerAdapter
    private lateinit var pointerAdapter: PointOfCityAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val weatherCityViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[WeatherCityViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        app.component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSliderWeatherCitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val permissionGrantedAccessFineLocation =
            requireContext().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val permissionGrantedAccessBackground =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requireContext().hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                true
            }
        if (permissionGrantedAccessFineLocation && permissionGrantedAccessBackground) {
            initialiseSliderWeathersFragment()
        } else {
            val permissions = mutableListOf<String>()
            if (!permissionGrantedAccessFineLocation) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (!permissionGrantedAccessBackground) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
            }
            findNavController().navigate(
                SliderWeatherCitiesFragmentDirections.actionPermissionsRequestFragment(
                    permissions.toTypedArray()
                )
            )
        }
    }

    private fun initialiseSliderWeathersFragment() {
        initialisePointsOfCitiesFragment()
        initialiseEditLocations()
        initialiseCityPager()
        observableCityInfoStates()
        observablePointsState()
    }

    private fun initialisePointsOfCitiesFragment() {
        pointerAdapter = PointOfCityAdapter()
        binding.pointsListContainerRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.pointsListContainerRv.adapter = pointerAdapter
    }

    private fun initialiseEditLocations() {
        binding.editListLocationIv.setOnClickListener {
            findNavController().navigate(R.id.action_sliderWeatherCitiesFragment_to_searchLocationFragment)
        }
    }

    private fun initialiseCityPager() {
        pagerAdapter = WeatherOfCityPagerAdapter(childFragmentManager, lifecycle)
        binding.pagerFragmentsVp.adapter = pagerAdapter
        binding.pagerFragmentsVp.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    weatherCityViewModel.selectPointCurrentCity(position)
                }
            }
        )
    }

    private fun observableCityInfoStates() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherCityViewModel.viewStateCityInfo.collect {
                    when (it) {
                        is WeatherCityViewModel.CityInfoState.CityListContent -> {
                            binding.locationPb.isVisible = false
                            pagerAdapter.cityList = it.cities
                        }
                        is WeatherCityViewModel.CityInfoState.CityLoad -> {
                            binding.locationPb.isVisible = true
                        }
                        is WeatherCityViewModel.CityInfoState.CityListError -> {
                            binding.locationPb.isVisible = false
                            Toast.makeText(requireContext(), "Error load city", Toast.LENGTH_LONG)
                                .show()
                        }
                        is WeatherCityViewModel.CityInfoState.NotAccessToLocation -> {
                            binding.locationPb.isVisible = false
                            Snackbar.make(
                                binding.mainSliderContCl,
                                "No access location. Check Permission",
                                Snackbar.LENGTH_INDEFINITE
                            ).setAction(R.string.ok) {
                                findNavController().navigate(
                                    SliderWeatherCitiesFragmentDirections.actionPermissionsRequestFragment(
                                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                                    )
                                )
                            }.show()
                        }
                    }
                }
            }
        }
    }

    private fun observablePointsState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                weatherCityViewModel.viewStatePointsCity
                    .filter {
                        it != WeatherCityViewModel.PointState.PointContent(emptyList())
                    }
                    .collectLatest {
                        when (it) {
                            is WeatherCityViewModel.PointState.PointContent -> {
                                pointerAdapter.submitList(it.points)
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