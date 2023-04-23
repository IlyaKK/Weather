package com.elijah.weather.presentation.slider_weather_of_cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elijah.weather.domain.GetCitiesUseCase
import com.elijah.weather.domain.StartUpdateWeatherDataUseCase
import com.elijah.weather.domain.StartCurrentCityUpdateUseCase
import com.elijah.weather.domain.entity.City
import com.elijah.weather.presentation.mapper.PointsMapper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherCityViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val startCurrentCityUpdateUseCase: StartCurrentCityUpdateUseCase,
    startUpdateWeatherDataUseCase: StartUpdateWeatherDataUseCase,
    private val pointsMapper: PointsMapper
) : ViewModel() {
    init {
        initialiseCurrentCityUpdate()
        startUpdateWeatherDataUseCase.invoke()
    }

    private val _viewStateCityInfo: MutableStateFlow<CityInfoState> =
        MutableStateFlow(
            CityInfoState.CityLoad
        )
    val viewStateCityInfo: StateFlow<CityInfoState> = _viewStateCityInfo.asStateFlow()

    private val _viewStatePointsCity: MutableStateFlow<PointState> =
        MutableStateFlow(
            PointState.PointContent(emptyList())
        )
    val viewStatePointsCity: StateFlow<PointState> = _viewStatePointsCity.asStateFlow()

    private fun initialiseCurrentCityUpdate() {
        viewModelScope.launch {
            val startSuccess = startCurrentCityUpdateUseCase.invoke()
            if (startSuccess) {
                initialiseGetCityList()
            } else {
                _viewStateCityInfo.value = CityInfoState.NotAccessToLocation
            }
        }
    }

    private suspend fun initialiseGetCityList() {
        getCitiesUseCase.invoke()
            .onEach { cityList ->
                updatePointCities(cityList)
            }
            .map {
                CityInfoState.CityListContent(it) as CityInfoState
            }
            .catch { emit(CityInfoState.CityListError) }
            .collectLatest {
                _viewStateCityInfo.emit(it)
            }

    }

    private suspend fun updatePointCities(cityList: List<City>) {
        val pointState = _viewStatePointsCity.value as? PointState.PointContent
        pointState?.let { pointCont ->
            val oldList = pointCont.points
            val newList = cityList.map { city ->
                pointsMapper.mapCityToPointOfItem(city)
            }.toMutableList()
            for (oldPointInd in oldList.indices) {
                if (oldList[oldPointInd].select && oldPointInd < newList.size) {
                    newList[oldPointInd] = newList[oldPointInd].copy(select = true)
                }
            }
            _viewStatePointsCity.emit(PointState.PointContent(newList.toList()))
        }
    }

    fun selectPointCurrentCity(position: Int) {
        val pointState = _viewStatePointsCity.value as? PointState.PointContent
        pointState?.let {
            val oldList = it.points
            val newList = mutableListOf<PointOfCityItem>()
            for (itemInd in oldList.indices) {
                if (itemInd == position) {
                    newList.add(oldList[itemInd].copy(select = true))
                } else {
                    newList.add(oldList[itemInd].copy(select = false))
                }
            }
            _viewStatePointsCity.value = PointState.PointContent(newList)
        }
    }

    sealed class CityInfoState {
        data class CityListContent(val cities: List<City>) : CityInfoState()
        object CityLoad : CityInfoState()
        object CityListError : CityInfoState()
        object NotAccessToLocation : CityInfoState()

    }

    sealed class PointState {
        data class PointContent(val points: List<PointOfCityItem>) : PointState()
    }
}

