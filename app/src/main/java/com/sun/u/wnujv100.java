package com.sun.u;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;

import com.sun.tmrbmq646;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;


class wnujv100 {

    /**
     * 是否获取过刘海高度
     */
    private boolean hasExecuteNotch = false;

    /**
     * 是否有刘海
     */
    private boolean isNotch = false;

    /**
     * 刘海高度
     */
    private long notchHeight = 0;

    /**
     * 获取刘海高度
     */
    public int getNotchHeight(Activity activity) {
        String manufacturer = Build.MANUFACTURER.toLowerCase(Locale.getDefault());
        if (hasNotch(activity)) {
            //有刘海才获取高度 否则默认刘海高度是0
            if (manufacturer.equalsIgnoreCase("xiaomi")) {
                notchHeight = getSysStatusBarHeight(); //小米刘海会比状态栏小 直接获取状态栏高度
            } else if (manufacturer.equalsIgnoreCase(
                    "huawei"
            ) || manufacturer.equalsIgnoreCase("honour")
            ) {
                notchHeight = getNotchSizeAtHuaWei();
            } else if (manufacturer.equalsIgnoreCase("vivo")) {
                //VIVO是32dp
                notchHeight = (long) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        32f,
                        tmrbmq646.getInstance().getApplicationContext().getResources().getDisplayMetrics()
                );
            } else if (manufacturer.equalsIgnoreCase("oppo")) {
                notchHeight = 80; //oppo当时是固定数值
            } else if (Build.MANUFACTURER.equalsIgnoreCase("smartisan")) {
                notchHeight = 82; //当时锤子PDF文档上是固定数值
            } else {
                //其他品牌手机
                if (activity != null && activity.getWindow() != null) {
                    View decorView = activity.getWindow().getDecorView();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        WindowInsets windowInsets = decorView.getRootWindowInsets();
                        if (windowInsets != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                                if (displayCutout != null) {
                                    List<Rect> rects = displayCutout.getBoundingRects();
                                    if (rects != null && rects.size() > 1) {
                                        if (rects.get(0) != null) {
                                            notchHeight = rects.get(0).bottom;
                                            return (int) notchHeight;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return (int) notchHeight;
        }
        return 0;
    }

    /**
     * 小米刘海屏判断
     *
     * @return 0 if it is not notch ; return 1 means notch
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    private boolean hasNotchAtXiaoMi() {
        return getInt("ro.miui.notch") == 1;
    }

    private int getInt(String key) {
        int result = 0;
        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            try {
                ClassLoader classLoader = tmrbmq646.getInstance().getClassLoader();
                Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
                //参数类型
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = Integer.class;
                Method getInt = SystemProperties.getMethod("getInt", paramTypes);
                //参数
                Object[] params = new Object[2];
                params[0] = key;
                params[1] = 0;
                result = (int) getInt.invoke(SystemProperties, params);
            } catch (Exception e) {
                return 0;
            }
        }
        return result;
    }

    /**
     * 华为刘海屏判断
     *
     * @return
     */
    private boolean hasNotchAtHuawei() {
        boolean ret = false;
        try {
            ClassLoader classLoader = tmrbmq646.getInstance().getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (java.lang.Exception e) {
            return ret;
        }
        return ret;
    }

    /**
     * OPPO刘海屏判断
     *
     * @return
     */
    private boolean hasNotchAtOPPO() {
        return tmrbmq646.getInstance().getPackageManager()
                .hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    /**
     * VIVO刘海屏判断
     *
     * @return
     */
    private boolean hasNotchAtVivo() {
        boolean ret = false;
        byte VIVO_NOTCH = 0x00000020;
        try {
            ClassLoader classLoader = tmrbmq646.getInstance().getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", Integer.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (java.lang.Exception e) {
            return false;
        }
        return ret;
    }

    /**
     * 判断三星手机是否有刘海屏
     *
     * @return
     */
    private boolean hasNotchSamsung() {
        if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            try {
                Resources res = tmrbmq646.getInstance().getResources();
                int resId =
                        res.getIdentifier("config_mainBuiltInDisplayCutout", "string", "android");
                String spec = resId > 0 ? res.getString(resId) : null;
                return spec != null && !TextUtils.isEmpty(spec);
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Google手机是否有刘海屏
     *
     * @param activity
     * @return
     */
    private boolean isOtherBrandHasNotch(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            View decorView = activity.getWindow().getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                WindowInsets windowInsets = decorView.getRootWindowInsets();
                if (windowInsets != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                        if (displayCutout != null) {
                            List<Rect> rects = displayCutout.getBoundingRects();
                            if (rects != null && rects.size() > 0) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    /**
     * 是否是刘海或是钻孔屏幕 全局只取一次
     *
     * @return
     */
    private boolean hasNotch(Activity activity) {
        if (!hasExecuteNotch) {
            isNotch =
                    (hasNotchAtXiaoMi() || hasNotchAtHuawei() || hasNotchAtOPPO() || hasNotchAtVivo() || hasNotchSamsung() || isOtherBrandHasNotch(
                            activity
                    ));
            hasExecuteNotch = true;
        }
        return isNotch;
    }

    /**
     * 获得手机状态栏高度
     *
     * @return
     */
    private int getSysStatusBarHeight() {
        int result = 0;
        Resources resources = tmrbmq646.getInstance().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        if (result == 0) {
            result = 48;
        }
        return result;
    }

    /**
     * 华为获取刘海高度
     * 获取刘海尺寸：width、height
     * int[0]值为刘海宽度 int[1]值为刘海高度
     */
    private int getNotchSizeAtHuaWei() {
        int height = 0;
        try {
            ClassLoader cl = tmrbmq646.getInstance().getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            int[] ret = (int[]) get.invoke(HwNotchSizeUtil);
            height = ret[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
        return height;
    }

    public static wnujv100 getInstance() {
        return new wnujv100();
    }

}