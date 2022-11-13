package com.elijah.weather.ui.weather_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elijah.weather.R
import com.elijah.weather.app
import com.elijah.weather.databinding.FragmentWeatherInfoBinding
import com.elijah.weather.domain.entity.DailyWeather
import com.elijah.weather.domain.entity.HourlyWeather
import com.elijah.weather.ui.weather_info.hourly_weather_recycler_view.HourlyDayWeatherAdapterResView
import com.elijah.weather.ui.weather_info.week_days_recycler_view.WeekDaysWeatherAdapterResView
import kotlinx.coroutines.launch

const val KEY_LATITUDE_LOCATION = "KEY_LATITUDE_LOCATION"
const val KEY_LONGITUDE_LOCATION = "KEY_LONGITUDE_LOCATION"
const val KEY_NAME_LOCATION = "KEY_NAME_LOCATION"

class WeatherInfoFragment : Fragment() {
    private lateinit var binding: FragmentWeatherInfoBinding
    private lateinit var linearLayoutManagerHourlyDayWeather: LinearLayoutManager
    private lateinit var linearLayoutManagerWeekDaysWeather: LinearLayoutManager
    private lateinit var hourlyDayWeatherAdapterResView: HourlyDayWeatherAdapterResView
    private lateinit var weekDaysWeatherAdapterResView: WeekDaysWeatherAdapterResView

    init {
        initialiseHourlyWeatherShow()
        initialiseDailyWeatherShow()
    }

    private val weatherInfoViewModel: WeatherInfoViewModel by lazy {
        ViewModelProvider(
            this,
            app.viewModelFactory
        )[WeatherInfoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(KEY_NAME_LOCATION)?.let { showCityName(it) }
        initialiseHourlyDayWeatherRecyclerView()
        initialiseDaysWeekWeatherRecyclerView()
        weatherInfoViewModel.loadHourlyWeather(
            arguments?.getDouble(KEY_LATITUDE_LOCATION) ?: 0.0,
            arguments?.getDouble(KEY_LONGITUDE_LOCATION) ?: 0.0, "metric", "ru"
        )
        weatherInfoViewModel.loadDailyWeather(
            arguments?.getDouble(KEY_LATITUDE_LOCATION) ?: 0.0,
            arguments?.getDouble(KEY_LONGITUDE_LOCATION) ?: 0.0, "metric", "ru"
        )
    }

    private fun initialiseHourlyDayWeatherRecyclerView() {
        linearLayoutManagerHourlyDayWeather =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        binding.hourlyDayWeatherRv.layoutManager = linearLayoutManagerHourlyDayWeather
        hourlyDayWeatherAdapterResView = HourlyDayWeatherAdapterResView()
        binding.hourlyDayWeatherRv.adapter = hourlyDayWeatherAdapterResView

    }

    private fun initialiseDaysWeekWeatherRecyclerView() {
        linearLayoutManagerWeekDaysWeather =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        binding.sevenDayWeatherRv.layoutManager = linearLayoutManagerWeekDaysWeather
        weekDaysWeatherAdapterResView = WeekDaysWeatherAdapterResView()
        binding.sevenDayWeatherRv.adapter = weekDaysWeatherAdapterResView
    }

    private fun initialiseHourlyWeatherShow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherInfoViewModel.viewStateHourlyWeather.collect {
                    when (it) {
                        is HourlyWeatherListViewState.HourlyWeatherLoaded -> showHourlyWeather(it.hourlyWeather)
                        is HourlyWeatherListViewState.FailedToLoad -> showErrorMessage()
                    }
                }
            }
        }
    }

    private fun showHourlyWeather(hourlyWeather: List<HourlyWeather>) {
        hourlyDayWeatherAdapterResView.submitList(hourlyWeather)
        if (hourlyWeather.isNotEmpty()) {
            showNowTemperatureWithDescription(hourlyWeather[0])
        }
    }

    private fun showNowTemperatureWithDescription(hourlyWeather: HourlyWeather) {
        binding.variableTemperatureNowTv.text = hourlyWeather.temperature
        binding.titleWeatherTv.text = hourlyWeather.weather.description
    }


    private fun initialiseDailyWeatherShow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherInfoViewModel.viewStateDailyWeather.collect {
                    when (it) {
                        is DailyWeatherListViewState.DailyWeatherLoaded -> showDailyWeather(it.dailyWeather)
                        is DailyWeatherListViewState.FailedToLoad -> showErrorMessage()
                    }
                }
            }
        }
    }

    private fun showDailyWeather(dailyWeather: List<DailyWeather>) {
        weekDaysWeatherAdapterResView.submitList(dailyWeather)
        if (dailyWeather.isNotEmpty()) {
            showMaxMinTemperature(dailyWeather[0])
        }
    }

    private fun showCityName(nameLocation: String) {
        binding.titleCityTv.text = nameLocation
    }

    private fun showMaxMinTemperature(dailyWeather: DailyWeather) {
        val minTemperature =
            "${binding.root.context.getString(R.string.Minimal)}${dailyWeather.temperatureMin}"
        val maxTemperature =
            "${binding.root.context.getString(R.string.Maximal)}${dailyWeather.temperatureMax}"
        binding.variableNightTemperatureTv.text = minTemperature
        binding.variableDayTemperatureTv.text = maxTemperature
    }


    private fun showErrorMessage() {
        Toast.makeText(requireContext(), "Не удалось загрузить погоду", Toast.LENGTH_LONG).show()
    }

    companion object {
        fun getInstance(
            latitude: Double,
            longitude: Double,
            nameLocation: String
        ): WeatherInfoFragment =
            WeatherInfoFragment().apply {
                arguments = Bundle().apply {
                    putDouble(KEY_LATITUDE_LOCATION, latitude)
                    putDouble(KEY_LONGITUDE_LOCATION, longitude)
                    putString(KEY_NAME_LOCATION, nameLocation)
                }
            }
    }
}
