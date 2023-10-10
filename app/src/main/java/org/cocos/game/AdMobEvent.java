package org.cocos.game;

import org.greenrobot.eventbus.EventBus;

public class AdMobEvent {
    private boolean show;
    private String name;

    public AdMobEvent(boolean show, String name) {
        this.show = show;
        this.name = name;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
