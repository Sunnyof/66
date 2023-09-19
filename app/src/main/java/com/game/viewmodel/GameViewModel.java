package com.game.viewmodel;

import static com.game.BaseConstant.game_ca_beo66_ARGS;
import static com.game.BaseConstant.game_ca_beo66_CLASS_KEY;
import static com.game.BaseConstant.game_ca_beo66_FUNCTION;

import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.cocos.lib.CocosHelper;
import com.game.BaseApplication;
import com.game.BaseConstant;
import com.cocos.lib.CocosJavascriptJavaBridge;
import com.game.net.DownloadInfo;
import com.game.net.DownloadManager;
import com.game.util.AppUtil;
import com.game.util.Base64Util;
import com.game.util.DateUtil;
import com.game.util.FileUtil;
import com.game.util.NetworkUtil;
import com.game.util.SharePreferenceHelp;
import com.game.util.ThreadUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import game.crossingthe.greattrench.BuildConfig;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class GameViewModel extends ViewModel implements JsListener {

    private Disposable disposable;

    private String TAG = this.getClass().getSimpleName();

    public ObservableField<String> name = new ObservableField<>();


    public void download(String url, String fileName) {
        if (disposable != null) {
            disposable.dispose();
        }
        try {
            info = DownloadManager.getInstance().createDownInfo(url);
            info.setId("");
            if (fileName.endsWith("zip")) {
                info.setFileName(fileName.substring(fileName.lastIndexOf("/")));
            }
            downloadFileCallback(4, 0);
            Log.i(TAG, fileName + "-" + info.getFileName());
            DownloadManager.getInstance().setFilePath(fileName.substring(0, fileName.lastIndexOf("/")));
            DownloadManager.getInstance().download(info, url, new Observer<DownloadInfo>() {
                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(DownloadInfo downloadInfo) {

                }

                @Override
                public void onError(Throwable e) {
                    EventBus.getDefault().post("showDialog");
                }

                @Override
                public void onComplete() {
                    downloadFileCallback(4, 200);
                }
            });
        } catch (Exception e) {
            EventBus.getDefault().post("showDialog");
        }

    }


    public void download(String id) {
        if (disposable != null) {
            disposable.dispose();
        }
        try {
            DownloadManager.getInstance().download(info, info.getUrl(), new Observer<DownloadInfo>() {
                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(DownloadInfo downloadInfo) {
                    Log.i(TAG, "downloadInfo:" + info.getFileName());
                    downloadCallback(id, 3, 200, downloadInfo.getProgress());
                }

                @Override
                public void onError(Throwable e) {
                    Log.i(TAG, e.getMessage() + info.getFileName());
                    downloadCallback(id, 4, 0, 0);
                    EventBus.getDefault().post("showDialog");
                }

                @Override
                public void onComplete() {
                    downloadCallback(id, 4, 200, info.getProgress());
                }
            });
        } catch (Exception e) {
            downloadCallback(id, 4, 0, 0);
            DownloadManager.getInstance().pauseDownload(info.getUrl());
            EventBus.getDefault().post("showDialog" + e.getMessage());
        }

    }

    private DownloadInfo info;

    public void createDownloadInfo(String id, String url, String fileName) {
        downloadCallback(id, 0, 0, 0);
        try {
            info = DownloadManager.getInstance().createDownInfo(url);
            info.setId(id);
            if (fileName.endsWith("zip")) {
                info.setFileName(fileName.substring(fileName.lastIndexOf("/")));
            }
            Log.i(TAG, fileName + "-" + info.getFileName());
            DownloadManager.getInstance().setFilePath(fileName.substring(0, fileName.lastIndexOf("/")));
        } catch (Exception e) {
            EventBus.getDefault().post("showDialog");
        }
    }

    public void unzipFile(String id, String dataDir, String outFileDir) {
        try {
            FileUtil.unzip(dataDir, outFileDir, new FileUtil.ZipProgress() {
                @Override
                public void onProgress(int progress) {

                }

                @Override
                public void onDone() {
                    zipCallback(id, dataDir, "suc");
                }

                @Override
                public void failed() {
                    zipCallback(id, dataDir, "failed");
                    EventBus.getDefault().post("showDialog");
                }
            });
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            zipCallback(id, dataDir, "failed");
            EventBus.getDefault().post("showDialog");
        }
    }

    public void startDownload(String id) {
        ThreadUtil.executeThread(() -> {
            download(id);
        });
    }

    public void pauseDownload(String id) {
        downloadCallback(id, 5, 0, 0);
        disposable.dispose();
        DownloadManager.getInstance().pauseDownload(info.getUrl());
    }

    @Override
    public void callUpdateInfoCallback() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(game_ca_beo66_CLASS_KEY, Base64Util.decode("U0RLTG9n"));//SDKLog
            jsonObject.put(game_ca_beo66_FUNCTION, Base64Util.decode("Z2V0VXBkYXRlSW5mbw=="));
            JSONObject argObject = new JSONObject();
            argObject.put("code", SharePreferenceHelp.instance().popString("code"));
            argObject.put("domian", BaseConstant.game_ca_beo66_BASE_URL);
            argObject.put("channelId", SharePreferenceHelp.instance().popString("channelId"));
            argObject.put("vestId", BaseConstant.game_ca_beo66_PLATFORM_ID);
            argObject.put("updateTimestamp", DateUtil.instance().getTime(BaseConstant.game_ca_beo66_UPDATE_TIME));
            argObject.put("installReferrer", SharePreferenceHelp.instance().popString("installReferrer"));
            jsonObject.put(game_ca_beo66_ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }

    }

    @Override
    public void callFcmTokenCallBack() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(game_ca_beo66_CLASS_KEY, Base64Util.decode("U0RLTG9n"));
            jsonObject.put(game_ca_beo66_FUNCTION, "getFcmToken");
            JSONObject argObject = new JSONObject();
            argObject.put("token", SharePreferenceHelp.instance().popString("fcmToken"));
            jsonObject.put(game_ca_beo66_ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }
    }

    @Override
    public void callFcmCustomDataCallBack() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(game_ca_beo66_CLASS_KEY, "FirebaseMessaging");
            jsonObject.put(game_ca_beo66_FUNCTION, "PushMsg");
            JSONObject argObject = new JSONObject(SharePreferenceHelp.instance().popString("fcmData"));
            jsonObject.put(game_ca_beo66_ARGS, argObject);
