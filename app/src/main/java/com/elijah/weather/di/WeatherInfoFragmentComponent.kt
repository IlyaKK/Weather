package com.elijah.weather.di

import com.elijah.weather.domain.entity.City
import com.elijah.weather.presentation.weather_info.WeatherInfoFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ViewModuleWeatherInfo::class])
interface WeatherInfoFragmentComponent {

    fun inject(weatherInfoFragment: WeatherInfoFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance @CityQualifier city: City,
        ): WeatherInfoFragmentComponent
    }
}