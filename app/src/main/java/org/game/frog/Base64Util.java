package org.game.frog;

import android.util.Base64;

import java.util.zip.ZipInputStream;

public class Base64Util {
    public static String decodeFrog(String str) {
        try {
            byte[] data = Base64.decode(str, Base64.DEFAULT);
            return new String(data);
        } catch (Exception e) {
            return "frog";
        }
    }


    public void unzipFile(){

    }

}
