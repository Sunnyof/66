package com.game.viewmodel;

public interface WebViewListener {
    public void showWebView();

    public void hideWebView();

    public void closeWebView();

    public void openWebView(String url, String bgColor, boolean showClose);

}
