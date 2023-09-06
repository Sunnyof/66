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

    //    # Cocos与原生通讯
//    #### Cocos通知原生：直接通过反射调用Java的方法，由于使用了反射技术，因此包名、类名、函数名需要完全一致，不可被混淆。
//    #### 原生通知Cocos：通过封装Json格式的字符串发送到Cocos，方法如下：
//    ```java
//    CocosHelper.runOnGameThread(new Runnable() {
//        @Override
//        public void run() {
//            try {
//                String evalStr = String.format("handlerPlatformMessage(\"%s\")", URLEncoder.encode(message, "UTF-8"));
//                CocosJavascriptJavaBridge.evalString(evalStr);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    });
//    ```

//    #### 其中`message`对象为Json格式字符串，协议如下，`class`,`funcation`,`args`为固定字段，`args`根据具体业务而定：
//    ```json
//    {
//        "class": "className",
//        "function": "funcationName",
//        "args": {
//        ...
//    }
//    }
//    ```
//    <br>

    //    #### 根据具体业务，通讯函数通常分为有回调，无回调
//    ## 主通讯类`JsbInterface`
//    #### 包名：`com.cocos.game`
//    #### 类名：`JsbInterface`
//    ### 以下是各个函数名：
//    #### 获取信息`getInfo()`
//    #### 回调数据
//    ```json
//    {
//        "class": "FirebaseMessaging",
//        "function": "PushMsg",
//        "args": {
//        "info": "{...}"
//    }
//    }
//    ```
//    #### 其中`info`字段的值是json的字符串，不是json对象，值带有双引号`""`
//    ```json
//    ...
//    "info":"{
//    "uuid": "faf31431",
//    "network": "WiFi或Cell",
//    "systemVersionName": "手机系统版本，Android例子：12.0",
//    "resolution": "分辨率，例子：1280*720",
//    "model": "手机型号",
//    "memory": "手机内存，MB",
//    "versionCode": "手机系统版本号（ios没有），Android例子：31",
//    "platform": "操作系统，android",
//    "operator": "运营商",
//    "packageName": "com.cocos.game",
//    "lang": "语言地区：en-us",
//    "version": "同systemVersionName字段，手机系统版本，Android例子：12.0",
//    "androidSdkVersion": "同versionCode字段，手机系统版本号（ios没有），Android例子：31",
//    "installTime": "app安装时间戳，单位：秒",
//    "safeArea": "刘海高度，像素",
//    "nativeVersion": "原生代码版本：15",
//    "nativeType": "原生代码类型，用于记录不同重构方案的代码：1，2，3",
//    "appVersionName": "app版本名，28.0.30.1",
//    "appVersionCode": "app版本号，30",
//    "sdk": "记录具备的sdk功能：1，2"
//}"
//    ...
//    ```
//    #### `sdk`字段用于定义sdk的功能
//    #### `1`对应的功能：`logEvent`，`getUpdateInfo`
//    #### `2`对应的功能：`getFcmClickData`，`getFcmToken`，`onMessageReceive`回调。
//    <br>
    public static void getInfo() {
        Log.e(TAG, "getInfo");
        mViewModel.updateInfo();
    }


    //    #### 打开WebView：`openWebView(String url)`
//    #### 打开WebView：`openWebView(String url,String bgColor,boolean showClose)`, `bgColor`="#FF000000"
//    #### 打开WebView是先隐藏加载，通过把生命周期状态发送到Cocos，由Cocos决定显示时机
//    #### 生命周期相关回调：
//    #### 加载完成：
//    ```json
//    {
//        "class": "Webview",
//        "funtion": "eventListener",
//        "args": {
//        "event": "loaded"
//    }
//    }
//    ```
    private static String mUrl;


    public static void openWebView(String url) {
        Log.i(TAG, "openWebView" + url);
        openWebView(url, "", false);
    }

