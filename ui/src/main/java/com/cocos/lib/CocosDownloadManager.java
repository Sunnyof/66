package com.cocos.lib;

import android.os.Environment;
import android.util.Log;


import org.colorful.interconnects.value.FileUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CocosDownloadManager {
    private final String TAG = this.getClass().getSimpleName();
    private static final AtomicReference<CocosDownloadManager> INSTANCE = new AtomicReference<>();
    private OkHttpClient mClient;
    private HashMap<String, Call> downCalls; //用来存放各个下载的请求


    private String filePath = Environment.getDataDirectory() + "/data/" + "game.Colorful.interconnects" + "/files/org/";

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public static CocosDownloadManager getInstance() {
        for (; ; ) {
            CocosDownloadManager current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new CocosDownloadManager();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    private CocosDownloadManager() {
        downCalls = new HashMap<>();
        mClient = new OkHttpClient.Builder().build();
    }

    /**
     * 查看是否在下载任务中
     *
     * @param url
     * @return
     */
    public boolean getDownloadUrl(String url) {
        return downCalls.containsKey(url);
    }

    /**
     * 开始下载
     *
     * @param url 下载请求的网址
     */
    public void download(CocosDownloadInfo info, String url, Observer<CocosDownloadInfo> observer) {
        // 下载
        // 生成 DownloadInfo
        // 过滤 call的map中已经有了,就证明正在下载,则这次不下载
        Observable.just(url)
                .filter(s -> !downCalls.containsKey(s))
                .map(s -> {
                    Log.i(TAG, "createDownInfo");
                    return info;
                })
                .map((Function<Object, CocosDownloadInfo>) o -> {
                    Log.i(TAG, "getRealFileName");
                    return getRealFileName((CocosDownloadInfo) o);
                })
                .flatMap((Function<CocosDownloadInfo, ObservableSource<CocosDownloadInfo>>) downloadInfo -> {
                    Log.i(TAG, "DownloadSubscribe");
                    return Observable.create(new DownloadSubscribe(downloadInfo));
                })
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程中回调
                .subscribeOn(Schedulers.io()) //  在子线程中执行
                .subscribe(observer); //  添加观察者，监听下载进度
    }

    /**
     * 下载取消或者暂停
     *
     * @param url
     */
    public void pauseDownload(String url) {
        Call call = downCalls.get(url);
        if (call != null) {
            call.cancel();//取消
        }
        downCalls.remove(url);
    }

    /**
     * 取消下载 删除本地文件
     *
     * @param info
     */
    public void cancelDownload(CocosDownloadInfo info) {
        pauseDownload(info.getUrl());
        info.setProgress(0);
        info.setDownloadStatus(CocosDownloadInfo.DOWNLOAD_CANCEL);
        EventBus.getDefault().post(info);
        FileUtil.deleteFile(filePath, info.getFileName());
    }

    /**
     * 创建DownInfo
     *
     * @param url 请求网址
     * @return DownInfo
     */
    public CocosDownloadInfo createDownInfo(String url) {
        CocosDownloadInfo downloadInfo = new CocosDownloadInfo(url);
        Log.i(TAG, "fileName:" + url);
        long contentLength = getContentLength(url);//获得文件大小
        Log.i(TAG, "fileName:" + contentLength);
        downloadInfo.setTotal(contentLength);
        String fileName = url.substring(url.lastIndexOf("/"));
        downloadInfo.setFileName(fileName);
        Log.i(TAG, "fileName:" + fileName);
        return downloadInfo;
    }

    /**
     * 如果文件已下载重新命名新文件名
     *
     * @param downloadInfo
     * @return
     */
    private CocosDownloadInfo getRealFileName(CocosDownloadInfo downloadInfo) {
        String fileName = downloadInfo.getFileName();
        long downloadLength = 0, contentLength = downloadInfo.getTotal();

        File path = new File(filePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(filePath, fileName);
        Log.i(TAG, filePath + "---" + file.getPath());
        if (file.exists()) {
            //找到了文件,代表已经下载过,则获取其长度
            downloadLength = file.length();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //设置改变过的文件名/大小
        downloadInfo.setProgress(downloadLength);
        return downloadInfo;
    }

    private class DownloadSubscribe implements ObservableOnSubscribe<CocosDownloadInfo> {
        private CocosDownloadInfo downloadInfo;

        public DownloadSubscribe(CocosDownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void subscribe(ObservableEmitter<CocosDownloadInfo> e) throws Exception {
            String url = downloadInfo.getUrl();
            long downloadLength = downloadInfo.getProgress();//已经下载好的长度
            long contentLength = downloadInfo.getTotal();//文件的总长度
            //初始进度信息
            e.onNext(downloadInfo);
            Request request = new Request.Builder()
                    //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
                    .addHeader("RANGE", "bytes=" + downloadLength + "-" + contentLength)
                    .url(url)
                    .build();
            Log.i(TAG, "downloadInfo url:" + downloadLength + "-" + contentLength);
            Call call = mClient.newCall(request);
            downCalls.put(url, call);//把这个添加到call里,方便取消
            InputStream is = null;
            FileOutputStream fileOutputStream = null;
            Response response;
            try {
                response = call.execute();
                File file = new File(filePath, downloadInfo.getFileName());
                is = response.body().byteStream();
                fileOutputStream = new FileOutputStream(file, true);
                byte[] buffer = new byte[4096];//缓冲数组2kB
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                    downloadLength += len;
                    downloadInfo.setProgress(downloadLength);
                    e.onNext(downloadInfo);
                }
                fileOutputStream.flush();
                downCalls.remove(url);
                Log.i(TAG, "fileName:" + file.getName());
            } catch (Exception exception) {
            } finally {
                //关闭IO流
                closeAll(is, fileOutputStream);

            }
            e.onComplete();//完成
        }
    }

    public void closeAll(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取下载长度
     *
     * @param downloadUrl
     * @return
     */
    private long getContentLength(String downloadUrl) {
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();

        Call call = mClient.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.i(TAG, "contentLength:" + "--" + downloadUrl);
            throw new RuntimeException(e);
        }
        Log.i(TAG, "contentLength:" + response.message() + "--" + downloadUrl);
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            Log.i(TAG, "contentLength:" + contentLength + "--" + downloadUrl);
            response.close();
            return contentLength == 0 ? CocosDownloadInfo.TOTAL_ERROR : contentLength;
        }
        return CocosDownloadInfo.TOTAL_ERROR;
    }

}
