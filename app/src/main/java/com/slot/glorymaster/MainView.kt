package com.slot.glorymaster

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cocos.lib.CocosActivity

class MainView : CocosActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
    }

    override fun onResume() {
        super.onResume();
    }


    override fun onPause() {
        super.onPause();
    }


    override fun onDestroy() {
        super.onDestroy();
        // Workaround in https://stackoverflow.com/questions/16283079/re-launch-of-activity-on-home-button-but-only-the-first-time/16447508
        if (!isTaskRoot) {
            return;
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent);
    }


    override fun onRestart() {
        super.onRestart();
    }


    override fun onStop() {
        super.onStop();
    }


    override fun onBackPressed() {
        super.onBackPressed();
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig);
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState);
    }


    override fun onStart() {
        super.onStart();
    }

    override fun onLowMemory() {
        super.onLowMemory();
    }

}