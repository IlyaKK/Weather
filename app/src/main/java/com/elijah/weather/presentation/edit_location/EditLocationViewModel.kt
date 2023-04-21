package com.elijah.weather.presentation.edit_location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elijah.weather.domain.AddCityUseCase
import com.elijah.weather.domain.DeleteCityUseCase
import com.elijah.weather.domain.GetCitiesUseCase
import com.elijah.weather.domain.SearchCitiesByNameUseCase
import com.elijah.weather.domain.entity.City
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditLocationViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val searchCitiesByNameUseCase: SearchCitiesByNameUseCase,
    private val deleteCityUseCase: DeleteCityUseCase,
    private val addCityUseCase: AddCityUseCase,
) : ViewModel() {

    init {
        getCityInfo()
    }

    private val _viewStateCityInfo: MutableStateFlow<CityInfoState> =
        MutableStateFlow(
            CityInfoState.CityLoad
        )
    val viewStateCityInfo: StateFlow<CityInfoState> = _viewStateCityInfo.asStateFlow()

    private val _searchedCityState: MutableStateFlow<SearchCityState> =
        MutableStateFlow(
            SearchCityState.SearchListNotLoad
        )
    val searchedCityState: StateFlow<SearchCityState> = _searchedCityState.asStateFlow()

    private fun getCityInfo() {
        viewModelScope.launch {
            getCitiesUseCase.invoke()
                .filter { it.isNotEmpty() }
                .map {
                    CityInfoState.CityListContent(it) as CityInfoState
                }
                .catch { emit(CityInfoState.CityListError) }
                .onStart { emit(CityInfoState.CityLoad) }
                .collectLatest {
                    _viewStateCityInfo.emit(it)
                }
        }
    }

    fun searchCity(nameCity: CharSequence?) {
        if (!nameCity.isNullOrBlank()) {
            viewModelScope.launch {
                searchCitiesByNameUseCase(nameCity.toString())
                    .map {
                        if (it.isEmpty()) {
                            SearchCityState.SearchedListEmpty as SearchCityState
                        } else {
                            SearchCityState.SearchedListContent(it) as SearchCityState
                        }
                    }
                    .onStart {
                        emit(SearchCityState.SearchedListLoad)
                    }
                    .catch {
                        emit(SearchCityState.SearchListError)
                    }
                    .collectLatest {
                        _searchedCityState.emit(it)
                    }
            }
        } else {
            viewModelScope.launch {
                _searchedCityState.emit(SearchCityState.SearchListNotLoad)
            }
        }
    }

    fun clickCancelSearchButton() {
        viewModelScope.launch {
            _searchedCityState.emit(SearchCityState.SearchListNotLoad)
        }
    }

    fun addCity(it: City) {
        viewModelScope.launch {
            addCityUseCase.invoke(it)
            _searchedCityState.emit(SearchCityState.SearchListNotLoad)
        }
    }

    fun deleteCity(city: City) {
        viewModelScope.launch {
            deleteCityUseCase.invoke(city)
        }
    }

    sealed class SearchCityState {
        data class SearchedListContent(val cities: List<City>) : SearchCityState()
        object SearchedListEmpty : SearchCityState()
        object SearchedListLoad : SearchCityState()
        object SearchListNotLoad : SearchCityState()
        object SearchListError : SearchCityState()
    }

    sealed class CityInfoState {
        data class CityListContent(val cities: List<City>) : CityInfoState()
        object CityLoad : CityInfoState()
        object CityListError : CityInfoState()
    }
}