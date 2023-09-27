package com.game.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileUtil {
    /**
     * 根据文件名称删除文件
     *
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String filePath,String fileName) {
        boolean status;
        SecurityManager checker = new SecurityManager();
        File file = new File(filePath + fileName);
        if (file.exists()) {
            checker.checkDelete(file.toString());
            if (file.isFile()) {
                try {
                    file.delete();
                    status = true;
                } catch (SecurityException se) {
                    se.printStackTrace();
                    status = false;
                }
            } else
                status = false;
        } else
            status = false;
        return status;
    }

    public static void unzip(String zipFileString, String outPathString, ZipProgress iProgress) throws Exception {
        long zipLength = getZipSize(zipFileString);
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        long count = 0;
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (szName != null && szName.contains("../")) {
                // 解压路径存在路径穿越问题，直接过滤
                continue;
            }
            if (zipEntry.isDirectory()) {
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                File file = new File(outPathString + File.separator + szName);
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                if (!file.exists())
                    file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = inZip.read(buffer)) != -1) {
                    count += len;
                    int progress = (int) ((count * 100) / zipLength);
                    if (iProgress != null) {
                        iProgress.onProgress(progress);
                    }
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        if (iProgress != null) {
            iProgress.onDone();
        }
        inZip.close();
    }

    private static long getZipSize(String filePath) {
        long size = 0;
        ZipFile f;
        try {
            f = new ZipFile(filePath);
            Enumeration<? extends ZipEntry> en = f.entries();
            while (en.hasMoreElements()) {
                size += en.nextElement().getSize();
            }
        } catch (IOException e) {
            size = 0;
        }
        return size;
    }

    public interface ZipProgress {
        void onProgress(int progress);

        void onDone();

        void failed();
    }

    public static boolean isMediaAlive() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}
