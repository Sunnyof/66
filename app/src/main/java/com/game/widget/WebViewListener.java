package com.game.widget;

public interface WebViewListener {
    public void openWebView(String str);

    public void closeWebView(boolean flag);

    public void hideWebView(int time, String name);

    public void openWebView(String url, String bgColor, boolean showClose);

}
