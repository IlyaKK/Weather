package com.elijah.weather.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(entity = WeatherOfDaysCityDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun insertWeatherCityOfDays(weatherOfDaysCityDbModel: List<WeatherOfDaysCityDbModel>)

    @Insert(entity = WeatherOfHoursCityDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun insertWeatherCityOfHours(weatherOfHoursCityDbModel: List<WeatherOfHoursCityDbModel>)

    @Query(
        "SELECT * FROM weather_days_city " +
                "INNER JOIN locations ON locations.id = weather_days_city.city_id " +
                "WHERE locations.id LIKE :cityId " +
                "ORDER BY weather_days_city.day_date"
    )
    fun getDaysWeatherByIdCity(cityId: Int): Flow<List<WeatherOfDaysCityDbModel>>

    @Query(
        "SELECT * FROM weather_hourly_city " +
                "INNER JOIN locations ON locations.id = weather_hourly_city.city_id " +
                "WHERE locations.id LIKE :cityId " +
                "ORDER BY weather_hourly_city.hour_time"
    )
    fun getHoursWeatherByIdCity(cityId: Int): Flow<List<WeatherOfHoursCityDbModel>>

    @Query(
        "DELETE FROM weather_days_city " +
                "WHERE city_id = :idCity "
    )
    suspend fun deleteWeatherDayOfCity(idCity: Int)

    @Query(
        "DELETE FROM weather_hourly_city " +
                "WHERE city_id = :idCity "
    )
    suspend fun deleteWeatherHoursOfCity(idCity: Int)
}