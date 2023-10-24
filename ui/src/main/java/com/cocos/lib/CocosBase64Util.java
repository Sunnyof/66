package com.cocos.lib;

import android.util.Base64;

public class CocosBase64Util {

    public static String decode (String str){
        return new String(Base64.decode(str,Base64.DEFAULT));
    }

}
