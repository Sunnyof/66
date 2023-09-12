package com.sun.u;

import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewHelper {

    public void initWebSetting(WebView webView){
        WebSettings settings = webView.getSettings();
        Log.e("TAG", "setWebSetting");
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);  //设置是否运行网上的js代码
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setSupportZoom(true);    //支持缩放
        settings.setSupportMultipleWindows(true);
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
    }
}
