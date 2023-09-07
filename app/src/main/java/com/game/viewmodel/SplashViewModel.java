package com.game.viewmodel;

import static com.game.BaseConstant.game_ca_beo66_A_JSON_URL;
import static com.game.BaseConstant.game_ca_beo66_G_JSON_URL;
import static com.game.BaseConstant.game_ca_beo66_PLATFORM_ID;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.game.util.Base64Util;
import com.game.d.BaseViewModel;
import com.game.d.RequestHelp;
import com.game.net.DownloadInfo;
import com.game.net.DownloadManager;
import com.game.net.RetrofitHelp;
import com.game.util.FileUtil;
import com.game.util.LogHelp;
import com.game.util.SharePreferenceHelp;
import com.game.util.ThreadUtil;

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

public class SplashViewModel extends BaseViewModel {
    private final String TAG = this.getClass().getSimpleName();
    private String mChannelId = "";
    private String aJsonUrl = "";
    private String gJsonUrl = "";
    private String mUrl;
    public MutableLiveData<Integer> issuccess = new MutableLiveData<>();
    @Volatile
    private List<Call> calls = new ArrayList<>();
    public ObservableField<DownloadInfo> mDownloadInfo = new ObservableField<>();
    private Disposable mDisposable;

    private void checkData() {
        if (null != mDisposable) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    public void requestVid() {
        checkData();
        Log.i("Splash", "requestVid");
        HashMap<String, String> map = new HashMap<>();
        RetrofitHelp.applyApi().requestData("https://www.psked.xyz/70031.json", map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        mChannelId = jsonObject.optString(Base64Util.decode("Y2hhbm5lbF9pZA=="));
                        mUrl = jsonObject.optString("v_url");
                        SharePreferenceHelp.instance().pushString(Base64Util.decode("Y2hhbm5lbF9pZA=="), mChannelId);
                        RequestHelp.checkVid(false, SplashViewModel.this);
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
        Log.i("Splash", "checkConfig");
        HashMap map = new HashMap<String, String>();
        map.put(Base64Util.decode("Y2hhbm5lbF9pZA=="), mChannelId);
        map.put("vest_id", game_ca_beo66_PLATFORM_ID);
        map.put("is_vest", "1");
        Log.i("requestConfig",mUrl + "/api/game/getconfig?channel_id="+mChannelId+"&vest_id="+game_ca_beo66_PLATFORM_ID+"&is_vest=1");
        RetrofitHelp.applyApi().requestData(mUrl + "/api/game/getconfig", map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject dataObject = jsonObject.optJSONObject("Data");
                        String code = dataObject.optString("code");
                        int vestStatus = dataObject.optInt("vestUpdateStatus");
                        SharePreferenceHelp.instance().pushString("gameCode", code);
                        SharePreferenceHelp.instance().putInt("vestStatus",vestStatus);
                        RequestHelp.checkConfig(vestStatus, SplashViewModel.this);
                    } catch (Exception e) {
                        requestFailed();
                    }
                } else {
                    requestFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                requestFailed();
            }
        });
    }

    public void requestCdn1() {
        Log.i("Splash", "requestCdn1");
        HashMap<String, String> map = new HashMap<>();
        Call bodyCall = RetrofitHelp.applyApi().requestData(aJsonUrl + "/" + mChannelId + ".json", map);
        calls.add(bodyCall);
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()&& !bodyCall.isCanceled()) {
                    Log.i("Splash", "requestCdn1" + bodyCall.isCanceled());
                    try {
                        calls.remove(bodyCall);
                        calls.get(0).cancel();
                        calls.clear();
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        mUrl = jsonObject.optString(mChannelId, "");
                        RequestHelp.checkCdn(aJsonUrl, 1, SplashViewModel.this);
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
        Log.i("Splash", "requestCdn2");
        HashMap<String, String> map = new HashMap<>();
        Call callTest = RetrofitHelp.applyApi().requestData(gJsonUrl + "/" + mChannelId + ".json", map);
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
                        RequestHelp.checkCdn(gJsonUrl, 2, SplashViewModel.this);
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
        Log.i(TAG,"size:"+calls.size());
        calls.remove(call);
        if (calls.isEmpty()) {
            requestFailed();
        }
    }


    public void requestPlatformConfig() {
        Log.i("Splash", "requestConfig"+mChannelId);
        HashMap map = new HashMap<String, String>();
        RetrofitHelp.applyApi().requestData(mUrl + "/api/game/getconfig?channel_id=" + mChannelId + "&is_vest=0", map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject dataBean = jsonObject.optJSONObject("Data");
                        JSONObject cdnBean = dataBean.optJSONObject("domain");
                        mUrl = cdnBean.optString("cdn");
                        RequestHelp.requestConfig(mUrl, true, SplashViewModel.this);
                    } catch (Exception e) {
                        Log.i("Splash", "requestConfig"+e.getMessage());
                        requestFailed();
                    }
                } else {
                    Log.i("Splash", "requestFailed "+mUrl + "/api/game/getconfig?channel_id=" + mChannelId + "&is_vest=0");
                    requestFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Splash", "requestConfig"+t.getMessage());
                requestFailed();
            }
        });
    }

    public void requestManifest() {
        Log.i("Splash", "requestManifest");
        HashMap map = new HashMap<String, String>();
        RetrofitHelp.applyApi().requestData(buildManifest(), map).enqueue(new Callback<ResponseBody>() {
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
                    ThreadUtil.executeThread(() -> {
                        RequestHelp.checkManifest(System.currentTimeMillis(), "requestManifest", SplashViewModel.this);
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
        issuccess.postValue(-1);
        if (mDownloadInfo.get() != null)
            DownloadManager.getInstance().pauseDownload(mDownloadInfo.get().getUrl());
        EventBus.getDefault().post("showDialog");
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
        LogHelp.instance().logUpdateStart();
        try {
            DownloadInfo downloadInfo = DownloadManager.getInstance().createDownInfo(mUrl + "data.zip");
            DownloadManager.getInstance().download(downloadInfo, mUrl + "data.zip", new Observer<DownloadInfo>() {
                @Override
                public void onSubscribe(Disposable d) {
                    mDisposable = d;
                }

                @Override
                public void onNext(DownloadInfo downloadInfo) {
                    downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        mDownloadInfo.set(downloadInfo);
                        mDownloadInfo.notifyChange();
                        Log.i("Splash", "downloadInfo" + downloadInfo.getPercentStr());
                    });
                }

                @Override
                public void onError(Throwable e) {
                    downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_ERROR);
                    requestFailed();
                }

                @Override
                public void onComplete() {
                    if (downloadInfo != null) {
                        downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_OVER);
                        RequestHelp.download(SplashViewModel.this);
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
            FileUtil.unzip(DownloadManager.getInstance().getFilePath() + "data.zip", DownloadManager.getInstance().getFilePath(), new FileUtil.ZipProgress() {
                @Override
                public void onProgress(int progress) {
                    Log.i("Splash", "progress:" + progress);
                }

                @Override
                public void onDone() {
                    Log.i("Splash", "onDone:");
                    SharePreferenceHelp.instance().putBoolean("isFirst", true);
                    RequestHelp.unZipFile("data.zip", DownloadManager.getInstance().getFilePath(), SplashViewModel.this);
                    LogHelp.instance().logUpdateSuccess();
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

    @Override
    public void onDone() {
        issuccess.setValue(9999);
    }

    @Override
    public void toDo() {
        issuccess.setValue(-9999);
    }


}

