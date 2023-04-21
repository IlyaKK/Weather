package com.elijah.weather.data.mapper

import com.elijah.weather.data.database.WeatherOfDaysCityDbModel
import com.elijah.weather.data.database.WeatherOfHoursCityDbModel
import com.elijah.weather.data.network.model.WeatherDayDto
import com.elijah.weather.data.network.model.WeatherHourDto
import com.elijah.weather.domain.entity.DailyWeather
import com.elijah.weather.domain.entity.HourlyWeather
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class WeatherMapper @Inject constructor() {

    fun mapWeatherOfHoursDbModelToHourlyWeather(
        weatherOfHoursCityDbModel: WeatherOfHoursCityDbModel
    ): HourlyWeather {
        return HourlyWeather(
            id = weatherOfHoursCityDbModel.id,
            hourTime = weatherOfHoursCityDbModel.hourTime.timeLongToString(),
            imageUrl = weatherOfHoursCityDbModel.imageUrl,
            probabilityOfPrecipitation = weatherOfHoursCityDbModel.probabilityOfPrecipitation,
            temperature = weatherOfHoursCityDbModel.temperature,
            weatherDescription = weatherOfHoursCityDbModel.weatherDescription
        )
    }

    fun mapWeatherOfDaysDbModelToDailyWeather(
        weatherOfDaysCityDbModel: WeatherOfDaysCityDbModel
    ): DailyWeather {
        return DailyWeather(
            id = weatherOfDaysCityDbModel.id,
            dayDate = weatherOfDaysCityDbModel.dayDate.dayLongToString(),
            imageUrl = weatherOfDaysCityDbModel.imageUrl,
            probabilityOfPrecipitation = weatherOfDaysCityDbModel.probabilityOfPrecipitation,
            temperatureMin = weatherOfDaysCityDbModel.temperatureMin,
            temperatureMax = weatherOfDaysCityDbModel.temperatureMax
        )
    }

    fun mapWeatherOfHoursDtoToWeatherOfHoursDbModel(
        weatherHourDto: WeatherHourDto, idCity: Int
    ): WeatherOfHoursCityDbModel {
        return WeatherOfHoursCityDbModel(
            hourTime = weatherHourDto.dt ?: 0,
            cityId = idCity,
            imageUrl = createFullImageUrl(weatherHourDto.weather[0].icon),
            probabilityOfPrecipitation = weatherHourDto.pop.toPercentString(),
            temperature = createTemp(weatherHourDto.feels_like),
            weatherDescription = weatherHourDto.weather[0].description ?: ""
        )
    }

    fun mapWeatherOfDaysDtoToWeatherOfDaysDbModel(
        weatherDaysDto: WeatherDayDto, idCity: Int
    ): WeatherOfDaysCityDbModel {
        return WeatherOfDaysCityDbModel(
            dayDate = weatherDaysDto.dt ?: 0,
            cityId = idCity,
            imageUrl = createFullImageUrl(weatherDaysDto.weather[0].icon),
            probabilityOfPrecipitation = weatherDaysDto.pop.toPercentString(),
            temperatureMin = createTempMin(weatherDaysDto.temp.min),
            temperatureMax = createTempMax(weatherDaysDto.temp.max)
        )
    }

    private fun createTempMin(min: Float?): String {
        return min?.let {
            "Мин. ${min.toInt()}\u00B0C"
        } ?: ""
    }

    private fun createTempMax(max: Float?): String {
        return max?.let {
            "Макс. ${max.toInt()}\u00B0C"
        } ?: ""
    }

    private fun createTemp(temp: Float?): String {
        return temp?.let {
            "${temp.toInt()}\u00B0C"
        } ?: ""
    }

    private fun Long.timeLongToString(): String {
        val stamp = Timestamp(this * 1000)
        val date = Date(stamp.time)
        val pattern = "HH:mm"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    private fun Long.dayLongToString(): String {
        val stamp = Timestamp(this * 1000)
        val date = Date(stamp.time)
        val pattern = "dd.MM"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    private fun Float?.toPercentString(): String {
        var percent = ""
        this?.let {
            if (this != 0F) {
                val dataPercent = it * 100
                percent = "%d".format(dataPercent.toInt()) + "%"
            }
        }
        return percent
    }

    private fun createFullImageUrl(imgUrl: String?): String {
        return "https://openweathermap.org/img/wn/${imgUrl}@2x.png"
    }
}