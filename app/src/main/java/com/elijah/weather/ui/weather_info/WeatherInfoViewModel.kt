package com.elijah.weather.ui.weather_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elijah.weather.domain.WeatherRepository
import com.elijah.weather.domain.entity.DailyWeather
import com.elijah.weather.domain.entity.HourlyWeather
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherInfoViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {
    private val scope = viewModelScope
    private val handlerHourlyWeather = CoroutineExceptionHandler { _, _ ->
        _viewStateHourlyWeather.value = HourlyWeatherListViewState.FailedToLoad
    }

    private val handlerDailyWeather = CoroutineExceptionHandler { _, _ ->
        _viewStateDailyWeather.value = DailyWeatherListViewState.FailedToLoad
    }

    private val _viewStateHourlyWeather: MutableStateFlow<HourlyWeatherListViewState> =
        MutableStateFlow(
            HourlyWeatherListViewState.HourlyWeatherLoaded(
                emptyList()
            )
        )
    val viewStateHourlyWeather: StateFlow<HourlyWeatherListViewState> = _viewStateHourlyWeather

    private val _viewStateDailyWeather: MutableStateFlow<DailyWeatherListViewState> =
        MutableStateFlow(
            DailyWeatherListViewState.DailyWeatherLoaded(
                emptyList()
            )
        )
    val viewStateDailyWeather: StateFlow<DailyWeatherListViewState> = _viewStateDailyWeather

    fun loadHourlyWeather(latitude: Double, longitude: Double, units: String, language: String) {
        scope.launch(handlerHourlyWeather) {
            launch(Dispatchers.IO) {
                weatherRepository.getHourlyWeather(latitude, longitude, units, language)
                    .collect {
                        _viewStateHourlyWeather.value =
                            HourlyWeatherListViewState.HourlyWeatherLoaded(it)
                    }
            }
        }
    }

    fun loadDailyWeather(latitude: Double, longitude: Double, units: String, language: String) {
        scope.launch(handlerDailyWeather) {
            launch(Dispatchers.IO) {
                weatherRepository.getDailyWeather(latitude, longitude, units, language)
                    .collect {
                        _viewStateDailyWeather.value =
                            DailyWeatherListViewState.DailyWeatherLoaded(it)
                    }
            }
        }
    }
}

sealed class HourlyWeatherListViewState {

    data class HourlyWeatherLoaded(val hourlyWeather: List<HourlyWeather>) :
        HourlyWeatherListViewState()

    object FailedToLoad : HourlyWeatherListViewState()
}

sealed class DailyWeatherListViewState {

    data class DailyWeatherLoaded(val dailyWeather: List<DailyWeather>) :
        DailyWeatherListViewState()

    object FailedToLoad : DailyWeatherListViewState()
}
