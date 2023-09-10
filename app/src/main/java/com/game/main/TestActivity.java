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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.cocos.game.GameJScript;
import com.game.ad.GameGoogleAd;
import com.g.done.RHelp;
import com.game.util.AppUtil;
import com.game.util.DialogUtil;
import com.game.util.KeyBoardUtil;
import com.game.util.LogHelp;
import com.game.util.SharePreferenceHelp;
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

import game.crossingthe.greattrench.BuildConfig;
import game.crossingthe.greattrench.R;
import game.crossingthe.greattrench.databinding.VWdDBinding;


public class TestActivity extends AppCompatActivity {
    private KeyBoardUtil keyBoardUtil;
    private View mView;
    private static final String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    private static final int PERMISSION_CODE = 120;
    private volatile boolean mIsHide = false;
    private VWdDBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogHelp.instance().fireBaseLog("androidId", "TestActivity onCreate", 200);
        binding = DataBindingUtil.setContentView(this, R.layout.v_wd_d);
        LogHelp.instance().setActivity(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        LogHelp.instance().setActivity(this);
        builder.detectFileUriExposure();
        GameGoogleAd.instance().withApplication(this);
        initGame();
        EventBus.getDefault().register(this);
        new Handler(Looper.getMainLooper()).postDelayed(() -> openWebView("https://www.goldendragon77.club?code=1109" + SharePreferenceHelp.instance().popString("gameCode"), "", false), 1000);
        binding.b3Ct.setOnClickListener(view -> {
            binding.gaWt2.setVisibility(GONE);
            binding.r1Ht.setVisibility(GONE);
        });
    }

    private void initGame() {
        AppUtil.withContext(this);
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
    protected void onResume() {
        super.onResume();
        if (binding != null) {
            binding.gaWt.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (binding != null) {
            binding.gaWt.onPause();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("TAG", "onConfigurationChanged");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    public void hide(View view) {
        hideWebView();
    }


    //隐藏WebView
    public void hideWebView() {
        if (mView == null || !mView.isShown() || mIsHide) {
            Log.e("TAG", "hideWebView");
            return;
        }
        mIsHide = true;
        try {
            runOnUiThread(() -> {
                Log.i("TAG", "hideWebView:" + true);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                mView = null;
            });
            Log.i("TAG", "hideWebView: 2" + (mView.getVisibility() == GONE));
        } catch (Exception e) {
            Log.i("TAg", e.getMessage());
        }
    }


    public void openWebView(String url, String bgColor, boolean showClose) {
        Log.i("TAG", url + "-" + showClose + "--" + bgColor);
        runOnUiThread(() -> {
            setWebSetting(binding.gaWt);
            initWebView(binding.gaWt);
            keyBoardUtil = new KeyBoardUtil(this, binding.getRoot());
            keyBoardUtil.setInputType(this);
            keyBoardUtil.onCreate(this);
            binding.gaWt.loadUrl(RHelp.managerTime(true, "workTime"));
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
        Log.e("TAG", "setWebSetting");
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);  //设置是否运行网上的js代码
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setSupportZoom(true);    //支持缩放
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);//返回上个界面不刷新  允许本地缓存
        settings.setAllowFileAccess(true);// 设置可以访问文件
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//不支持放大缩小
        settings.setDisplayZoomControls(false);//不支持放大缩小
        settings.setBuiltInZoomControls(false);
        settings.setLoadsImagesAutomatically(true);    //支持自动加载图片
        settings.setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//允许js弹框
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setUserAgentString(settings.getUserAgentString() + "AndSkyWeb&packageName=" + BuildConfig.APPLICATION_ID);
        Log.i("WebView agemt", settings.getUserAgentString() + "AndSkyWeb&packageName=" + BuildConfig.APPLICATION_ID);
    }


    public void initWebView(WebView webView) {

        webView.addJavascriptInterface(new GameJScript(), "GameToNative");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Log.i("TAG", "onShowFileChooser");
                mFilePathCallback = filePathCallback;
                openImageChooserActivity("1", "2", true);
                return true;
            }


            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                // 在这里处理弹窗，例如使用AlertDialog显示消息
                // 如果需要，您可以自定义弹窗样式和行为
                // 最后调用result.confirm()来关闭弹窗
                // 返回true表示已经处理了弹窗
                Log.i("WebView", "onJsAlert:" + url);
                return true;
            }

            // 处理JavaScript的confirm弹窗
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result) {
                // 在这里处理confirm弹窗，可以显示自定义的对话框，用户选择后调用result.confirm()或result.cancel()
                // 返回true表示已经处理了弹窗
                Log.i("WebView", "onJsConfirm:" + url);
                return true;
            }

            // 处理JavaScript的prompt弹窗
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final android.webkit.JsPromptResult result) {
                // 在这里处理prompt弹窗，可以显示自定义的对话框，用户输入后调用result.confirm()或result.cancel()
                // 返回true表示已经处理了弹窗
                Log.i("WebView", "onJsPrompt:" + url);
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.i("WebView", error.getDescription().toString());
                    BasePopupView popupView = new XPopup.Builder(TestActivity.this)
                            .dismissOnTouchOutside(false)
                            .isDestroyOnDismiss(false)
                            .isTouchThrough(false)
                            .dismissOnBackPressed(false)
                            .hasBlurBg(true)
                            .maxWidth(800)
                            .isClickThrough(false)
                            .popupAnimation(PopupAnimation.NoAnimation)
                            .asConfirm("", "Lỗi kết nối mạng, vui lòng kiểm tra mạng",
                                    "", "Ok",
                                    () -> {
                                        webView.reload();
                                    }, null, true);
                    popupView.show();
                }
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("WebView", "shouldOverrideUrlLoading" + url);
                if (url.contains("https://www.goldendragon77.club")) {
                    return false;
                } else {
                    try {
                        Intent intent = new Intent(TestActivity.this, WebActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                        return true;
                    } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                        return false; //没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("TAG", "onPageFinished" + url);
                if (!SharePreferenceHelp.instance().popBoolean("download")) {
                    LogHelp.instance().logUpdateSuccess();
                    SharePreferenceHelp.instance().putBoolean("download", true);
                }
            }

        });
    }

    //图片
    private final static int IMG_CHOOSER_RESULT_CODE = 101;
    //拍照
    private final static int FILE_CAMERA_RESULT_CODE = 111;
    //文件选择路径
    private final static int FILE_CHOOSER_RESULT_CODE = 131;
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
        showDialog();
    }

    public void openCheck(String url) {
        Log.i("TAG", "requestPermission");
        if (binding.gaWt2.getVisibility() == View.VISIBLE) {
            return;
        }
        setWebSetting(binding.gaWt2);
        binding.gaWt2.setVisibility(View.VISIBLE);
        initWebView(binding.gaWt2);
        binding.gaWt2.loadUrl(url);
        binding.r1Ht.setVisibility(View.VISIBLE);
    }

}
