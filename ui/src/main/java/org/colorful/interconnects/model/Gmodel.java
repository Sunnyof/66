package org.colorful.interconnects.model;

import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.cocos.lib.CocosHelper;
import com.cocos.lib.CocosAppUtil;
import com.cocos.lib.CocosBase64Util;

import org.colorful.interconnects.BaseApplication;
import org.colorful.interconnects.value.FileUtil;

import com.cocos.lib.CocosNetworkUtil;

import org.colorful.interconnects.value.SPHelp;

import com.cocos.lib.CocosThreadUtil;
import com.cocos.lib.CocosDownloadInfo;
import com.cocos.lib.CocosDownloadManager;
import com.cocos.lib.Downloader;
import com.cocos.lib.exception.DownloadError;
import com.cocos.lib.utils.IListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Gmodel extends ViewModel implements JsListener {

    private String CLASS_KEY = CocosBase64Util.decode("Y2xhc3M=");
    private String ARGS = CocosBase64Util.decode("YXJncw==");
    private String FUNCTION = CocosBase64Util.decode("ZnVuY3Rpb24=");
    private String BASE_URL = "https://www.tksnth.xyz/";
    private Disposable disposable;
    private String TAG = this.getClass().getSimpleName();

    public ObservableField<String> name = new ObservableField<>();


    public void download(String url, String fileName) {
        if (disposable != null) {
            disposable.dispose();
        }

        try {
            info = CocosDownloadManager.getInstance().createDownInfo(url);
            info.setId("");
            if (fileName.endsWith("zip")) {
                info.setFileName(fileName.substring(fileName.lastIndexOf("/")));
            }
            downloadFileCallback(4, 0);
            Log.i(TAG, fileName + "-" + info.getFileName() + "-" + Thread.currentThread().getName());
            CocosDownloadManager.getInstance().setFilePath(fileName.substring(0, fileName.lastIndexOf("/")));
            CocosDownloadManager.getInstance().download(info, url, new Observer<CocosDownloadInfo>() {
                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(CocosDownloadInfo downloadInfo) {

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
            CocosDownloadManager.getInstance().download(info, info.getUrl(), new Observer<CocosDownloadInfo>() {
                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(CocosDownloadInfo downloadInfo) {
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
            CocosDownloadManager.getInstance().pauseDownload(info.getUrl());
            EventBus.getDefault().post("showDialog" + e.getMessage());
        }

    }

    private CocosDownloadInfo info;

    public void createDownloadInfo(String id, String url, String fileName) {
        downloadCallback(id, 0, 0, 0);
        Log.i("TAG","createDownloadInfo");
        String filePath = Environment.getDataDirectory() + "/data/" + "game.Colorful.interconnects" + "/files/org/data.zip";
        Downloader.addTask(new File(filePath), url, new IListener() {
            @Override
            public void onPreExecute(long fileSize) {

            }

            @Override
            public void onProgressChange(long fileSize, long downloadedSize) {
                Log.i("TAG","createDownloadInfo:"+fileSize+"-"+downloadedSize);
            }

            @Override
            public void onProgressChange(long fileSize, long downloadedSize, long speed) {
                Log.i("TAG","createDownloadInfo:"+fileSize+"-"+downloadedSize+"-"+speed);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(DownloadError error) {
                Log.i("TAG","createDownloadInfo onError:"+error.getMessage());
            }

            @Override
            public void onSuccess() {

            }
        });

//        try {
//            info = CocosDownloadManager.getInstance().createDownInfo(url);
//            info.setId(id);
//            if (fileName.endsWith("zip")) {
//                info.setFileName(fileName.substring(fileName.lastIndexOf("/")));
//            }
//            Log.i(TAG, fileName + "-" + info.getFileName());
//            CocosDownloadManager.getInstance().setFilePath(fileName.substring(0, fileName.lastIndexOf("/")));
//        } catch (Exception e) {
//            EventBus.getDefault().post("showDialog");
//        }
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
        CocosThreadUtil.executeThread(() -> {
            download(id);
        });
    }

    public void pauseDownload(String id) {
        downloadCallback(id, 5, 0, 0);
        disposable.dispose();
        CocosDownloadManager.getInstance().pauseDownload(info.getUrl());
    }

    @Override
    public void callUpdateInfoCallback() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CLASS_KEY, CocosBase64Util.decode("U0RLTG9n"));//SDKLog
            jsonObject.put(FUNCTION, CocosBase64Util.decode("Z2V0VXBkYXRlSW5mbw=="));
            JSONObject argObject = new JSONObject();
            argObject.put("code", SPHelp.instance().popString("gameCode"));
            argObject.put("domian", BASE_URL);
            argObject.put("channelId", SPHelp.instance().popString("channelId"));
            argObject.put("vestId", "620114");
            argObject.put("updateTimestamp", "1698422691");
            argObject.put("installReferrer", SPHelp.instance().popString("installReferrer"));
            jsonObject.put(ARGS, argObject);
            Log.i(TAG, jsonObject.toString());
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }

    }

    @Override
    public void callLoadWebViewCallBack() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CLASS_KEY, CocosBase64Util.decode("V2Vidmlldw=="));
            jsonObject.put(FUNCTION, CocosBase64Util.decode("ZXZlbnRMaXN0ZW5lcg=="));
            JSONObject argObject = new JSONObject();
            argObject.put("event", "loaded");
            jsonObject.put(ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }
    }

    @Override
    public void callHideWebViewCallBack() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CLASS_KEY, CocosBase64Util.decode("V2Vidmlldw=="));
            jsonObject.put(FUNCTION, "hide");
            JSONObject argObject = new JSONObject();
            jsonObject.put(ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }
    }

    @Override
    public void callShowWebViewCallBack() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CLASS_KEY, CocosBase64Util.decode("V2Vidmlldw=="));//Webview
            jsonObject.put(FUNCTION, CocosBase64Util.decode("ZXZlbnRMaXN0ZW5lcg=="));
            JSONObject argObject = new JSONObject();
            argObject.put("event", "show");
            jsonObject.put(ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }
    }

    @Override
    public void callCloseWebViewCallBack() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CLASS_KEY, CocosBase64Util.decode("V2Vidmlldw=="));
            jsonObject.put(FUNCTION, CocosBase64Util.decode("ZXZlbnRMaXN0ZW5lcg=="));
            JSONObject argObject = new JSONObject();
            argObject.put("event", "close");
            jsonObject.put(ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }
    }

    public void updateInfo() {
        Log.i(TAG, "updateInfo");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CLASS_KEY, "Device");
            jsonObject.put(FUNCTION, "info");
            JSONObject infoObject = new JSONObject();
            infoObject.put("androidSdkVersion", Build.VERSION.SDK_INT);
            infoObject.put("appVersionCode", 43);
            infoObject.put("appVersionName", "1.0.3");
            infoObject.put("installTime", CocosAppUtil.instance().getInstallTime());
            infoObject.put("lang", CocosAppUtil.instance().getLang());
            infoObject.put("memory", CocosAppUtil.instance().getMemory());
            infoObject.put("model", Build.MODEL);
            infoObject.put("nativeVersion", 15);
            infoObject.put("nativeType", 3);
            infoObject.put("network", CocosNetworkUtil.getNetWorkType());
            infoObject.put("operator", CocosAppUtil.instance().getOS());
            infoObject.put("packageName", "game.Colorful.interconnects");
            infoObject.put("platform", "android");
            infoObject.put("code", SPHelp.instance().popString("gameCode"));
            infoObject.put("agentCode", SPHelp.instance().popString("gameCode"));
            infoObject.put("resolution", CocosAppUtil.instance().getResolution());
            infoObject.put("safeArea", CocosAppUtil.instance().getNotchHeight());
            infoObject.put("sdk", "1,2");
            infoObject.put("systemVersionName", Build.VERSION.RELEASE);
            infoObject.put("uuid", Settings.Secure.getString(BaseApplication.application.getContentResolver(), Settings.Secure.ANDROID_ID));
            infoObject.put("version", Build.VERSION.RELEASE);
            infoObject.put("versionCode", Build.VERSION.SDK_INT);
            JSONObject argsObject = new JSONObject();
            argsObject.put("info", infoObject.toString());
            jsonObject.put(ARGS, argsObject);
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
            jsonObject.put(CLASS_KEY, "Download");
            jsonObject.put(FUNCTION, CocosBase64Util.decode("ZXZlbnRMaXN0ZW5lcg=="));
            JSONObject argObject = new JSONObject();
            argObject.put("id", id);
            argObject.put("state", state);
            argObject.put("status", status);
            argObject.put("downloadedSize", downloadSize);
            jsonObject.put(ARGS, argObject);
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
            jsonObject.put(CLASS_KEY, "Zip");
            jsonObject.put(FUNCTION, "decompress");
            JSONObject argObject = new JSONObject();
            argObject.put("id", id);
            argObject.put("status", status);
            argObject.put("fileName", fileName);
            jsonObject.put(ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (Exception e) {

        }
    }


    private void downloadFileCallback(int state, int status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(CLASS_KEY, CocosBase64Util.decode("RG93bmxvYWRGaWxl"));
            jsonObject.put(FUNCTION, CocosBase64Util.decode("ZXZlbnRMaXN0ZW5lcg=="));
            JSONObject argObject = new JSONObject();
            argObject.put("state", state);
            argObject.put("status", status);
            jsonObject.put(ARGS, argObject);
            java2Cocos(jsonObject.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void java2Cocos(String message) {
        CocosHelper.cocos2Java(message);
    }
}
