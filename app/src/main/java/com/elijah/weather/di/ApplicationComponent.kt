package com.elijah.weather.di

import android.app.Application
import com.elijah.weather.presentation.App
import com.elijah.weather.presentation.LocationUpdatesBroadcastReceiver
import com.elijah.weather.presentation.edit_location.EditLocationFragment
import com.elijah.weather.presentation.slider_weather_of_cities.SliderWeatherCitiesFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        WorkerModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(application: App)

    fun inject(sliderWeatherCitiesFragment: SliderWeatherCitiesFragment)

    fun weatherInfoComponentFactory(): WeatherInfoFragmentComponent.Factory

    fun inject(locationUpdatesBroadcastReceiver: LocationUpdatesBroadcastReceiver)

    fun inject(editLocationFragment: EditLocationFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}