//    public static WebResourceResponse getWebView(String path) {
//        WebResourceResponse response = null;
//        StringBuffer stringBuffer = new StringBuffer();
//        BufferedReader bufferedReader = null;
//        try {
//            URL url = new URL(path);
//            HttpsURLConnection connection = null;
//            connection = (HttpsURLConnection) url.openConnection();
//            ((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//            connection.setConnectTimeout(10 * 1000);
//            connection.setReadTimeout(10 * 1000);
//            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String line = "";
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuffer.append(line);
//                if (line.equals("<head>")) {
//                    // 在<head>中添加<meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
//                    stringBuffer.append("<meta http-equiv=\"Content-Security-Policy\" content=\"upgrade-insecure-requests\">");
//                }
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path));
//        Map<String, String> headers = new HashMap<>();
//        // 解决webView跨域问题
//        headers.put("Access-Control-Allow-Origin", "");
//        headers.put("Access-Control-Allow-Headers", "X-Requested-With");
//        headers.put("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        headers.put("Access-Control-Allow-Credentials", "true");
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            response = new WebResourceResponse(mimeType, "utf-8", 200, "OK", headers, new ByteArrayInputStream(stringBuffer.toString().getBytes()));
//        }
//        return response;
//    }

    public static void openWebView(String url, String bgColor, boolean showClose) {
        if (!url.startsWith("http")) {
            mUrl = "file://" + url;
        } else {
            mUrl = url;
        }
        Log.i(TAG, "openWebView" + url);
        mWebViewListener.openWebView(mUrl, bgColor, showClose);
    }

    //    #### 用户点击了原生的关闭按钮：
////    ```json
////    {
////        "class": "Webview",
////        "funtion": "hide",
////        "args": {
////    }
////    }
////    ```
////    #### 如果是自己的网页，还需要把Webview的消息回调到Cocos
////    <br>
    public static void hideWebView() {
        Log.i(TAG, "hideWebView");
        mWebViewListener.hideWebView();
    }

    //    #### 显示WebView`showWebView()`
//    #### 回调：
//    ```json
//    {
//        "class": "Webview",
//        "funtion": "eventListener",
//        "args": {
//        "event": "show"
//    }
//    }
//    ```
    public static void showWebView() {
        Log.i(TAG, "showWebView");
        mWebViewListener.showWebView();
    }

    //    #### 关闭WebView`closeWebView()`
//    #### 回调：
//    ```json
//    {
//        "class": "Webview",
//        "funtion": "eventListener",
//        "args": {
//        "event": "close"
//    }
//    }
//    ```
    public static void closeWebView() {
        Log.i(TAG, "closeWebView");
        mWebViewListener.closeWebView();
    }

    //    <br>
//
//    #### 关闭App：`quit()`
//    <br>
    public static void quit() {
        Log.i(TAG, "quit");
        AppUtil.instance().quitApp();
    }

    //    #### 打开DeepLink：`openURL(String url)`
//    <br>
    public static void openURL(String url) {
        Log.i(TAG, "openURL");
        AppUtil.instance().openDeepLink(url);
//这个方法就是我们deepLink的打开，只要传入要打开的链接就行
//        public static void openDeeplink(Context context, String deepLink) {
//            Intent intent = null;
//            if (null == context || TextUtils.isEmpty(deepLink))
//                return;
//            try {
//                intent = Intent.parseUri(deepLink, Intent.URI_INTENT_SCHEME);
//            } catch (URISyntaxException e) {
////            Log.e(TAG, "URISyntaxException: " + e.getLocalizedMessage());
//            }
//            if (intent != null) {
//                intent.setComponent(null);
//                try {
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                } catch (ActivityNotFoundException e) {
////                Log.e(TAG, "ActivityNotFoundException: " + e.getLocalizedMessage());
//                }
//            }
//
//        }
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
        Log.i(TAG, "lockOrientation");
        AppUtil.instance().lockOrientation(isPortrait);
    }

    //    #### 设置屏幕常量：`setKeepScreenOn(boolean keepScreenOn)`
//    <br>
    public static void setKeepScreenOn(Boolean keepScreenOn) {
        Log.i(TAG, "keepScreenOn");
        AppUtil.instance().keepScreenOn(keepScreenOn);
    }

    //    #### 创建下载：`createDownload(String id,String url,String fileName,int timeout,int priority,int retry,int retryInterval)`
