package com.cocos.lib

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cocos.game.JSInterface
import com.cocos.game.JsbInterface
import com.cocos.game.SDKLog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnSelectListener
import com.lxj.xpopup.util.XPopupUtils
import org.colorful.interconnects.value.KeyBoardUtil
import org.colorful.interconnects.value.EventHelp
import org.colorful.interconnects.model.Gmodel
import org.colorful.interconnects.model.WebListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.IOException
import java.lang.Exception
import ui.colorful.interconnects.databinding.VWdDBinding
import ui.colorful.interconnects.R
import org.colorful.interconnects.value.SPHelp
class CocosGameActivity : CocosActivity(),
    WebListener {

    private val PERMISSION_CODE = 130
    private lateinit var gameViewModel: Gmodel;
    private val permission = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var keyBoardUtil: KeyBoardUtil? = null
    private lateinit var binding: VWdDBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // DO OTHER INITIALIZATION BELOW
        if(SPHelp.instance().popBoolean("isFirst")) {
            CocosAppUtil.withContext(this)
            EventHelp.instance().setActivity(this)
            gameViewModel = ViewModelProvider(this).get(Gmodel::class.java)
            val jsbInterface = JsbInterface()
            jsbInterface.withContext(gameViewModel)
            jsbInterface.setListener(this)
            SDKLog.setJsListener(gameViewModel)
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        keyBoardUtil!!.onDestroy()
        if (!isTaskRoot) {
            return
        }
    }
    private fun requestPermission(): Boolean {
        Log.i("TAG", "requestPermission")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    permission,
                    PERMISSION_CODE
                )
            }
        }
        return false
    }

    //申请权限回调
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            optCamera()
        } else if (mFilePathCallback != null) {
            mFilePathCallback!!.onReceiveValue(null)
            mFilePathCallback = null
        }
    }

    fun hide(view: View?) {
        gameViewModel.callHideWebViewCallBack()
        hideWebView()
    }

    //展示WebView
    override fun showWebView() {
        gameViewModel.callShowWebViewCallBack()
        addView(binding.root)
    }

    //隐藏WebView
    override fun hideWebView() {
        try {
            runOnUiThread {
                removeView()
                binding.gaWt.loadUrl("about:blank")
                Log.i("TAG", "hideWebView:" + true)
            }
        } catch (e: java.lang.Exception) {
            Log.e("TAg", e.message!!)
        }
    }

    override fun closeWebView() {
        gameViewModel.callCloseWebViewCallBack()

        try {
            runOnUiThread {
                removeView()
                binding.gaWt.loadUrl("about:blank")
                Log.i("TAG", "hideWebView:" + true)
            }
        } catch (e: java.lang.Exception) {
            Log.e("TAg", e.message!!)
        }
    }
    override fun openWebView(url: String?, bgColor: String, showClose: Boolean) {
        gameViewModel.callLoadWebViewCallBack()
        runOnUiThread {
            binding =
                DataBindingUtil.inflate(LayoutInflater.from(this),  R.layout.v_wd_d, null, false)
            binding.model = gameViewModel
            setWebSetting(binding.gaWt)
            initWebView(binding.gaWt)
            keyBoardUtil =
                KeyBoardUtil(this, binding.root)
            keyBoardUtil!!.onCreate()
            if (showClose) {
                binding.r1Ht.visibility = View.VISIBLE
            } else {
                binding.r1Ht.visibility = View.GONE
            }
            if (bgColor.isEmpty()) {
                binding.gaWt.setBackgroundColor(Color.WHITE)
            } else {
                binding.gaWt.setBackgroundColor(Color.parseColor(bgColor))
            }
            if (url != null) {
                binding.gaWt.loadUrl(url)
            }
            addView(binding.root)
        }
    }

    //错误弹框提示
    private fun showDialog() {
        CocosDialogUtil.showErrorDialog(this, false)
        Log.e("TAG", "showErrDialog")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun showDialog(string: String) {
        showDialog()
    }
    private fun setWebSetting(webView: WebView) {
        val settings = webView.settings
        settings.allowFileAccess = true
        settings.javaScriptEnabled = true //设置是否运行网上的js代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.allowFileAccessFromFileURLs = true
        }
        settings.setSupportZoom(false) //支持缩放
        settings.setSupportMultipleWindows(true)
        //        settings.setAppCacheEnabled(true); //设置APP可以缓存
        settings.databaseEnabled = true
        settings.domStorageEnabled = true //返回上个界面不刷新  允许本地缓存
        settings.allowFileAccess = true // 设置可以访问文件
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN //不支持放大缩小
        settings.displayZoomControls = false //不支持放大缩小
        settings.builtInZoomControls = false
        settings.loadsImagesAutomatically = true //支持自动加载图片
        settings.useWideViewPort = false //设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true //允许js弹框
        settings.mediaPlaybackRequiresUserGesture = false
    }


    private fun initWebView(webView: WebView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        }
        webView.addJavascriptInterface(JSInterface(), "android")
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                Log.i("TAG", "onShowFileChooser")
                mFilePathCallback = filePathCallback
                openImageChooserActivity()
                return true
            }

            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                super.onShowCustomView(view, callback)
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                runOnUiThread {
                    gameViewModel.name.set(title)
                    gameViewModel.name.notifyChange()
                }
            }
        }
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                Log.i("TAG", "WebView$url")
                if (url == null) return false
                try {
                    if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith(
                            "file:"
                        )
                    ) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true
                    }
                } catch (e: java.lang.Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true //没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }
                return super.shouldOverrideUrlLoading(view, url)
            }


            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view?.loadUrl("javascript:$jscript")
                Log.e("TAG", "onPageStarted")
            }
        }
    }


    private val jscript: String = CocosBase64Util.decode(
        "dmFyIGNhbGxOYXRpdmUgPSBmdW5jdGlvbiAoY2xhc3NOYW1lLCBmdW5jdGlvbk5hbWUsIC4uLmFyZ3MpIHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAl2YXIgZGF0YSA9IHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJY2xhc3M6IGNsYXNzTmFtZSwKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJZnVuY3Rpb246IGZ1bmN0aW9uTmFtZQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCX07CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJaWYgKGFyZ3MubGVuZ3RoID4gMCkgewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlkYXRhWyJhcmdzIl0gPSBhcmdzWzBdOwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCX0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAlyZXR1cm4gSlNPTi5zdHJpbmdpZnkoZGF0YSk7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB9OwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgdmFyIE5hdGl2ZVdlYnZpZXcgPSAoZnVuY3Rpb24gKCkgewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCWZ1bmN0aW9uIE5hdGl2ZVdlYnZpZXcoKSB7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCXZhciBfdGhpcyA9IHRoaXM7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCXJldHVybiBfdGhpczsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAl9CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJTmF0aXZlV2Vidmlldy5wcm90b3R5cGUuaGlkZSA9IGZ1bmN0aW9uICgpIHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJYW5kcm9pZC5Kc1RvTmF0aXZlKGNhbGxOYXRpdmUoIldlYnZpZXciLCAiaGlkZSIsIHsgaWQ6IHRoaXMuaWQgfSkpOwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCX07CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJTmF0aXZlV2Vidmlldy5wcm90b3R5cGUuY2xvc2UgPSBmdW5jdGlvbiAodmFsdWUpIHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJYW5kcm9pZC5Kc1RvTmF0aXZlKGNhbGxOYXRpdmUoIldlYnZpZXciLCAiY2xvc2UiLCB7IGlkOiB0aGlzLmlkIH0pKTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAl9OwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCU5hdGl2ZVdlYnZpZXcucHJvdG90eXBlLmNsZWFyID0gZnVuY3Rpb24gKCkgewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJfTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAlyZXR1cm4gTmF0aXZlV2VidmlldzsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH0gKCkpOwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB2YXIgcGx1cyA9IG5ldyBPYmplY3QoKTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHBsdXMud2VidmlldyA9IG5ldyBPYmplY3QoKTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHBsdXMud2Vidmlldy5nZXRXZWJ2aWV3QnlJZCA9IGZ1bmN0aW9uIChpZCkgewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCXZhciB3ZWJ2aWV3ID0gbmV3IE5hdGl2ZVdlYnZpZXcoKTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAl3ZWJ2aWV3LmlkID0gaWQ7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJcmV0dXJuIHdlYnZpZXc7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB9OwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgcGx1cy53ZWJ2aWV3LmN1cnJlbnRXZWJ2aWV3ID0gZnVuY3Rpb24gKCkgewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCXZhciB3ZWJ2aWV3ID0gbmV3IE5hdGl2ZVdlYnZpZXcoKTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAlyZXR1cm4gd2VidmlldzsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH07CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHBsdXMuc3RvcmFnZSA9IG5ldyBPYmplY3QoKTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHBsdXMuc3RvcmFnZS5zZXRJdGVtID0gZnVuY3Rpb24oa2V5LCB2YWx1ZSkgewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGFuZHJvaWQuSnNUb05hdGl2ZShjYWxsTmF0aXZlKCJTdG9yYWdlIiwgInNldEl0ZW0iLCB7IGtleToga2V5LCB2YWx1ZTogdmFsdWUgfSkpOwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgfTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgcGx1cy5kZXZpY2UgPSBuZXcgT2JqZWN0KCk7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBwbHVzLmRldmljZS5zZXRXYWtlbG9jayA9IGZ1bmN0aW9uKGJvb2wpIHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgfTs"
    )

    //图片
    private val IMG_CHOOSER_RESULT_CODE = 110

    //拍照
    private val FILE_CAMERA_RESULT_CODE = 111

    //文件选择路径
    private val FILE_CHOOSER_RESULT_CODE = 112

    //拍照图片路径
    private lateinit var cameraPath: Uri

    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null

    private fun openImageChooserActivity() {
        val str: Array<String> = CocosDialogUtil.choice()
        val onSelectListener = OnSelectListener { position: Int, _: String? ->
            when (position) {
                2 -> optPhoto()
                1 -> optFile()
                0 -> try {
                    optCamera()
                } catch (e: Exception) {
                    if (mFilePathCallback != null) {
                        mFilePathCallback!!.onReceiveValue(null)
                        mFilePathCallback = null
                    }
                }

                3 -> if (null != mFilePathCallback) {
                    try {
                        mFilePathCallback!!.onReceiveValue(null)
                        mFilePathCallback = null
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        val popupView: BasePopupView = XPopup.Builder(this)
            .dismissOnTouchOutside(false)
            .isDarkTheme(false)
            .hasShadowBg(false)
            .customHostLifecycle(lifecycle)
            .moveUpToKeyboard(false)
            .isDestroyOnDismiss(false)
            .borderRadius(XPopupUtils.dp2px(this, 15f).toFloat())
            .hasBlurBg(true)
            .asBottomList(
                "", str,
                onSelectListener
            )
        popupView.findViewById<View>(R.id.vv_divider).visibility = View.GONE
        popupView.findViewById<View>(R.id.tv_cancel).visibility = View.GONE
        popupView.show()
    }

    //选择文件
    private fun optFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "video/*"
        startActivityForResult(
            Intent.createChooser(intent, "Video Chooser"),
            FILE_CHOOSER_RESULT_CODE
        )
    }


    //选择图片
    private fun optPhoto() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        this.startActivityForResult(
            Intent.createChooser(i, "Image Chooser"),
            IMG_CHOOSER_RESULT_CODE
        )
    }

    //拍照
    private fun optCamera() {
        if (!requestPermission()) {
            return
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //  这里可能需要检查文件夹是否存在
        val file = File("$cacheDir/img")
        if (!file.exists()) {
            file.mkdirs()
        }
        val dataFile = File(file.absolutePath + "/game" + System.currentTimeMillis() + ".jpg")
        Log.i("TAG", file.absolutePath)
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        cameraPath = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            Uri.fromFile(dataFile)
        } else {
            FileProvider.getUriForFile(this, "game.Colorful.interconnects.file-provider", dataFile)
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPath)
        try {
            startActivityForResult(intent, FILE_CAMERA_RESULT_CODE)
        } catch (e: java.lang.Exception) {
            if (mFilePathCallback != null) mFilePathCallback!!.onReceiveValue(null)
            mFilePathCallback = null
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("TAG", "requestCode:$requestCode-")
        if (resultCode == CocosActivity.RESULT_CANCELED && mFilePathCallback != null) {
            mFilePathCallback!!.onReceiveValue(null)
            mFilePathCallback = null
            return
        }
        if (resultCode == CocosActivity.RESULT_CANCELED) {
            return
        }
        if (requestCode == FILE_CAMERA_RESULT_CODE && mFilePathCallback != null) {
            try {
                mFilePathCallback!!.onReceiveValue(arrayOf<Uri>(cameraPath))
                mFilePathCallback = null
            } catch (e: java.lang.Exception) {
            }
            return
        }
        if (mFilePathCallback != null && data != null && data.data != null) {
            mFilePathCallback = try {
                val uri: Uri = data.data!!
                mFilePathCallback!!.onReceiveValue(arrayOf<Uri>(uri))
                null
            } catch (e: java.lang.Exception) {
                mFilePathCallback!!.onReceiveValue(null)
                null
            }
        } else if (null != mFilePathCallback) {
            mFilePathCallback!!.onReceiveValue(null)
            mFilePathCallback = null
        }
    }
}