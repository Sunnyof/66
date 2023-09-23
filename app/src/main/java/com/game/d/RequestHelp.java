package com.game.d;

public class RequestHelp {
    public static native long requestTime(String name, BaseViewModel baseViewModel);

    public static native void checkTime(boolean state, boolean isConnect, BaseViewModel baseViewModel);

    public static native void checkVid(boolean status, BaseViewModel baseViewModel);

    public static native void checkConfig(int config, BaseViewModel baseViewModel);

    public static native void requestConfig(String path, boolean state, BaseViewModel baseViewModel);

    public static native void checkManifest(long manifest, String name, BaseViewModel baseViewModel);

    public static native void checkCdn(String cdn, int position, BaseViewModel baseViewModel);

    public static native void download(BaseViewModel baseViewModel);

    public static native void unZipFile(String fileName, String path, BaseViewModel baseViewModel);

    public static native void reconnect(boolean connect,BaseViewModel baseViewModel);
}
