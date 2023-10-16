package com.mul.download.ui;




import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cocos.lib.CocosActivity;
import com.mul.downloadui.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mul.download.adapter.LanguageDownloadAdapter;
import com.mul.download.bean.DownloadBean;
import com.mul.download.bean.DownloadConfigBean;
import com.mul.download.bean.LanguageBean;
import com.mul.download.click.OnProgressListener;
import com.mul.download.config.EventConfig;
import com.mul.download.event.EventBusMessage;
import com.mul.download.proxy.DownloadProxy;
import com.mul.download.util.DataUtils;
import com.mul.download.util.FileAccessor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends CocosActivity {
    public static String TAG = "com.iguan.text.ui.MainActivity";

    private ConstraintLayout title;
    private RecyclerView rv;
    private LanguageDownloadAdapter rvAdapter;
    private LanguageBean languageBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        FileAccessor.initFileAccess();
        DataUtils.getInstance();
////        DownloadManagerController.getInstance().init(MainActivity.this);
//        DownloadProxy.obtain().init(new DownloadConfigBean().setContext(this));
//        initView();
//        initRv();
//        initClick();
    }

//    private void initView() {
//        title = findViewById(R.id.title);
//        rv = findViewById(R.id.rv);
//    }

    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new LanguageDownloadAdapter();
        rv.setAdapter(rvAdapter);
        rvAdapter.setDatas(DataUtils.getInstance().getDatas());
        rvAdapter.setItemClick(new LanguageDownloadAdapter.ItemClick() {

            @Override
            public void itemSwitchClick(LanguageBean languageBean) {
                Log.i(TAG, "itemSwitchClick::::::切换语言项");
//                DownloadManagerController.getInstance().remoe(SpUtil.getInstance().getValue("ceshi", 0L));
                if (!languageBean.isSelect()) {
                    rvAdapter.setDatas(DataUtils.getInstance().updateData(languageBean));
                }
            }

            @Override
            public void itemDownloadClick(final LanguageBean languageBean) {
                Log.i(TAG, "itemDownloadClick::::::下载语言项");
                MainActivity.this.languageBean = languageBean;
//                DialogActivity.launch(MainActivity.this, false, languageBean);
                requestPermission(1, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

            }

            @Override
            public void itemLongClick(LanguageBean languageBean) {
                Log.i(TAG, "itemLongClick::::::长按");
                DialogActivity.launch(MainActivity.this, true, languageBean);
            }
        });
    }

    /**
     * 表示事件处理函数的线程在主线程(UI)线程，因此在这里不能进行耗时操作。
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainMessageEvent(EventBusMessage event) {
        if (null != event && EventConfig.FILE_DELETE_SUCCESS.equals(event.getStatusStr())) {
            DataUtils.getInstance().setData();
            rvAdapter.setDatas(DataUtils.getInstance().getDatas());
        }
    }

    private void initClick() {
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DownloadProxy.obtain().registerDownload();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DownloadProxy.obtain().unRegisterDownload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private int mRequestCode;

    /**
     * 请求权限
     *
     * @param permissions 需要的权限列表
     * @param requestCode 请求码
     */
    protected void requestPermission(int requestCode, String... permissions) {
        mRequestCode = requestCode;
        if (checkPermissions(permissions)) {
            permissionSuccess(requestCode);
        } else {
            List<String> needPermissions = checkSelfPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //系统请求权限回调
        if (requestCode == mRequestCode) {
            if (verifyPermissions(grantResults)) {
                permissionSuccess(mRequestCode);
            } else {
                permissionFail(mRequestCode);
            }
        }
    }

    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    protected void permissionSuccess(int requestCode) {
        DownloadProxy.obtain().download("https://images.pexels.com/photos/61135/pexels-photo-61135.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                , FileAccessor.TRANSLATE_MICROSOFT_DOWNLOAD_PATH
                , languageBean.getFileName()
                , languageBean.getPosition())
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(DownloadBean downloadBean) {
                        Log.i(TAG, "当前id为" + downloadBean.getDownloadId()
                                + "::::下载进度为::::::" + downloadBean.getProgress()
                                + "::::第几条::::::" + downloadBean.getPosition());
                        DataUtils.getInstance().getDatas().get(downloadBean.getPosition()).setProgress(downloadBean.getProgress() * 360);
                        DataUtils.getInstance().getDatas().set(downloadBean.getPosition(), DataUtils.getInstance().getDatas().get(downloadBean.getPosition()));
                        rvAdapter.setDatas(DataUtils.getInstance().getDatas());
                    }

                    @Override
                    public void onSuccess(DownloadBean downloadBean) {
                        DataUtils.getInstance().setData();
                        rvAdapter.setDatas(DataUtils.getInstance().getDatas());
                    }
                });
    }

    protected void permissionFail(int mRequestCode) {

    }

    /**
     * 检查权限
     *
     * @param permissions
     * @return
     */
    private List<String> checkSelfPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            // 检查权限,如果没有授权就添加
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;

    }

    /**
     * 检查所需的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions) {
        // 手机版本 SDK 低于23 ，在Manifest 上注册有效，大于 23 的（android6.0以后的），读取手机的隐私需要在代码动态申请
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}