package com.elijah.weather.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    "weather_hourly_city",
    foreignKeys = [ForeignKey(LocationDbModel::class, ["id"], ["city_id"], onDelete = CASCADE)]
)
data class WeatherOfHoursCityDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "hour_time")
    val hourTime: Long,
    @ColumnInfo(name = "city_id")
    val cityId: Int,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "probability_of_precipitation")
    val probabilityOfPrecipitation: String,
    @ColumnInfo(name = "temperature")
    val temperature: String,
    @ColumnInfo(name = "description_weather")
    val weatherDescription: String
)
