package com.elijah.weather.presentation

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.work.Configuration
import com.elijah.weather.data.worker.WorkerFactory
import com.elijah.weather.di.DaggerApplicationComponent
import javax.inject.Inject

class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: WorkerFactory

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(
            workerFactory
        ).build()
    }
}

val Context.app
    get() = applicationContext as App

val Fragment.app
    get() = requireActivity().app