//          Log.i(TAG, jsonObject.toString());
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }
    }

    @Override
    public void callLoadWebViewCallBack() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(game_ca_beo66_CLASS_KEY, Base64Util.decode("V2Vidmlldw=="));
            jsonObject.put(game_ca_beo66_FUNCTION, Base64Util.decode("ZXZlbnRMaXN0ZW5lcg=="));
            JSONObject argObject = new JSONObject();
            argObject.put("event", "loaded");
            jsonObject.put(game_ca_beo66_ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }
    }

    @Override
    public void callHideWebViewCallBack() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(game_ca_beo66_CLASS_KEY, Base64Util.decode("V2Vidmlldw=="));
            jsonObject.put(game_ca_beo66_FUNCTION, "hide");
            JSONObject argObject = new JSONObject();
            jsonObject.put(game_ca_beo66_ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }
    }

    @Override
    public void callShowWebViewCallBack() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(game_ca_beo66_CLASS_KEY,  Base64Util.decode("V2Vidmlldw=="));//Webview
            jsonObject.put(game_ca_beo66_FUNCTION, Base64Util.decode("ZXZlbnRMaXN0ZW5lcg=="));
            JSONObject argObject = new JSONObject();
            argObject.put("event", "show");
            jsonObject.put(game_ca_beo66_ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }
    }

    @Override
    public void callCloseWebViewCallBack() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(game_ca_beo66_CLASS_KEY, Base64Util.decode("V2Vidmlldw=="));
            jsonObject.put(game_ca_beo66_FUNCTION, Base64Util.decode("ZXZlbnRMaXN0ZW5lcg=="));
            JSONObject argObject = new JSONObject();
            argObject.put("event", "close");
            jsonObject.put(game_ca_beo66_ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }
    }

    public void updateInfo() {
        Log.i(TAG, "updateInfo");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(game_ca_beo66_CLASS_KEY, "Device");
            jsonObject.put(game_ca_beo66_FUNCTION, "info");
            JSONObject infoObject = new JSONObject();
            infoObject.put("androidSdkVersion", Build.VERSION.SDK_INT);
            infoObject.put("appVersionCode", BuildConfig.VERSION_CODE);
            infoObject.put("appVersionName", BuildConfig.VERSION_NAME);
            infoObject.put("installTime", AppUtil.instance().getInstallTime());
            infoObject.put("lang", AppUtil.instance().getLang());
            infoObject.put("memory", AppUtil.instance().getMemory());
            infoObject.put("model", Build.MODEL);
            infoObject.put("nativeVersion", 15);
            infoObject.put("nativeType", 3);
            infoObject.put("network", NetworkUtil.getNetWorkType());
            infoObject.put("operator", AppUtil.instance().getOS());
            infoObject.put("packageName", BuildConfig.APPLICATION_ID);
            infoObject.put("platform", "android");
            infoObject.put("code", SharePreferenceHelp.instance().popString("gameCode"));
            infoObject.put("agentCode", SharePreferenceHelp.instance().popString("gameCode"));
            infoObject.put("resolution", AppUtil.instance().getResolution());
            infoObject.put("safeArea", AppUtil.instance().getNotchHeight());
            infoObject.put("sdk", "1,2");
            infoObject.put("systemVersionName", Build.VERSION.RELEASE);
            infoObject.put("uuid", Settings.Secure.getString(BaseApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID));
            infoObject.put("version", Build.VERSION.RELEASE);
            infoObject.put("versionCode", Build.VERSION.SDK_INT);
            JSONObject argsObject = new JSONObject();
            argsObject.put("info", infoObject.toString());
            jsonObject.put(game_ca_beo66_ARGS, argsObject);
            Log.i(TAG, jsonObject.toString());
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }

    }

    /**
     * 创建任务的ID
     * state 下载状态 0 准备现在 3开始下载 4下载完成/失败 5下载取消
     * status 任务状态 成功是200 准备是 0 失败取网络回调值或者0
     */
    private void downloadCallback(String id, int state, int status, long downloadSize) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(game_ca_beo66_CLASS_KEY, "Download");
            jsonObject.put(game_ca_beo66_FUNCTION, Base64Util.decode("ZXZlbnRMaXN0ZW5lcg=="));
            JSONObject argObject = new JSONObject();
            argObject.put("id", id);
            argObject.put("state", state);
            argObject.put("status", status);
            argObject.put("downloadedSize", downloadSize);
            jsonObject.put(game_ca_beo66_ARGS, argObject);
            Log.i(TAG, jsonObject.toString());
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }

    }

    /**
     * 解压回调
     * fileName解压文件的目录
     * status 成功suc 失败fail
     */
    private void zipCallback(String id, String fileName, String status) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(game_ca_beo66_CLASS_KEY, "Zip");
            jsonObject.put(game_ca_beo66_FUNCTION, "decompress");
            JSONObject argObject = new JSONObject();
            argObject.put("id", id);
            argObject.put("status", status);
            argObject.put("fileName", fileName);
            jsonObject.put(game_ca_beo66_ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }
    }


    private void downloadFileCallback(int state, int status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(game_ca_beo66_CLASS_KEY, Base64Util.decode("RG93bmxvYWRGaWxl"));
            jsonObject.put(game_ca_beo66_FUNCTION, Base64Util.decode("ZXZlbnRMaXN0ZW5lcg=="));
            JSONObject argObject = new JSONObject();
            argObject.put("state", state);
            argObject.put("status", status);
            jsonObject.put(game_ca_beo66_ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void java2Cocos(String message) {
        CocosHelper.runOnGameThread(() -> {
            try {
                String evalStr = String.format("handlerPlatformMessage(\"%s\")", URLEncoder.encode(message, "UTF-8"));
                CocosJavascriptJavaBridge.evalString(evalStr);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
