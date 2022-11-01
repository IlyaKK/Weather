package com.elijah.weather.data.remote.retrofit.response

data class WeatherApi(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)

data class WeatherHourlyResponse(
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
    val timezone_offset: Long?,
    val hourly: List<HourlyWeatherApi>,
)

data class HourlyWeatherApi(
    val dt: Long?,
    val temp: Float?,
    val feels_like: Float?,
    val pressure: Int?,
    val humidity: Int?,
    val dew_point: Float?,
    val uvi: Float?,
    val clouds: Int?,
    val visibility: Int?,
    val wind_speed: Float?,
    val wind_deg: Int?,
    val wind_gust: Float?,
    val weather: List<WeatherApi>,
    val pop: Float?
)

data class WeatherDailyResponse(
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
    val timezone_offset: Long?,
    val daily: List<DailyWeatherApi>
)

data class DailyWeatherApi(
    val dt: Long?,
    val sunrise: Long?,
    val sunset: Long?,
    val moonrise: Long?,
    val moonset: Long?,
    val moon_phase: Float?,
    val temp: Temp,
    val weather: List<WeatherApi>,
    val pop: Float?
)

data class Temp(
    val day: Float?,
    val min: Float?,
    val max: Float?,
    val night: Float?,
    val eve: Float?,
    val morn: Float?,
    val weather: List<WeatherApi>,
    val pop: Float?
)
