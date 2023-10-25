package org.colorful.interconnects.model;

public interface JsListener {

    public void callUpdateInfoCallback();

//    public void callFcmTokenCallBack();

//    public void callFcmCustomDataCallBack();

    public void callLoadWebViewCallBack();

    public void callHideWebViewCallBack();

    public void callShowWebViewCallBack();

    public void callCloseWebViewCallBack();
}
