package com.elijah.weather.data.remote.retrofit

import com.elijah.weather.BuildConfig
import com.elijah.weather.data.remote.RemoteWeatherDataSource
import com.elijah.weather.domain.entity.DailyWeather
import com.elijah.weather.domain.entity.HourlyWeather
import com.elijah.weather.domain.entity.Weather
import java.util.*

class RetrofitWeatherDataSource(private val api: WeatherApiService) : RemoteWeatherDataSource {
    private val calendar: Calendar = Calendar.getInstance()
    override suspend fun getHourlyWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String
    ): List<HourlyWeather> {
        val weatherApiHourlyResponse = api.getHourlyWeather(
            latitude,
            longitude,
            exclude = "current,minutely,daily,alerts",
            units = units,
            language = language,
            apiKey = BuildConfig.WEATHER_ONE_CALL_API_KEY
        )
        val cityName = weatherApiHourlyResponse.timezone
        return weatherApiHourlyResponse.hourly.map { hourlyWeatherApi ->
            HourlyWeather(
                id = hourlyWeatherApi.dt ?: 0,
                cityName = cityName ?: "",
                hourTime = hourlyWeatherApi.dt.toHourTimeString(),
                temperature = hourlyWeatherApi.temp?.toInt().toString() + "\u2103",
                probabilityOfPrecipitation = hourlyWeatherApi.pop.toPercentString(),
                weather = Weather(
                    main = hourlyWeatherApi.weather[0].main ?: "",
                    description = hourlyWeatherApi.weather[0].description ?: "",
                    icon = hourlyWeatherApi.weather[0].icon.toTitleIcon()
                )
            )
        }
    }

    override suspend fun getDailyWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String
    ): List<DailyWeather> {
        val weatherApiDailyResponse = api.getDailyWeather(
            latitude,
            longitude,
            exclude = "current,minutely,hourly,alerts",
            units = units,
            language = language,
            apiKey = BuildConfig.WEATHER_ONE_CALL_API_KEY
        )

        return weatherApiDailyResponse.daily.map { dailyWeatherApi ->
            DailyWeather(
                id = dailyWeatherApi.dt ?: 0,
                dayDate = dailyWeatherApi.dt.toDayDateString(),
                temperatureMin = dailyWeatherApi.temp.min?.toInt().toString() + "\u2103",
                temperatureMax = dailyWeatherApi.temp.max?.toInt().toString() + "\u2103",
                probabilityOfPrecipitation = dailyWeatherApi.pop.toPercentString(),
                weather = Weather(
                    dailyWeatherApi.weather[0].main ?: "",
                    dailyWeatherApi.weather[0].description ?: "",
                    dailyWeatherApi.weather[0].icon.toTitleIcon()
                )
            )
        }
    }

    private fun Float?.toPercentString(): String {
        var percent = ""
        this?.let {
            if (this != 0F) {
                val dataPercent = it * 100F
                percent = "%.2f".format(dataPercent) + "%"
            }
        }
        return percent
    }

    private fun Long?.toHourTimeString(): String {
        var time = ""
        this?.let {
            val date = Date(it * 1000L)
            calendar.time = date
            time = "%02d:00".format(calendar.get(Calendar.HOUR_OF_DAY))
        }
        return time
    }

    private fun Long?.toDayDateString(): String {
        var dateStr = ""
        this?.let {
            val date = Date(it * 1000L)
            calendar.time = date
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1
            dateStr = String.format("%02d.%02d", day, month)
        }
        return dateStr
    }

    private fun String?.toTitleIcon(): String {
        return when (this) {
            "01n" -> {
                "moon"
            }
            "01d" -> {
                "sun"
            }
            "02n" -> {
                "few_clouds_n"
            }
            "02d" -> {
                "few_clouds_d"
            }
            "03n", "03d" -> {
                "scattered_clouds"
            }
            "04n", "04d" -> {
                "broken_clouds"
            }
            "09n", "09d" -> {
                "shower_rain"
            }
            "10n" -> {
                "rain_n"
            }
            "10d" -> {
                "rain_d"
            }
            "11n", "11d" -> {
                "thunderstorm"
            }
            "13n", "13d" -> {
                "snow"
            }
            "50n", "50d" -> {
                "mist"
            }
            else -> {
                "sun"
            }
        }
    }
}