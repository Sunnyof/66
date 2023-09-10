package com.sun.viewmodel;

import static com.sun.mdhaat979.game_ca_beo66_PLATFORM_ID;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.sun.util.qikn616;
import com.g.done.BVM;
import com.g.done.RHelp;
import com.sun.net.cvokt977;
import com.sun.util.wovg23;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import kotlin.jvm.Volatile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class yycvfvfm192 extends BVM {
    private final String TAG = this.getClass().getSimpleName();
    private String mChannelId = "";
    private String mUrl;
    public MutableLiveData<Integer> issuccess = new MutableLiveData<>();
    @Volatile
    private Disposable mDisposable;

    private void checkData() {
        if (null != mDisposable) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    @Override
    public void setName(String name) {
        name = issuccess.getValue().toString();
    }

    public void vid() {
        HashMap<String, String> map = new HashMap<>();
        String url = "https://www.psked.xyz/70031.json";
        cvokt977.applyApi().requestUrl(url, map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        mChannelId = jsonObject.optString(qikn616.decode("Y2hhbm5lbF9pZA=="));
                        mUrl = jsonObject.optString("v_url");
                        wovg23.instance().pushString(qikn616.decode("Y2hhbm5lbF9pZA=="), mChannelId);
                        RHelp.checkVid(false, yycvfvfm192.this);
                    } catch (Exception e) {
                        requestFailed();
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

    @Override
    public void getName() {
        Log.i("Splash", "getName");
    }


    public void config() {
        Log.i("Splash", "checkConfig");
        HashMap map = new HashMap<String, String>();
        map.put(qikn616.decode("Y2hhbm5lbF9pZA=="), mChannelId);
        map.put("vest_id", game_ca_beo66_PLATFORM_ID);
        map.put("is_vest", "1");
        Log.i("requestConfig", mUrl + "/api/game/getconfig?channel_id=" + mChannelId + "&vest_id=" + game_ca_beo66_PLATFORM_ID + "&is_vest=1");
        cvokt977.applyApi().requestUrl(mUrl + "/api/game/getconfig", map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject dataObject = jsonObject.optJSONObject("Data");
                        String code = dataObject.optString("code");
                        int vestStatus = dataObject.optInt("vestUpdateStatus");
                        wovg23.instance().pushString("gameCode", code);
                        wovg23.instance().putInt("vestStatus", vestStatus);
                        RHelp.checkConfig(vestStatus, yycvfvfm192.this);
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


    public void requestPlatformConfig(String name, int age) {
        Log.i("Splash", "requestConfig" + mChannelId);
        HashMap map = new HashMap<String, String>();
        cvokt977.applyApi().requestUrl(mUrl + "/api/game/getconfig?channel_id=" + mChannelId + "&is_vest=0", map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject dataBean = jsonObject.optJSONObject("Data");
                        JSONObject cdnBean = dataBean.optJSONObject("domain");
                        mUrl = cdnBean.optString("cdn");
                        RHelp.requestConfig(mUrl, true, yycvfvfm192.this);
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


    private void requestFailed() {
        issuccess.postValue(-1);
        EventBus.getDefault().post("showDialog");
    }

    @Override
    public void done() {
        issuccess.setValue(9999);
    }

    @Override
    public void next() {
        issuccess.setValue(-9999);
    }


}