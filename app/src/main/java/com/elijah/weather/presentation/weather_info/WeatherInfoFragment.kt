package com.elijah.weather.presentation.weather_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.elijah.weather.databinding.FragmentWeatherInfoBinding
import com.elijah.weather.di.CityQualifier
import com.elijah.weather.domain.entity.City
import com.elijah.weather.presentation.ViewModelFactory
import com.elijah.weather.presentation.app
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherInfoFragment : Fragment() {
    private var _binding: FragmentWeatherInfoBinding? = null
    private val binding: FragmentWeatherInfoBinding
        get() = _binding ?: throw RuntimeException("FragmentWeatherInfoBinding == null")
    private lateinit var hourlyDayWeatherAdapterResView: HourlyDayWeatherAdapterResView
    private lateinit var weekDaysWeatherAdapterResView: WeekDaysWeatherAdapterResView

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    @CityQualifier
    lateinit var city: City

    private val weatherInfoViewModel: WeatherInfoViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[WeatherInfoViewModel::class.java]
    }

    private val component by lazy {
        val cityArg: City = arguments?.getParcelable(KEY_CITY)
            ?: throw RuntimeException("WeatherInfoFragment: City == null")
        app.component.weatherInfoComponentFactory().create(cityArg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseHourlyDayWeatherRecyclerView()
        initialiseDaysWeekWeatherRecyclerView()
        observeWeatherOfCityState()
    }

    private fun initialiseHourlyDayWeatherRecyclerView() {
        hourlyDayWeatherAdapterResView = HourlyDayWeatherAdapterResView()
        with(binding) {
            hourlyDayWeatherRv.adapter = hourlyDayWeatherAdapterResView
            hourlyDayWeatherRv.addOnItemTouchListener(
                object : OnItemTouchListener {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        when (e.action) {
                            MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(
                                true
                            )
                        }
                        return false
                    }

                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
                }
            )
        }
    }

    private fun initialiseDaysWeekWeatherRecyclerView() {
        weekDaysWeatherAdapterResView = WeekDaysWeatherAdapterResView()
        binding.sevenDayWeatherRv.adapter = weekDaysWeatherAdapterResView
    }

    private fun observeWeatherOfCityState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherInfoViewModel.viewStateWeatherOfCity
                    .collect {
                        when (it) {
                            is WeatherInfoViewModel.WeatherOfCityViewState.WeatherOfCityContent -> {
                                binding.loadWeatherDataPb.isVisible = false
                                with(it.weatherOfCity) {
                                    with(binding) {
                                        titleCityTv.text = city.name
                                        variableTemperatureNowTv.text =
                                            hoursWeather[0].temperature
                                        titleWeatherTv.text =
                                            hoursWeather[0].weatherDescription
                                        variableDayTemperatureTv.text =
                                            daysWeather[0].temperatureMax
                                        variableNightTemperatureTv.text =
                                            daysWeather[0].temperatureMin
                                    }

                                    hourlyDayWeatherAdapterResView.submitList(hoursWeather)
                                    weekDaysWeatherAdapterResView.submitList(daysWeather)
                                }
                            }
                            is WeatherInfoViewModel.WeatherOfCityViewState.LoadWeather -> {
                                binding.loadWeatherDataPb.isVisible = true
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


    companion object {

        private const val KEY_CITY = "KEY_CITY"
        fun getInstance(
            city: City
        ): WeatherInfoFragment =
            WeatherInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_CITY, city)
                }
            }
    }
}
