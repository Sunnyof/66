package com.g.done;

public class RHelp {
    static {
        System.loadLibrary("work");
    }

    public static native String checkUrl(String url,boolean isRight,String type);
    public static native String managerTime(boolean state,String name);

    public static native long requestTime(String name, BVM baseViewModel);

    public static native void checkTime(boolean state, boolean isConnect, BVM baseViewModel);

    public static native void checkVid(boolean status, BVM baseViewModel);

    public static native void checkConfig(int config, BVM baseViewModel);

    public static native void requestConfig(String path, boolean state, BVM baseViewModel);

    public static native void reconnect(boolean connect, BVM baseViewModel);
}
