package com.cocos.lib;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


import org.colorful.interconnects.value.SPHelp;

import org.greenrobot.eventbus.EventBus;

public class CocosDialogUtil {

    private static String[] ERROR_1 = {"Jaringan  terputus, coba sambungkan kembali?", "Menghubungkan kembali"};
    private static String[] ERROR_2 = {"Mạng bị ngắt kết nối. Bạn có muốn cập nhật lại không?", "kết nối lại"};
    private static String[] ERROR_3 = {"Desconectado, tente atualizar de novo?", "Reconecte"};
    private static String[] ERROR_4 = {"Disconnected, try update again?", "Reconnect"};


    private static String[] CHOICE_1 = {"Kamera", "Foto", "Folder"};
    private static String[] CHOICE_2 = {"Máy ảnh", "Hình chụp", "Tài liệu"};
    private static String[] CHOICE_3 = {"Câmera", "Foto", "Arquivo"};
    private static String[] CHOICE_4 = {"Camera", "Photo", "File"};

    private static AlertDialog alertDialog;

    public static void showErrorDialog(Activity activity, boolean click) {
        if (null != alertDialog) {
            return;
        }
        alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setMessage(netTip()[0]);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, netTip()[1], (dialog, which) -> {
            if (click) {
                EventBus.getDefault().post("reConnect");
            }
        });
        alertDialog.setOnDismissListener(dialog -> alertDialog = null);
        alertDialog.show();
    }


    public static int getPlatform() {
        int platform = 28;
        String channelId = SPHelp.instance().popString("channel_id");
        if (!channelId.isEmpty()) {
            try {
                platform = Integer.parseInt(channelId);
            } catch (Exception e) {
                platform = 28;
            }
        }
        return platform;
    }


    //网络错误弹框
    public static String[] netTip() {
        switch (getPlatform()) {
            case 28:
            case 29:
                return ERROR_1;
            case 30:
            case 33:
            case 330000:
            case 38:
                return ERROR_2;
            case 32:
                return ERROR_3;
        }
        return ERROR_4;
    }

    //客服弹框选项
    public static String[] choice() {
        switch (getPlatform()) {
            case 28:
            case 29:
                return CHOICE_1;
            case 30:
            case 33:
            case 330000:
            case 38:
                return CHOICE_2;
            case 32:
                return CHOICE_3;
        }
        return CHOICE_4;
    }
}
