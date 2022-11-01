package com.elijah.weather.ui.control_locations_weather

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.elijah.weather.R
import com.elijah.weather.app
import com.elijah.weather.databinding.FragmentControlLocationsWeatherBinding
import com.elijah.weather.domain.entity.Location
import com.elijah.weather.ui.LocationViewModel
import com.elijah.weather.ui.LocationViewState
import com.elijah.weather.ui.control_locations_weather.pointers_recycler_view.PointersAdapter
import com.elijah.weather.ui.weather_info.WeatherInfoFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.*

class ControlLocationsWeatherFragment : Fragment() {
    private lateinit var binding: FragmentControlLocationsWeatherBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var poolFragmentsWeather: MutableList<Fragment> = mutableListOf()
    private lateinit var pagerAdapter: ScreenSlidePagerAdapter
    private var listPointers: MutableList<PointItem> = mutableListOf(PointItem(0, true))
    private lateinit var pointerAdapter: PointersAdapter

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
        binding = FragmentControlLocationsWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialisePagerView()
        initialiseRequestLocationPermission()
        initialiseEditLocationPlaces()
        initialiseLocationsStates()
        initialisePointsExistsWeatherFragments()
    }

    private fun initialisePagerView() {
        pagerAdapter = ScreenSlidePagerAdapter()
        binding.pagerFragmentsVp.adapter = pagerAdapter
        binding.pagerFragmentsVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val newList: List<PointItem> = listPointers.map {
                    PointItem(
                        id = it.id,
                        select = it.id == position
                    )
                }
                pointerAdapter.submitList(newList)
            }
        })
    }

    private fun initialiseRequestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) || (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) -> {
                    requestCurrentCoordinates()
                }
                else -> {

                }
            }
        }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun initialiseEditLocationPlaces() {
        binding.editListLocationIv.setOnClickListener {
            findNavController().navigate(R.id.next_action)
        }
    }

    private fun initialiseLocationsStates() {
        locationViewModel.getAccessLocations()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationViewModel.viewStateLocation.collect {
                    when (it) {
                        is LocationViewState.LocationListLoaded -> createSliderWeather(it.locations)
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

    private fun createSliderWeather(locations: List<Location>) {
        for (i in locations.indices) {
            if (locations[i].current) {
                Collections.swap(locations, 0, i)
            }
        }
        poolFragmentsWeather.clear()
        listPointers.clear()
        var i = 0
        locations.forEach {
            listPointers.add(PointItem(i, i == 0))
            poolFragmentsWeather.add(
                WeatherInfoFragment.getInstance(
                    it.latitude,
                    it.longitude,
                    it.cityName
                )
            )
            i++
        }
        pagerAdapter.notifyDataSetChanged()
    }

    private fun initialisePointsExistsWeatherFragments() {
        pointerAdapter = PointersAdapter()
        binding.pointsListContainerRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.pointsListContainerRv.adapter = pointerAdapter
        pointerAdapter.submitList(listPointers)
    }

    @SuppressLint("MissingPermission")
    private fun requestCurrentCoordinates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener {
            locationViewModel.addCurrentCoordinates(it.latitude, it.longitude)
        }
    }

    private inner class ScreenSlidePagerAdapter :
        FragmentStateAdapter(childFragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return poolFragmentsWeather.size
        }

        override fun createFragment(position: Int): Fragment {
            return poolFragmentsWeather[position]
        }
    }
}