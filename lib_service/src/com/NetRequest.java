package com;

public interface NetRequest {
    public void requestVid();

    public void requestManifest();

    public void requestCdn1();

    public void requestCdn2();

    public void requestPlatformConfig();

    public void requestConfig();

    public void download();

    public void unZip();

    public void onDone();
}
