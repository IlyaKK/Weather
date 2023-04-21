package com.elijah.weather.presentation.weather_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elijah.weather.di.CityQualifier
import com.elijah.weather.domain.GetWeatherOfCityUseCase
import com.elijah.weather.domain.entity.City
import com.elijah.weather.domain.entity.WeatherOfCity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherInfoViewModel @Inject constructor(
    private val getWeatherOfCityUseCase: GetWeatherOfCityUseCase,
    @CityQualifier private val city: City
) : ViewModel() {
    init {
        getWeather()
    }

    private val _viewStateWeatherOfCity: MutableStateFlow<WeatherOfCityViewState> =
        MutableStateFlow(
            WeatherOfCityViewState.LoadWeather
        )
    val viewStateWeatherOfCity: StateFlow<WeatherOfCityViewState> =
        _viewStateWeatherOfCity.asStateFlow()

    private fun getWeather() {
        viewModelScope.launch {
            getWeatherOfCityUseCase.invoke(city)
                .onStart { _viewStateWeatherOfCity.emit(WeatherOfCityViewState.LoadWeather) }
                .collect {
                    _viewStateWeatherOfCity.emit(WeatherOfCityViewState.WeatherOfCityContent(it))
                }
        }
    }

    sealed class WeatherOfCityViewState {
        data class WeatherOfCityContent(val weatherOfCity: WeatherOfCity) : WeatherOfCityViewState()
        object LoadWeather : WeatherOfCityViewState()
    }
}
