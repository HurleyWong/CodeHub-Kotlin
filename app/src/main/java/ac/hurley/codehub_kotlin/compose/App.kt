package ac.hurley.codehub_kotlin.compose

import ac.hurley.util.AppContext
import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.initialize(applicationContext)
        initData()
    }

    private fun initData() {

    }
}