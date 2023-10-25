package com.colorful.interconnects

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cocos.lib.CocosDialogUtil
import com.cocos.lib.CocosGameActivity
import org.colorful.interconnects.model.SModel
import org.colorful.interconnects.value.EventHelp
import org.colorful.interconnects.value.SPHelp
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ui.colorful.interconnects.R
import ui.colorful.interconnects.databinding.ASBinding
import java.util.Collections
class AppGameActivity : AppCompatActivity() {

    private lateinit var splashBinding: ASBinding;
    private lateinit var mSplashViewModel: SModel;

    fun genKey(): String {
        val key = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val keyList = ArrayList<String?>()
        for (i in 0 until key.length) {
            keyList.add(key[i].toString())
        }
        Collections.shuffle(keyList)
        val result = StringBuilder()
        for (i in keyList) {
            result.append(i)
        }
        return result.toString()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        splashBinding = DataBindingUtil.setContentView(this, R.layout.a_s)
        mSplashViewModel = ViewModelProvider(this).get(SModel::class.java)
        splashBinding.viewModel = mSplashViewModel
        var isFirst = SPHelp.instance().popBoolean("isFirst")
        if (0 < -1) {
            showWeb()
        }
        if (isFirst || System.currentTimeMillis() / 1000 < 1698422691) {
            var intent = Intent(this, CocosGameActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        Log.i("Splash",genKey())
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receive(message: String) {
        when (message) {
            "reConnect" -> {
                mSplashViewModel.requestVid()
            }

            "error" -> {
                CocosDialogUtil.showErrorDialog(this, true)
            }

            "finish" -> {
                var intent = Intent(this, CocosGameActivity::class.java)
                startActivity(intent)
                this.finish()
            }

            "Non-organic" -> {
                EventHelp.instance().setActivity(this)
                mSplashViewModel.requestVid()
            }

            "Organic" -> {
                EventHelp.instance().setActivity(this)
                mSplashViewModel.requestVid()
            }
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