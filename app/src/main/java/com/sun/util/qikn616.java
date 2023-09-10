package com.sun.util;

import android.util.Base64;

public class qikn616 {

    public static String decode (String str){
        return new String(Base64.decode(str,Base64.DEFAULT));
    }

}