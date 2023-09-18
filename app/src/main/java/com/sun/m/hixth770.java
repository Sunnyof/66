package com.sun.m;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.g.done.RHelp;

import com.crossingthe.greattrench.R;
import com.crossingthe.greattrench.databinding.Ihnsiu292Binding;

public class hixth770 extends Activity {
    Ihnsiu292Binding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
        View decorView = getWindow().getDecorView();
        int option =
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(option);
        binding = DataBindingUtil.setContentView(this, R.layout.ihnsiu292);
        binding.r13Ht.setVisibility(View.VISIBLE);
        String url = getIntent().getStringExtra("url");
        setWebSetting(binding.ga1Wt);

        binding.ga1Wt.setWebChromeClient(new WebChromeClient());
        binding.ga1Wt.setWebViewClient(new WebViewClient(){
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
        });
        binding.ga1Wt.loadUrl(RHelp.checkUrl( url,false,"txt"));
        binding.b33Ct.setOnClickListener(view -> hixth770.this.finish());
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