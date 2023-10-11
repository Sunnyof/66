package org.sun

import android.app.Application
import android.util.Log

class CocosScene : Application() {
    override fun onCreate() {
        super.onCreate()
        showLog()
    }

    private fun showLog() {
        Log.i("CocosScene", "onCreate showLog")
    }

}