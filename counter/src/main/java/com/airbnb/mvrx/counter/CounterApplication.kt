package com.airbnb.mvrx.counter

import android.app.Application
import com.airbnb.mvrx.MvRx

class CounterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MvRx.install(this)
    }
}