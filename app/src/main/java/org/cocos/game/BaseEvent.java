package org.cocos.game;

import org.greenrobot.eventbus.EventBus;

public class BaseEvent {
    public static void adEventShow() {
        EventBus.getDefault().post(new AdMobEvent(true, "Base Event"));
    }
}
