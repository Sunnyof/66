package org.colorful.interconnects

import android.app.Application

class BaseApplication : Application() {

    companion object {
        lateinit var application: BaseApplication;

        public fun instance(): BaseApplication {
            return application;
        }

//        init {
//            try {
//                System.loadLibrary("nc")
//            } catch (e: UnsatisfiedLinkError) {
//                e.printStackTrace()
//            }
//        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this;
    }

}