package com.elijah.weather.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class WorkerFactory @Inject constructor(
    private val workerProviders: @JvmSuppressWildcards Map<Class<out ListenableWorker>,
            Provider<ChildWorkerFactory>>
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            UpdateDataWeatherWorker::class.qualifiedName -> {
                val childWorkerFactory = workerProviders[UpdateDataWeatherWorker::class.java]?.get()
                return childWorkerFactory?.create(appContext, workerParameters)
            }
            RefreshCurrentLocationWorker::class.qualifiedName -> {
                val childWorkerFactory =
                    workerProviders[RefreshCurrentLocationWorker::class.java]?.get()
                return childWorkerFactory?.create(appContext, workerParameters)
            }
            LoadNewWeatherCityWorker::class.qualifiedName -> {
                val childWorkerFactory =
                    workerProviders[LoadNewWeatherCityWorker::class.java]?.get()
                return childWorkerFactory?.create(appContext, workerParameters)
            }
            else -> null
        }
    }
}