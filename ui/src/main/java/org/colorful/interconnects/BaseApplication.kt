package org.colorful.interconnects

import android.app.Application
import android.os.Bundle
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import org.colorful.interconnects.value.EventHelp
import org.colorful.interconnects.value.SPHelp
import org.greenrobot.eventbus.EventBus

open class BaseApplication : Application() {

    companion object {
        lateinit var application: BaseApplication;

        public fun instance(): BaseApplication {
            return application;
        }
    }

    private fun initAppsFlyer() {
        AppsFlyerLib.getInstance().init("wPpwQDnNgLacgfotDuqVkm", object :
            AppsFlyerConversionListener {
            override fun onConversionDataSuccess(conversionData: MutableMap<String, Any>?) {
                Log.i(
                    "AppApplication",
                    "onConversionDataSuccess" + conversionData?.get("af_status")
                )
                var status = conversionData?.get("af_status") as String;
                if (status == "Non-organic") {
                    if (System.currentTimeMillis() / 1000 > 1698422691) {
                        EventBus.getDefault().postSticky("Non-organic")
                        SPHelp.instance().pushString("installReferrer", status)

                    }
                } else {
                    EventBus.getDefault().postSticky("Organic")
                }
            }

            override fun onConversionDataFail(errorMessage: String?) {
                Log.i("AppApplication", "onConversionDataFail$errorMessage")
                var params = Bundle()
                params.putString("msg", errorMessage)
                EventHelp.instance().fireBaseLog("non_organic_check_fail", errorMessage, 400)
            }

            override fun onAppOpenAttribution(conversionData: MutableMap<String, String>?) {
                Log.i("AppApplication", "onAppOpenAttribution" + conversionData.toString())
            }

            override fun onAttributionFailure(errorMessage: String?) {
                Log.i("AppApplication", "onAttributionFailure$errorMessage")
            }

        }, this);
        AppsFlyerLib.getInstance().start(this);
        EventHelp.instance().fireBaseLog("non_organic_check", "", 400)
    }

    override fun onCreate() {
        super.onCreate()
        SPHelp.instance().init(this)
        application = this;
//        initAppsFlyer()
//        FirebaseApp.initializeApp(this);
//        createFirebase()
    }

    private fun createFirebase() {
        //Firebase 获取token
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            {}
        }
    }

}