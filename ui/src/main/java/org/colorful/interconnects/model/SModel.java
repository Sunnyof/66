package org.colorful.interconnects.model;


import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cocos.lib.CocosBase64Util;
import org.colorful.interconnects.value.FileUtil;
import org.colorful.interconnects.value.EventHelp;
import org.colorful.interconnects.value.SPHelp;

import com.cocos.lib.CocosThreadUtil;
import com.cocos.lib.CocosDownloadInfo;
import com.cocos.lib.CocosDownloadManager;
import com.cocos.lib.CocosRetrofitHelp;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import kotlin.jvm.Volatile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SModel extends ViewModel {

    private String PLATFORM_ID = "620114";
    private String A_JSON_URL = CocosBase64Util.decode("YV9qc29uX3VybA==");
    private String G_JSON_URL = CocosBase64Util.decode("Z19qc29uX3VybA==");
    private final String TAG = this.getClass().getSimpleName();
    private String mChannelId = "";
    private String aJsonUrl = "";
    private String gJsonUrl = "";
    private String mUrl;
    public MutableLiveData<Integer> isSuccess = new MutableLiveData<>();
    @Volatile
    private List<Call> calls = new ArrayList<>();
    public ObservableField<CocosDownloadInfo> mDownloadInfo = new ObservableField<>();
    private Disposable mDisposable;

    private void checkData() {
        if (null != mDisposable) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    public void requestVid() {
        EventHelp.instance().fireBaseLog("requestVid", Build.ID, 200);
        checkData();
        HashMap<String, String> map = new HashMap<>();
        CocosRetrofitHelp.applyApi().requestData(CocosBase64Util.decode("aHR0cHM6Ly93d3cudGtzbnRoLnh5ei82MjAxMTQuanNvbg=="), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        aJsonUrl = jsonObject.optString(A_JSON_URL);
                        mChannelId = jsonObject.optString(CocosBase64Util.decode("Y2hhbm5lbF9pZA=="));
                        gJsonUrl = jsonObject.optString(G_JSON_URL);
                        mUrl = jsonObject.optString("v_url");
                        SPHelp.instance().pushString(CocosBase64Util.decode("Y2hhbm5lbF9pZA=="), mChannelId);
                        requestConfig();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.i("Splash", "requestVid");
                    requestFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Splash", "requestVid" + t.getMessage());
                requestFailed();
            }
        });
    }


    public void requestConfig() {
        Log.i("Splash", "checkConfig" + CocosBase64Util.decode("Y2hhbm5lbF9pZA==") + mUrl + "/api/game/getconfig");
        HashMap map = new HashMap<String, String>();
        map.put(CocosBase64Util.decode("Y2hhbm5lbF9pZA=="), mChannelId);
        map.put("vest_id", PLATFORM_ID);
        map.put("is_vest", "1");
        CocosRetrofitHelp.applyApi().requestData(mUrl + "/api/game/getconfig", map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject dataObject = jsonObject.optJSONObject("Data");
                        String code = dataObject.optString("code");
                        int vestStatus = dataObject.optInt("vestUpdateStatus");
                        SPHelp.instance().pushString("gameCode", code);
                        requestCdn1();
                        requestCdn2();
                    } catch (Exception e) {
                        Log.i("Splash", e.getMessage());
                        requestFailed();
                    }
                } else {
                    Log.i("Splash", response.message());
                    requestFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Splash", t.getMessage());
                requestFailed();
            }
        });
    }

    public void requestCdn1() {
        Log.i("Splash", "requestCdn1");
        HashMap<String, String> map = new HashMap<>();
        Call bodyCall = CocosRetrofitHelp.applyApi().requestData(aJsonUrl + "/" + mChannelId + ".json", map);
        calls.add(bodyCall);
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && !bodyCall.isCanceled()) {
                    Log.i("Splash", "requestCdn1" + bodyCall.isCanceled());
                    try {
                        calls.remove(bodyCall);
                        calls.get(0).cancel();
                        calls.clear();
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        mUrl = jsonObject.optString(mChannelId, "");

                        Log.i("Splash", "success requestCdn1" + mUrl);
                        requestPlatformConfig();
                    } catch (Exception e) {
                        Log.i("Splash", "requestCdn1" + e.getMessage());
                        checkCall(bodyCall);
                    }
                } else {
                    Log.i("Splash", "requestCdn1 response.isSuccessful()");
                    checkCall(bodyCall);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Splash", "requestCdn1    " + t.getMessage());
                checkCall(bodyCall);
            }
        });
    }

    public void requestCdn2() {
        Log.i("Splash", "requestCdn2" + gJsonUrl + "/" + mChannelId + ".json");
        HashMap<String, String> map = new HashMap<>();
        Call callTest = CocosRetrofitHelp.applyApi().requestData(gJsonUrl + "/" + mChannelId + ".json", map);
        calls.add(callTest);
        callTest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("Splash", "requestCdn2" + callTest.isCanceled());
                if (response.isSuccessful() && !callTest.isCanceled()) {
                    Log.i("Splash", "requestCdn2" + response.isSuccessful());
                    try {
                        calls.remove(callTest);
                        calls.get(0).cancel();
                        calls.clear();
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        mUrl = jsonObject.optString(mChannelId, "");
                        Log.i("Splash", "success requestCdn2" + mUrl);
                        requestPlatformConfig();
                    } catch (Exception e) {
                        Log.i("Splash", "requestCdn2" + e.getMessage());
                        checkCall(callTest);
                    }
                } else {
                    Log.i("Splash", "requestCdn2 response.isSuccessful()");
                    checkCall(callTest);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                checkCall(callTest);
            }
        });
    }

    private void checkCall(Call call) {
        if (call.isCanceled()) {
            return;
        }
        if (calls.isEmpty()) {
            return;
        }
        Log.i(TAG, "size:" + calls.size());
        calls.remove(call);
        if (calls.isEmpty()) {
            requestFailed();
        }
    }


    public void requestPlatformConfig() {
        Log.i("Splash", "requestConfig--" + mChannelId);
        HashMap map = new HashMap<String, String>();
        CocosRetrofitHelp.applyApi().requestData(mUrl + "/api/game/getconfig?channel_id=" + mChannelId + "&is_vest=0", map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject dataBean = jsonObject.optJSONObject("Data");
                        JSONObject cdnBean = dataBean.optJSONObject("domain");
                        mUrl = cdnBean.optString("cdn");
                        requestManifest();
                    } catch (Exception e) {
                        Log.i("Splash", "requestConfig" + e.getMessage());
                        requestFailed();
                    }
                } else {
                    Log.i("Splash", "requestFailed " + mUrl + "/api/game/getconfig?channel_id=" + mChannelId + "&is_vest=0");
                    requestFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Splash", "requestConfig" + t.getMessage());
                requestFailed();
            }
        });
    }

    public void requestManifest() {
        Log.i("Splash", "requestManifest");
        HashMap map = new HashMap<String, String>();
        CocosRetrofitHelp.applyApi().requestData(buildManifest(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        mUrl = jsonObject.optString("packageUrl");
                    } catch (Exception e) {
                        Log.i("Splash", "requestManifest" + e.getMessage());
                        requestFailed();
                    }
                    CocosThreadUtil.executeThread(() -> {
                        download();
                    });
                } else {
                    Log.i("Splash", "requestManifest" + buildManifest());
                    requestFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Splash", "requestManifest" + t.getMessage());
                requestFailed();
            }
        });
    }

    private void requestFailed() {
        isSuccess.postValue(-1);
        if (mDownloadInfo.get() != null)
            CocosDownloadManager.getInstance().pauseDownload(mDownloadInfo.get().getUrl());
        EventBus.getDefault().post("error");
    }

    public String buildManifest() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://");
        stringBuilder.append(mUrl);
        stringBuilder.append("/appupdate/");
        stringBuilder.append(mChannelId);
        stringBuilder.append("/android/version.manifest");
        return stringBuilder.toString();
    }

    public void download() {
        Log.i("Splash", "download" + mUrl);
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        EventHelp.instance().logUpdateStart();
        try {
            CocosDownloadInfo downloadInfo = CocosDownloadManager.getInstance().createDownInfo(mUrl + "data.zip");
            CocosDownloadManager.getInstance().download(downloadInfo, mUrl + "data.zip", new Observer<CocosDownloadInfo>() {
                @Override
                public void onSubscribe(Disposable d) {
                    mDisposable = d;
                }

                @Override
                public void onNext(CocosDownloadInfo downloadInfo) {
                    downloadInfo.setDownloadStatus(CocosDownloadInfo.DOWNLOAD);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        mDownloadInfo.set(downloadInfo);
                        mDownloadInfo.notifyChange();
                        Log.i("Splash", "downloadInfo" + downloadInfo.getPercentStr());
                    });
                }

                @Override
                public void onError(Throwable e) {
                    Log.i("Splash", "downloadInfo" + e.getMessage());
                    downloadInfo.setDownloadStatus(CocosDownloadInfo.DOWNLOAD_ERROR);
                    requestFailed();
                }

                @Override
                public void onComplete() {
                    if (downloadInfo != null) {
                        downloadInfo.setDownloadStatus(CocosDownloadInfo.DOWNLOAD_OVER);
                        unZip();
                    }
                    Log.i("Splash", "onComplete" + downloadInfo.getDownloadStatus());
                }
            });
        } catch (Exception e) {
            requestFailed();
        }


    }

    public void unZip() {
        try {
            FileUtil.unzip(CocosDownloadManager.getInstance().getFilePath() + "data.zip", CocosDownloadManager.getInstance().getFilePath(), new FileUtil.ZipProgress() {
                @Override
                public void onProgress(int progress) {
                    Log.i("Splash", "progress:" + progress);
                }

                @Override
                public void onDone() {
                    Log.i("Splash", "onDone:");
                    SPHelp.instance().putBoolean("isFirst", true);
                    EventBus.getDefault().post("finish");
                    EventHelp.instance().logUpdateSuccess();
                }

                @Override
                public void failed() {
                    Log.i("Splash", "failed:");
                    requestFailed();
                }
            });
        } catch (Exception e) {
            requestFailed();
        }
    }
}

