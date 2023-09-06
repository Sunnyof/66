package com.game.util;

import android.util.Base64;

public class Base64Util {

    public static String decode (String str){
        return new String(Base64.decode(str,Base64.DEFAULT));
    }

}
