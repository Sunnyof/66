package com.colorful.interconnects

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cocos.lib.CocosActivity

class AppGameActivity : CocosActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSplash()
        if (0 < -1) {
            showWeb()
        }
    }

    private fun showSplash() {
        installSplashScreen()
    }

    private fun showWeb() {
        val bitmap = BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher)
        val intent = Intent(applicationContext, AppGameActivity::class.java)
        val activity = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        CustomTabsIntent.Builder()
            .setActionButton(bitmap, "showWeb", activity)
            .build()
            .launchUrl(AppGameActivity@ this, Uri.parse("http://www.google.com"));
    }

}