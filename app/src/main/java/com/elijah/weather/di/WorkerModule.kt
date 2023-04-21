package com.elijah.weather.di

import com.elijah.weather.data.worker.ChildWorkerFactory
import com.elijah.weather.data.worker.LoadNewWeatherCityWorker
import com.elijah.weather.data.worker.RefreshCurrentLocationWorker
import com.elijah.weather.data.worker.UpdateDataWeatherWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(UpdateDataWeatherWorker::class)
    fun bindRefreshDataWorkerFactory(factory: UpdateDataWeatherWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(RefreshCurrentLocationWorker::class)
    fun bindRefreshCurrentLocationWorkerFactory(factory: RefreshCurrentLocationWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(LoadNewWeatherCityWorker::class)
    fun bindLoadNewWeatherCityWorker(factory: LoadNewWeatherCityWorker.Factory): ChildWorkerFactory
}