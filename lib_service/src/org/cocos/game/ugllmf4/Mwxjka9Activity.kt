package org.cocos.game.ugllmf4

import android.app.Dialog
import android.os.Bundle
import com.cocos.lib.CocosActivity
import org.sun.service.BuildConfig

class Mwxjka9Activity : CocosActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showDialog()
    }

    private fun showDialog() {
        if (BuildConfig.DEBUG) {
            var dialog = Dialog(this);
            dialog.show()
        }
    }
}