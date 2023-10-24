package org.colorful.interconnects.model;

public interface WebListener {
    public void showWebView();

    public void hideWebView();

    public void closeWebView();

    public void openWebView(String url, String bgColor, boolean showClose);

}
