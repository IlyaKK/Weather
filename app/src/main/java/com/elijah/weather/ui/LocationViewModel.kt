package com.elijah.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elijah.weather.domain.LocationRepository
import com.elijah.weather.domain.entity.Location
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel(private val locationRepository: LocationRepository) : ViewModel() {
    private val scope = viewModelScope
    private val handlerExceptionAddLocation = CoroutineExceptionHandler { _, _ ->
        _viewStateLocation.value = LocationViewState.FailedAddLocation
    }

    private val handlerExceptionGetListLocations = CoroutineExceptionHandler { _, _ ->
        _viewStateLocation.value = LocationViewState.FailedGetListLocation
    }

    private val handlerExceptionSearchedLocations = CoroutineExceptionHandler { _, _ ->
        _viewStateSearched.value = SearchedLocationViewState.FailedSearchedLocations
    }

    private val handlerExceptionDeleteCity = CoroutineExceptionHandler { _, _ ->
        _viewStateLocation.value = LocationViewState.FailedDeleteCity
    }

    private val _viewStateLocation: MutableStateFlow<LocationViewState> =
        MutableStateFlow(
            LocationViewState.LocationListLoaded(
                emptyList()
            )
        )
    val viewStateLocation: StateFlow<LocationViewState> = _viewStateLocation

    private val _viewStateSearched: MutableStateFlow<SearchedLocationViewState> =
        MutableStateFlow(
            SearchedLocationViewState.NotSearched
        )

    val viewStateSearched: StateFlow<SearchedLocationViewState> = _viewStateSearched

    fun addCurrentCoordinates(latitude: Double, longitude: Double) {
        scope.launch(handlerExceptionAddLocation) {
            launch(Dispatchers.IO) {
                locationRepository.addLocation(
                    Location(
                        latitude = latitude,
                        longitude = longitude,
                        added = false,
                        current = true
                    ),
                    gpsLocation = true
                )
            }
        }
    }

    fun addNewLocation(location: Location) {
        scope.launch(handlerExceptionAddLocation) {
            launch(Dispatchers.IO) {
                _viewStateSearched.value = SearchedLocationViewState.NotSearched
                locationRepository.addLocation(
                    Location(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        cityName = location.cityName,
                        added = false,
                        current = false,
                    ),
                    gpsLocation = false
                )
            }
        }
    }

    fun getAccessLocations() {
        scope.launch(handlerExceptionGetListLocations) {
            launch(Dispatchers.IO) {
                locationRepository.getListAddedLocations().collect {
                    _viewStateLocation.value = LocationViewState.LocationListLoaded(it)
                }
            }
        }
    }

    fun searchedLocation(text: CharSequence?) {
        scope.launch(handlerExceptionSearchedLocations) {
            launch(Dispatchers.IO) {
                locationRepository.getCoordinatesByNameCity(text.toString()).collect {
                    _viewStateSearched.value = SearchedLocationViewState.SearchedListLoaded(it)
                }
            }
        }
    }

    fun deleteCity(location: Location) {
        scope.launch (handlerExceptionDeleteCity){
            launch(Dispatchers.IO){
                locationRepository.deleteLocation(location)
            }
        }
    }
}

sealed class LocationViewState {
    data class LocationListLoaded(val locations: List<Location>) :
        LocationViewState()

    object FailedAddLocation : LocationViewState()
    object FailedGetListLocation : LocationViewState()
    object FailedDeleteCity : LocationViewState()
}

sealed class SearchedLocationViewState {
    data class SearchedListLoaded(val locations: List<Location>) : SearchedLocationViewState()
    object NotSearched : SearchedLocationViewState()
    object FailedSearchedLocations : SearchedLocationViewState()
}
