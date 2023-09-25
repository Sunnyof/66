package com.crab.shooter;

import android.util.Base64;

public class Crab64Util {

    public static String decode (String str){
        return new String(Base64.decode(str,Base64.DEFAULT));
    }

}