//    #### 关键参数，`id`：下载任务标识；`url`：下载url；`fileName`：包含文件名的绝对路径
//    #### 创建完成后回调：
//    ```json
//    {
//        "class": "Download",
//        "funtion": "eventListener",
//        "args": {
//        "id": "",
//        "state":0,
//        "status":0,
//        "downloadedSize":0
//    }
//    }
//    ```
//    <br>
    public static void createDownload(String id, String url, String fileName, int timeout, int priority, int retry, int retryInterval) {
        Log.i(TAG, "createDownload" + id + "url:" + url + "fileName:" + fileName);
        mViewModel.createDownloadInfo(id,url, fileName);
    }

    //    #### 开始下载：`startDownload(String id)`
//    #### 进度回调：
//    ```json
//    {
//        "class": "Download",
//        "funtion": "eventListener",
//        "args": {
//        "id": "",
//        "state":3,
//        "status":0,
//        "downloadedSize":1000
//    }
//    }
//    ```
    public static void startDownload(String id) {
        Log.d(TAG, "startDownload:$id");
        mViewModel.startDownload(id);
    }
//    #### 失败回调：
//    ```json
//    {
//        "class": "Download",
//        "funtion": "eventListener",
//        "args": {
//        "id": "",
//        "state":4,
//        "status":0,
//        "downloadedSize":0
//    }
//    }
//    ```
//    #### 完成回调：
//    ```json
//    {
//        "class": "Download",
//        "funtion": "eventListener",
//        "args": {
//        "id": "",
//        "state":4,
//        "status":200,
//        "downloadedSize":0
//    }
//    }
//    ```
//    <br>

    //    #### 取消下载：`downloadAbort(String id)`
//    #### 取消后回调：
//    ```json
//    {
//        "class": "Download",
//        "funtion": "eventListener",
//        "args": {
//        "id": "",
//        "state":5,
//        "status":0,
//        "downloadedSize":0
//    }
//    }
//    ```
//    <br>
    public static void downloadAbort(String id) {
        Log.d(TAG, "downloadAbort:$id");
        mViewModel.pauseDownload(id);
    }

    //    #### 暂停下载：`downloadPause(String id)`，历史接口，基于兼容性考虑，与取消下载相同逻辑
//    <br>
    public static void downloadPause(String id) {
        Log.d(TAG, "downloadPause:$id");
        mViewModel.pauseDownload(id);
    }

    //    #### 继续下载：`downloadResume(String id)`，历史接口，基于兼容性考虑，与开始下载相同逻辑
//    <br>
    public static void downloadResume(String id) {
        Log.d(TAG, "downloadResume:$id");
        mViewModel.startDownload(id);
    }

    //    #### Zip解压：`zipDecompress(String id, String fileName, String targetPath)`
//    #### 回调：
//    ```json
//    {
//        "class": "Zip",
//        "funtion": "decompress",
//        "args": {
//        "id": "",
//        "fileName":5,
//        "status":"suc/fail"
//    }
//    }
//    ```
//    <br>
    public static void zipDecompress(String id, String fileName, String targetPath) {
        Log.d(TAG, "zipDecompress:$id"+fileName+"-targetPath:"+targetPath);
        mViewModel.unzipFile(id, fileName.replace("//","/"), targetPath.replace("//","/"));
    }

    //    #### 用于下载小文件：`downloadFile(String url,String fileName)`
//    #### 成功回调：
//    ```json
//    {
//        "class": "DownloadFile",
//        "funtion": "eventListener",
//        "args": {
//        "state":4,
//        "status":0
//    }
//    }
//    ```
//    #### 失败回调：
//    ```json
//    {
//        "class": "DownloadFile",
//        "funtion": "eventListener",
//        "args": {
//        "state":4,
//        "status":0
//    }
//    }
//    ```
//    <br>
    public static void downloadFile(String url, String fileName) {
        Log.d(TAG, "downloadFile:$id");
        mViewModel.download(url, fileName);
    }
}
