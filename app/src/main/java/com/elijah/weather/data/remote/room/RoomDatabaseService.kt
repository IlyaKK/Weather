package com.elijah.weather.data.remote.room

import androidx.room.*
import com.elijah.weather.data.remote.room.entity.LocationDatabase
import kotlinx.coroutines.flow.Flow

@Database(entities = [LocationDatabase::class], version = 1)
abstract class RoomDatabaseService : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}

@Dao
interface LocationDao {
    @Insert(entity = LocationDatabase::class)
    suspend fun insertLocation(locationDatabase: LocationDatabase)

    @Query(
        "SELECT * FROM locationdatabase " +
                "WHERE city_name = :cityName"
    )
    suspend fun getCityByName(cityName: String): List<LocationDatabase>

    @Query("SELECT * FROM locationdatabase " +
            "ORDER BY current DESC")
    fun getAllLocations(): Flow<List<LocationDatabase>>

    @Query("DELETE FROM locationdatabase WHERE city_name = :cityName ")
    suspend fun deleteLocationByNameCity(cityName: String)

    @Delete
    suspend fun deleteLocation(locationDatabase: LocationDatabase)

    @Query(
        "SELECT * FROM locationdatabase " +
                "WHERE current = 1"
    )
    suspend fun getCurrentLocation(): LocationDatabase?
}