package com.sun.m;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.cocos.game.GameJScript;
import com.cocos.lib.CocosReflectionHelper;
import com.cocos.lib.GlobalObject;

import java.lang.reflect.Field;

import game.crossingthe.greattrench.R;
import game.crossingthe.greattrench.databinding.Sgacmvq888Binding;

public class mcwxt406 extends AppCompatActivity {

    private Sgacmvq888Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setTheme( com.cocos.lib.R.style.Theme_AppCompat_Light_NoActionBar);
        GlobalObject.setActivity(this);
        setImmersiveMode();
        hideVirtualButton();
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.sgacmvq888);
        setWebSetting();
        initWebView(binding.web);
        binding.web.loadUrl("http://localhost:8080/index.html");

    }

    private void setImmersiveMode() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        try {
            Field field = lp.getClass().getField("layoutInDisplayCutoutMode");
            Field constValue = lp.getClass().getDeclaredField("LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES");
            field.setInt(lp, constValue.getInt(null));

            // https://developer.android.com/training/system-ui/immersive
            int flag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            flag |= View.class.getDeclaredField("SYSTEM_UI_FLAG_IMMERSIVE_STICKY").getInt(null);
            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(flag);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void hideVirtualButton() {
        if (Build.VERSION.SDK_INT >= 19 &&
                null != GlobalObject.getActivity()) {
            // use reflection to remove dependence of API level

            Class viewClass = View.class;
            final int SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION = CocosReflectionHelper.<Integer>getConstantValue(viewClass, "SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION");
            final int SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN = CocosReflectionHelper.<Integer>getConstantValue(viewClass, "SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN");
            final int SYSTEM_UI_FLAG_HIDE_NAVIGATION = CocosReflectionHelper.<Integer>getConstantValue(viewClass, "SYSTEM_UI_FLAG_HIDE_NAVIGATION");
            final int SYSTEM_UI_FLAG_FULLSCREEN = CocosReflectionHelper.<Integer>getConstantValue(viewClass, "SYSTEM_UI_FLAG_FULLSCREEN");
            final int SYSTEM_UI_FLAG_IMMERSIVE_STICKY = CocosReflectionHelper.<Integer>getConstantValue(viewClass, "SYSTEM_UI_FLAG_IMMERSIVE_STICKY");
            final int SYSTEM_UI_FLAG_LAYOUT_STABLE = CocosReflectionHelper.<Integer>getConstantValue(viewClass, "SYSTEM_UI_FLAG_LAYOUT_STABLE");

            // getWindow().getDecorView().setSystemUiVisibility();
            final Object[] parameters = new Object[]{SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | SYSTEM_UI_FLAG_IMMERSIVE_STICKY};
            CocosReflectionHelper.<Void>invokeInstanceMethod(GlobalObject.getActivity().getWindow().getDecorView(),
                    "setSystemUiVisibility",
                    new Class[]{Integer.TYPE},
                    parameters);
        }
    }
    public void setWebSetting() {
        WebSettings settings = binding.web.getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);  //设置是否运行网上的js代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
        }
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
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
        settings.setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//允许js弹框
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setAllowContentAccess(true);

    }
    public void initWebView(WebView webView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        webView.addJavascriptInterface(new GameJScript(), "android");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Log.i("TAG", "onShowFileChooser");

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
                Log.e("TAG", "onPageFinished"+url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("TAG", "onPageStarted"+url);
            }
        });
    }
}