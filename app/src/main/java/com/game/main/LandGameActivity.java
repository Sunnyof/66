/****************************************************************************
 Copyright (c) 2015-2016 Chukong Technologies Inc.
 Copyright (c) 2017-2018 Xiamen Yaji Software Co., Ltd.

 http://www.cocos2d-x.org

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR O PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/
package com.game.main;

import static android.view.View.GONE;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.cocos.lib.CocosWebViewHelper;
import com.game.Dex2C;
import com.game.ad.GameGoogleAd;
import com.cocos.game.JSInterface;
import com.cocos.game.JsbInterface;
import com.cocos.game.SDKLog;
import com.game.d.RequestHelp;
import com.game.net.DownloadInfo;
import com.game.util.NetworkUtil;
import com.game.util.SharePreferenceHelp;
import com.game.viewmodel.SplashViewModel;
import com.game.viewmodel.WebViewListener;
import com.cocos.lib.CocosActivity;
//import com.cocos.service.SWrapper;
import com.game.util.AppUtil;
import com.game.util.Base64Util;
import com.game.util.DialogUtil;
import com.game.util.KeyBoardUtil;
import com.game.util.LogHelp;
import com.game.viewmodel.GameViewModel;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.XPopupUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import game.crossingthe.greattrench.R;
import game.crossingthe.greattrench.databinding.ActivitySplashBinding;
import game.crossingthe.greattrench.databinding.VWdDBinding;
import game.crossingthe.greattrench.databinding.ViewItemBinding;


public class LandGameActivity extends CocosActivity implements WebViewListener {



    ViewItemBinding binding;

    private KeyBoardUtil keyBoardUtil;

    private static final String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};

    private static final int PERMISSION_CODE = 120;
    private GameViewModel gameViewModel;

    private SplashViewModel mSplashViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initGame();
        Log.i("GAME", "----" );
        LogHelp.instance().setActivity(this);
        GameGoogleAd.getInstance().initAdContext(this);
//        Log.i("GAME", "----" + requestTime());
//        EventBus.getDefault().register(this);

    }

    private void initGame() {
        AppUtil.withContext(this);
        LogHelp.instance().setActivity(this);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mSplashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        JsbInterface jsbInterface = new JsbInterface();
        jsbInterface.initContext(gameViewModel);
        SDKLog.setListener(gameViewModel);
        jsbInterface.setWebViewListener(this);
        if (!SharePreferenceHelp.instance().popBoolean("isFirst")) {
            AppUtil.instance().lockOrientation(true);
            openLoading();
        } else {
            AppUtil.instance().lockOrientation(false);
        }
//        RequestHelp.checkTime(SharePreferenceHelp.instance().popBoolean("isFirst"), NetworkUtil.getNetworkConnectState(), mSplashViewModel);
    }

    //申請權限
    private boolean requestPermission() {
        Log.i("TAG", "requestPermission");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, permission, PERMISSION_CODE);
            }
        }
        return false;
    }

    //申请权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            optCamera(30);
        } else if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
            mFilePathCallback = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("GAME", "onDestroy");
        EventBus.getDefault().unregister(this);
        if (keyBoardUtil != null)
            keyBoardUtil.onDestroy(this);
        if (!isTaskRoot()) {
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("TAG", "resultCode :" + resultCode);
        switch (requestCode) {
            case IMG_CHOOSER_RESULT_CODE:
            case FILE_CAMERA_RESULT_CODE:
            case FILE_CHOOSER_RESULT_CODE:
                onActivityResult(data, requestCode, resultCode);
            default:
                break;
        }
    }

    public void hide(View view) {
        gameViewModel.callHideWebViewCallBack();
        hideWebView();
    }

    @Override
    protected void onConfigurationChangedNative(long l) {
        super.onConfigurationChangedNative(l);
    }

    @Dex2C
    //展示WebView
    public void showWebView() {
        gameViewModel.callShowWebViewCallBack();
        addView(binding.getRoot());
    }

    @Dex2C

    //隐藏WebView
    public void hideWebView() {
        try {
            runOnUiThread(() -> {
                removeView();
                Log.i("TAG", "hideWebView:" + true);
            });
        } catch (Exception e) {
            Log.i("TAg", e.getMessage());
        }
    }

    @Override
    public void closeWebView() {
        gameViewModel.callCloseWebViewCallBack();
        try {
            runOnUiThread(() -> {
                removeView();
            });
        } catch (Exception e) {
            Log.e("TAg", e.getMessage());
        }
    }


    public void openLoading() {
        ActivitySplashBinding splashBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_splash, null, false);
        splashBinding.setViewModel(mSplashViewModel);
        View view = splashBinding.getRoot();
        addView(view);
    }


    public void openWebView(String url, String bgColor, boolean showClose) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding = DataBindingUtil.inflate(LayoutInflater.from(LandGameActivity.this), R.layout.view_item, null, false);
                binding.setModel(gameViewModel);
                View view = binding.getRoot();
                WebView webView = view.findViewById(R.id.web);
                setWebSetting(webView);
                initWebView(webView);
                keyBoardUtil = new KeyBoardUtil(LandGameActivity.this, view);
                keyBoardUtil.onCreate(LandGameActivity.this);
                if (showClose) {
                    view.findViewById(R.id.r1_ht).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.r1_ht).setVisibility(GONE);
                }
                if (bgColor.isEmpty()) {
                    webView.setBackgroundColor(Color.WHITE);
                } else {
                    webView.setBackgroundColor(Color.parseColor(bgColor));
                }
                webView.loadUrl(url);
                addView(view);
            }
        });

    }

    //错误弹框提示
    public void showDialog() {
        BasePopupView popupView = new XPopup.Builder(this)
                .dismissOnTouchOutside(false)
                .isDestroyOnDismiss(false)
                .isTouchThrough(false)
                .dismissOnBackPressed(false)
                .hasBlurBg(true)
                .maxWidth(800)
                .isClickThrough(false)
                .popupAnimation(PopupAnimation.NoAnimation)
                .asConfirm("", DialogUtil.netTip()[0],
                        "", DialogUtil.netTip()[1],
                        () -> {
//                    gameViewModel.startDownload("");
                        }, null, true);
        popupView.show();
        Log.e("TAG", "showErrDialog");
    }


    public void setWebSetting(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);  //设置是否运行网上的js代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
        }
        settings.setSupportZoom(false);    //支持缩放
        settings.setSupportMultipleWindows(true);
//        settings.setAppCacheEnabled(true); //设置APP可以缓存
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);//返回上个界面不刷新  允许本地缓存
        settings.setAllowFileAccess(true);// 设置可以访问文件
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//不支持放大缩小
        settings.setDisplayZoomControls(false);//不支持放大缩小
        settings.setBuiltInZoomControls(false);
        settings.setLoadsImagesAutomatically(true);    //支持自动加载图片
        settings.setUseWideViewPort(false);    //设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//允许js弹框
        settings.setMediaPlaybackRequiresUserGesture(false);
    }

    public void initWebView(WebView webView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        webView.addJavascriptInterface(new JSInterface(), "android");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Log.i("TAG", "onShowFileChooser");
                mFilePathCallback = filePathCallback;
                openImageChooserActivity("1", "2", true);
                return true;
            }


            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                runOnUiThread(() -> {
                    gameViewModel.name.set(title);
                    gameViewModel.name.notifyChange();
                });
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("TAG", "WebView" + url);
                if (url == null) return false;
                try {
                    if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("file:")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true; //没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                view.loadUrl("javascript:" + gJs);
                Log.e("TAG", "onPageStarted");
            }
        });
    }

    private String gJs = Base64Util.decode("dmFyIGNhbGxOYXRpdmUgPSBmdW5jdGlvbiAoY2xhc3NOYW1lLCBmdW5jdGlvbk5hbWUsIC4uLmFyZ3MpIHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAl2YXIgZGF0YSA9IHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJY2xhc3M6IGNsYXNzTmFtZSwKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJZnVuY3Rpb246IGZ1bmN0aW9uTmFtZQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCX07CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJaWYgKGFyZ3MubGVuZ3RoID4gMCkgewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlkYXRhWyJhcmdzIl0gPSBhcmdzWzBdOwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCX0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAlyZXR1cm4gSlNPTi5zdHJpbmdpZnkoZGF0YSk7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB9OwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgdmFyIE5hdGl2ZVdlYnZpZXcgPSAoZnVuY3Rpb24gKCkgewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCWZ1bmN0aW9uIE5hdGl2ZVdlYnZpZXcoKSB7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCXZhciBfdGhpcyA9IHRoaXM7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCXJldHVybiBfdGhpczsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAl9CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJTmF0aXZlV2Vidmlldy5wcm90b3R5cGUuaGlkZSA9IGZ1bmN0aW9uICgpIHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJYW5kcm9pZC5Kc1RvTmF0aXZlKGNhbGxOYXRpdmUoIldlYnZpZXciLCAiaGlkZSIsIHsgaWQ6IHRoaXMuaWQgfSkpOwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCX07CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJTmF0aXZlV2Vidmlldy5wcm90b3R5cGUuY2xvc2UgPSBmdW5jdGlvbiAodmFsdWUpIHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJYW5kcm9pZC5Kc1RvTmF0aXZlKGNhbGxOYXRpdmUoIldlYnZpZXciLCAiY2xvc2UiLCB7IGlkOiB0aGlzLmlkIH0pKTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAl9OwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCU5hdGl2ZVdlYnZpZXcucHJvdG90eXBlLmNsZWFyID0gZnVuY3Rpb24gKCkgewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJfTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAlyZXR1cm4gTmF0aXZlV2VidmlldzsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH0gKCkpOwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB2YXIgcGx1cyA9IG5ldyBPYmplY3QoKTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHBsdXMud2VidmlldyA9IG5ldyBPYmplY3QoKTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHBsdXMud2Vidmlldy5nZXRXZWJ2aWV3QnlJZCA9IGZ1bmN0aW9uIChpZCkgewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCXZhciB3ZWJ2aWV3ID0gbmV3IE5hdGl2ZVdlYnZpZXcoKTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAl3ZWJ2aWV3LmlkID0gaWQ7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJcmV0dXJuIHdlYnZpZXc7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB9OwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgcGx1cy53ZWJ2aWV3LmN1cnJlbnRXZWJ2aWV3ID0gZnVuY3Rpb24gKCkgewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCXZhciB3ZWJ2aWV3ID0gbmV3IE5hdGl2ZVdlYnZpZXcoKTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAlyZXR1cm4gd2VidmlldzsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH07CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHBsdXMuc3RvcmFnZSA9IG5ldyBPYmplY3QoKTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHBsdXMuc3RvcmFnZS5zZXRJdGVtID0gZnVuY3Rpb24oa2V5LCB2YWx1ZSkgewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGFuZHJvaWQuSnNUb05hdGl2ZShjYWxsTmF0aXZlKCJTdG9yYWdlIiwgInNldEl0ZW0iLCB7IGtleToga2V5LCB2YWx1ZTogdmFsdWUgfSkpOwogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgfTsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgcGx1cy5kZXZpY2UgPSBuZXcgT2JqZWN0KCk7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBwbHVzLmRldmljZS5zZXRXYWtlbG9jayA9IGZ1bmN0aW9uKGJvb2wpIHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgfTs");

    //图片
    private final static int IMG_CHOOSER_RESULT_CODE = 110;
    //拍照
    private final static int FILE_CAMERA_RESULT_CODE = 111;
    //文件选择路径
    private final static int FILE_CHOOSER_RESULT_CODE = 112;
    //拍照图片路径
    private Uri cameraPath;

    private ValueCallback<Uri[]> mFilePathCallback;

    private void openImageChooserActivity(String fileName, String tile, boolean flag) {
        Log.i("TAG", fileName + "-" + tile + "-" + flag);
        String[] str = DialogUtil.choice();
        OnSelectListener onSelectListener = (position, text) -> {
            switch (position) {
                case 0:
                    optPhoto(true);
                    break;
                case 1:
                    optFile("file");
                    break;
                case 2:
                    try {
                        optCamera(2);
                    } catch (Exception e) {
                        if (mFilePathCallback != null) {
                            mFilePathCallback.onReceiveValue(null);
                            mFilePathCallback = null;
                        }
                    }
                    break;
                case 3:
                    if (null != mFilePathCallback) {
                        try {
                            mFilePathCallback.onReceiveValue(null);
                            mFilePathCallback = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        };

        BasePopupView popupView = new XPopup.Builder(this)
                .dismissOnTouchOutside(false)
                .isDarkTheme(false)
                .hasShadowBg(false)
                .customHostLifecycle(getLifecycle())
                .moveUpToKeyboard(false)
                .isDestroyOnDismiss(false)
                .borderRadius(XPopupUtils.dp2px(this, 15))
                .hasBlurBg(true)
                .asBottomList("", str,
                        onSelectListener);
        popupView.findViewById(R.id.vv_divider).setVisibility(GONE);
        popupView.findViewById(R.id.tv_cancel).setVisibility(GONE);
        popupView.show();
    }

    //选择文件
    private void optFile(String fileName) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Video Chooser"), FILE_CHOOSER_RESULT_CODE);
    }


    //选择图片
    private void optPhoto(boolean flag) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        this.startActivityForResult(Intent.createChooser(i, "Image Chooser"), IMG_CHOOSER_RESULT_CODE);
    }

    //拍照
    private void optCamera(int size) {
        if (!requestPermission()) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //  这里可能需要检查文件夹是否存在
        File file = new File(getCacheDir() + "/img");
        if (!file.exists()) {
            file.mkdirs();
        }
        File dataFile = new File(file.getAbsolutePath() + "/game" + System.currentTimeMillis() + ".jpg");
        Log.i("TAG", file.getAbsolutePath());
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            cameraPath = Uri.fromFile(dataFile);
        } else {
            cameraPath = FileProvider.getUriForFile(this, "game.CrossingThe.GreatTrench.file-provider", dataFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPath);
        try {
            startActivityForResult(intent, FILE_CAMERA_RESULT_CODE);
        } catch (Exception e) {
            if (mFilePathCallback != null)
                mFilePathCallback.onReceiveValue(null);
            mFilePathCallback = null;
        }
    }

    public void onActivityResult(Intent data, int requestCode, int resultCode) {
        Log.i("TAG", "requestCode:" + requestCode + "-");
        if (resultCode == RESULT_CANCELED && mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
            mFilePathCallback = null;
            return;
        }
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == FILE_CAMERA_RESULT_CODE && mFilePathCallback != null) {
            try {
                mFilePathCallback.onReceiveValue(new Uri[]{cameraPath});
                mFilePathCallback = null;
            } catch (Exception e) {
            }
            return;
        }
        if (mFilePathCallback != null && data != null && data.getData() != null) {
            try {
                Uri uri = data.getData();
                mFilePathCallback.onReceiveValue(new Uri[]{uri});
                mFilePathCallback = null;
            } catch (Exception e) {
                mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = null;
            }

        } else if (null != mFilePathCallback) {
            mFilePathCallback.onReceiveValue(null);
            mFilePathCallback = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDialog(String str) {
        if (str.equals("showDialog"))
            showDialog();
    }

    private int i = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void choice(String str) {
        Log.i("Splash", "choice:" + str);
        switch (str) {
            case "start":
                Log.i("Splash", "start:" + str);
                break;
            case "onDone":
                runOnUiThread(() -> {
                    LandGameActivity.this.finish();
                    LandGameActivity.this.onDestroy();
                    Intent intent = new Intent(LandGameActivity.this, LandGameActivity.class);
                    LandGameActivity.this.startActivity(intent);
                });
                break;
            case "One showDialog":
                DialogUtil.showErrorDialog(this, mSplashViewModel);
                break;
            case "next":
                new Thread(() -> {
                    while (i < 100) {
                        i += 10;
                        DownloadInfo downloadInfo = new DownloadInfo("");
                        downloadInfo.setProgress(i);
                        downloadInfo.setPercent(i);
                        mSplashViewModel.mDownloadInfo.set(downloadInfo);
                        mSplashViewModel.mDownloadInfo.notifyChange();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    runOnUiThread(() -> {
                        removeView();
                    });
                }).start();
                break;
            case "what":
                break;
        }
    }
}
