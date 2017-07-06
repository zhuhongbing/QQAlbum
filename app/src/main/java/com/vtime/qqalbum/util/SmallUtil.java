package com.vtime.qqalbum.util;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.vtime.qqalbum.base.BaseApplication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jue on 2016/3/29.
 * 常用小工具类
 */
public class SmallUtil {

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = BaseApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * final Activity activity  ：调用该方法的Activity实例
     * long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */

    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }


    //将list转换为string
    public static String listToString(List<String> urlList) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < urlList.size(); i++) {
            if (null != buffer && buffer.length() > 0) {
                buffer.append("," + urlList.get(i));
            } else {
                buffer.append(urlList.get(i));
            }
        }
        return buffer.toString();
    }


    public static void Alert(Context context, String content) {
        Toast.makeText(context, content, 0).show();
    }


}
