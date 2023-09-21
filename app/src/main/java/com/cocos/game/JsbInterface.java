package com.cocos.game;


import android.util.Log;


import com.game.util.AppUtil;
import com.game.viewmodel.GameViewModel;
import com.game.viewmodel.WebViewListener;


public class JsbInterface {
    private static GameViewModel mViewModel;

    private static String TAG = JsbInterface.class.getName();

    private static WebViewListener mWebViewListener;


    public void initContext(GameViewModel viewModel) {
        mViewModel = viewModel;
    }

    public void setWebViewListener(WebViewListener webViewListener) {
        mWebViewListener = webViewListener;
    }

    public static void getInfo() {
        Log.e(TAG, "getInfo");
        mViewModel.updateInfo();
    }

    private static String mUrl;


    public static void openWebView(String url) {
        Log.i(TAG, "openWebView" + url);
        openWebView(url, "", false);
    }

    public static void openWebView(String url, String bgColor, boolean showClose) {
        if (!url.startsWith("http")) {
            mUrl = "file://" + url;
        } else {
            mUrl = url;
        }
        Log.i(TAG, "openWebView" + url);
        mWebViewListener.openWebView(mUrl, bgColor, showClose);
    }

    public static void hideWebView() {
        Log.i(TAG, "hideWebView");
        mWebViewListener.hideWebView();
    }

    public static void showWebView() {
        Log.i(TAG, "showWebView");
        mWebViewListener.showWebView();
    }

    public static void closeWebView() {
        Log.i(TAG, "closeWebView");
        mWebViewListener.closeWebView();
    }

    public static void quit() {
        Log.i(TAG, "quit");
        AppUtil.instance().quitApp();
    }

    public static void openURL(String url) {
        Log.i(TAG, "openURL");
        AppUtil.instance().openDeepLink(url);
    }

    //    #### 复制到剪切板：`setClipboard(String str)`
//    <br>
    public static void setClipboard(String str) {
        Log.e(TAG, str);
        AppUtil.instance().clip2Cocos(str);
    }

    //    #### 设置竖屏：`lockOrientation(boolean isPortrait)`
//    <br>
    public static void lockOrientation(boolean isPortrait) {
        Log.i(TAG, "lockOrientation"+isPortrait);
        AppUtil.instance().lockOrientation(isPortrait);
    }

    //    #### 设置屏幕常量：`setKeepScreenOn(boolean keepScreenOn)`
//    <br>
    public static void setKeepScreenOn(Boolean keepScreenOn) {
        Log.i(TAG, "keepScreenOn");
        AppUtil.instance().keepScreenOn(keepScreenOn);
    }

    public static void createDownload(String id, String url, String fileName, int timeout, int priority, int retry, int retryInterval) {
        Log.i(TAG, "createDownload" + id + "url:" + url + "fileName:" + fileName);
        mViewModel.createDownloadInfo(id,url, fileName);
    }

    public static void startDownload(String id) {
        Log.d(TAG, "startDownload:$id");
        mViewModel.startDownload(id);
    }

    public static void downloadAbort(String id) {
        Log.d(TAG, "downloadAbort:$id");
        mViewModel.pauseDownload(id);
    }

    public static void downloadPause(String id) {
        Log.d(TAG, "downloadPause:$id");
        mViewModel.pauseDownload(id);
    }

    public static void downloadResume(String id) {
        Log.d(TAG, "downloadResume:$id");
        mViewModel.startDownload(id);
    }

    public static void zipDecompress(String id, String fileName, String targetPath) {
        Log.d(TAG, "zipDecompress:$id"+fileName+"-targetPath:"+targetPath);
        mViewModel.unzipFile(id, fileName.replace("//","/"), targetPath.replace("//","/"));
    }

    public static void downloadFile(String url, String fileName) {
        Log.d(TAG, "downloadFile:$id");
        mViewModel.download(url, fileName);
    }
}
