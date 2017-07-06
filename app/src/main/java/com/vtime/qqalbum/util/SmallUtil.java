package com.vtime.qqalbum.util;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.easeui.domain.EaseUser;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.hfnzz.ziyoumao.R;
import net.hfnzz.ziyoumao.base.BaseApplication;
import net.hfnzz.ziyoumao.callback.UpdateChatCallBack;
import net.hfnzz.ziyoumao.callback.UpdateGroupCallBack;
import net.hfnzz.ziyoumao.http.Http;
import net.hfnzz.ziyoumao.model.GroupInfoBean;
import net.hfnzz.ziyoumao.model.UserBean;
import net.hfnzz.ziyoumao.model.WalletInfoBean;
import net.hfnzz.ziyoumao.myview.loading.VProgressDialog;
import net.hfnzz.ziyoumao.ui.UserInfoMode.SetNewPayCodeAcitivty;
import net.hfnzz.ziyoumao.ui.chat.DemoHelper;
import net.hfnzz.ziyoumao.ui.login.LoginActivity;
import net.hfnzz.ziyoumao.ui.login.PhoneVerificationActivity;
import net.hfnzz.ziyoumao.ui.pay.WalletPayActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static net.hfnzz.ziyoumao.configs.Constant.WX_APP_ID;
import static net.hfnzz.ziyoumao.configs.Instance.gson;
import static net.hfnzz.ziyoumao.utils.SharedPreferencesManager.groupHead;
import static net.hfnzz.ziyoumao.utils.SharedPreferencesManager.groupNick;

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
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeith(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = BaseApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
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

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

   /* public static int getStatusBarHeight(Context context) {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }*/


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

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = BaseApplication.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(BaseApplication.getInstance().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 把字符串转为日期
     *
     * @param strDate
     * @return
     * @throws Exception
     */
    public static Date StringToDate(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }

    /**
     * 把字符串转为日期
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 是否为今天之后的日期
     */
    public static boolean isAfter(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date));
        long time = cal.getTimeInMillis();

        return (System.currentTimeMillis() - time) > (1000 * 3600 * 24) ? false : true;
    }

    /**
     * 字符串的日期格式的计算
     */
    public static boolean isAfter(String beforeDay, String afterDay) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(beforeDay));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(afterDay));
        long time2 = cal.getTimeInMillis();

        return time2 - time1 > 0 ? true : false;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static void hideInputMethod(Context context, EditText editText) {
        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    /**
     * 若字符串为null、则返回空串
     *
     * @param str
     * @return
     */
    public static String toString(String str) {
        if (null != str) {
            return str;
        } else {
            return "";
        }
    }

    /**
     * 为什么有这样的方法、我不想说
     */
    public static ArrayList<String> deleteEmptyList(List<String> list) {
        ArrayList<String> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals("")) {
                newList.add(list.get(i));
            }
        }
        return newList;
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        if (MySharedPreferences.getStorageFromSharedPreference("loginState").equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    public static void reLogin(final Context context) {
        //EMClient.getInstance().logout(true);
        DemoHelper.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                MySharedPreferences.StorageBySharedPreference("loginState", "0");
                context.startActivity(new Intent(context, LoginActivity.class).putExtra("flag", 1));
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
            }
        });
    }

    /**
     * 弹出输入法
     *
     * @param context
     */
    public static void showInputMethod(Context context) {

        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!im.isActive()) {
            im.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
        }
    }

    /**
     * 打卡软键盘
     */
    public static void showInputMethod(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     */
    public static void closeInputMethod(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 微信支付
     */
    public static void weChatPay(final Activity context, String Purpose, String Explain, String fee, String Password, String refOrder) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, WX_APP_ID);
        api.registerApp(WX_APP_ID);
        if (!api.isWXAppInstalled()) {
            new AlertDialog.Builder(context).setMessage("请先安装微信才能支付").setPositiveButton("确定", null).create().show();
            return;
        } else if (!api.isWXAppSupportAPI()) {
            new AlertDialog.Builder(context).setMessage("您的微信版本过低，无法支付").setPositiveButton("确定", null).create().show();
            return;
        }
        final VProgressDialog progressDialog = new VProgressDialog(context);
        progressDialog.showProgressDlg(R.string.common_loading);
        Call call = Http.getHttpService().TenPrePay(Purpose, Explain, fee, refOrder, CatUtils.getId(), CatUtils.getToken(), Password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    if (json.getString("_Status").equals("1")) {
                        PayReq request = new PayReq();
                        request.appId = WX_APP_ID;
                        request.partnerId = json.getString("partnerId");
                        request.prepayId = json.getString("prepayId");
                        request.packageValue = json.getString("package");
                        request.nonceStr = json.getString("nonceStr");
                        request.timeStamp = json.getString("timeStamp");
                        request.sign = json.getString("sign");
                        api.sendReq(request);
                        progressDialog.dismissProgressDlg();
                    } else if (json.getString("_Status").equals("1")) {
                        SmallUtil.reLogin(context);
                        progressDialog.dismissProgressDlg();
                    } else {
                        new AlertDialog.Builder(context).setMessage("支付失败，" + json.getString("message")).setPositiveButton("确定", null).create().show();
                        progressDialog.dismissProgressDlg();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismissProgressDlg();
            }
        });
    }

    /**
     * 钱包支付
     */
    public static void walletPay(final Activity context, final String Purpose, final String Explain, final String fee, final String refOrder) {
        final String phone = SharedPreferencesManager.getUserInfo().getPhone();
        Call call = Http.getHttpService().GetWalletInfo(CatUtils.getId(), CatUtils.getToken(), System.currentTimeMillis() + "");
        call.enqueue(new Callback<WalletInfoBean>() {
            @Override
            public void onResponse(Call<WalletInfoBean> call, Response<WalletInfoBean> response) {
                WalletInfoBean bean = response.body();
                if (bean.get_Status().equals("1")) {
                    String password = bean.getPassword();
                    if (password.equals("") && phone.equals("")) {
                        Intent it = new Intent(context, PhoneVerificationActivity.class);
                        it.putExtra("part", 0);
                        it.putExtra("flag", 4);
                        context.startActivity(it);
                        return;
                    }
                    if (password.equals("")) {
                        context.startActivity(new Intent(context, PhoneVerificationActivity.class).putExtra("flag", 1).putExtra("phone", phone));
                    }
                    if (!password.equals("")) {
                        Intent it = new Intent(context, WalletPayActivity.class);
                        it.putExtra("fei", fee);
                        it.putExtra("orderid", refOrder);
                        it.putExtra("purpose", Purpose);
                        it.putExtra("flag", 1);
                        it.putExtra("explain", Explain);
                        context.startActivity(it);
                    }
                } else if (bean.get_Status().equals("-1")) {
                    SmallUtil.reLogin(context);
                } else {
                    Toast.makeText(context, bean.get_Message(), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<WalletInfoBean> call, Throwable t) {

            }
        });
    }

    /**
     * 微信支付
     */
    public static void weChatPay(final Fragment context, String Purpose, String Explain, String fee, String Password, String refOrder) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context.getContext(), WX_APP_ID);
        api.registerApp(WX_APP_ID);
        if (!api.isWXAppInstalled()) {
            new AlertDialog.Builder(context.getContext()).setMessage("请先安装微信才能支付").setPositiveButton("确定", null).create().show();
            return;
        } else if (!api.isWXAppSupportAPI()) {
            new AlertDialog.Builder(context.getContext()).setMessage("您的微信版本过低，无法支付").setPositiveButton("确定", null).create().show();
            return;
        }
        final VProgressDialog progressDialog = new VProgressDialog(context.getContext());
        progressDialog.showProgressDlg(R.string.common_loading);
        Call call = Http.getHttpService().TenPrePay(Purpose, Explain, fee, refOrder, CatUtils.getId(), CatUtils.getToken(), Password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    if (json.getString("_Status").equals("1")) {
                        //预下单成功
                        //发起微信支付
                        PayReq request = new PayReq();
                        request.appId = WX_APP_ID;
                        request.partnerId = json.getString("partnerId");
                        request.prepayId = json.getString("prepayId");
                        request.packageValue = json.getString("package");
                        request.nonceStr = json.getString("nonceStr");
                        request.timeStamp = json.getString("timeStamp");
                        request.sign = json.getString("sign");
                        api.sendReq(request);
                        progressDialog.dismissProgressDlg();
                    } else if (json.getString("_Status").equals("1")) {
                        SmallUtil.reLogin(context.getContext());
                        progressDialog.dismissProgressDlg();
                    } else {
                        new AlertDialog.Builder(context.getContext()).setMessage("支付失败，" + json.getString("message")).setPositiveButton("确定", null).create().show();
                        progressDialog.dismissProgressDlg();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismissProgressDlg();
            }
        });
    }

    /**
     * 钱包支付
     */
    public static void walletPay(final Fragment context, final String Purpose, final String Explain, final String fee, final String refOrder) {
        Call call = Http.getHttpService().GetWalletInfo(CatUtils.getId(), CatUtils.getToken(), System.currentTimeMillis() + "");
        call.enqueue(new Callback<WalletInfoBean>() {
            @Override
            public void onResponse(Call<WalletInfoBean> call, Response<WalletInfoBean> response) {
                WalletInfoBean bean = response.body();
                if (bean.get_Status().equals("1")) {
                    String password = bean.getPassword();
                    String phone = MySharedPreferences.getStorageFromSharedPreference(context.getContext(), "phone");
                    if (password.equals("")) {
                        Intent it = new Intent(context.getContext(), SetNewPayCodeAcitivty.class);
                        it.putExtra("flag", 8);
                        context.startActivity(it);
                    }
                    if (!password.equals("")) {
                        Intent it = new Intent(context.getContext(), WalletPayActivity.class);
                        it.putExtra("fei", fee);
                        it.putExtra("orderid", refOrder);
                        it.putExtra("purpose", Purpose);
                        it.putExtra("flag", 1);
                        it.putExtra("explain", Explain);
                        context.startActivityForResult(it, 2);
                    }
                    if (password.equals("") && phone.equals("")) {
                        Intent it = new Intent(context.getContext(), PhoneVerificationActivity.class);
                        it.putExtra("flag", 4);
                        it.putExtra("part", 0);
                        context.startActivity(it);
                    }
                } else if (bean.get_Status().equals("-1")) {
                    SmallUtil.reLogin(context.getContext());
                } else {
                    Toast.makeText(context.getContext(), bean.get_Message(), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<WalletInfoBean> call, Throwable t) {

            }
        });
    }


    //根据环信id获取头像昵称
    public static void updateChatUserInfo(final Context context, final List<String> userList, final UpdateChatCallBack callBack) {
        final Map<String, EaseUser> users = new HashMap<>();
        StringBuffer user_id = new StringBuffer();
        for (String userName : userList) {
            if (userName.startsWith("zym")) {
                if (TextUtils.isEmpty(user_id)) {
                    user_id.append(userName);
                } else {
                    user_id.append("," + userName);
                }
            }
        }
        if (TextUtils.isEmpty(user_id)) {
            callBack.updateEvent(users);
            return;
        }
        Call call = Http.getHttpService().GetUserPhotoInfo(user_id.toString(), CatUtils.getId(), CatUtils.getToken());
        call.enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                UserBean bean = response.body();
                Log.e("GetUserPhotoInfo", gson.toJson(response.body()));
                if (bean.get_Status().equals("1")) {
                    //List<EaseUser> easeUsers = new ArrayList<EaseUser>();
                    for (int i = 0; i < bean.getUsers().size(); i++) {
                        Log.e("GetUserPhotoInfo", bean.getUsers().get(i).getId());
                        EaseUser user = new EaseUser(bean.getUsers().get(i).getId());
                        user.setAvatar(bean.getUsers().get(i).getHeadImgUrl());
                        user.setNick(bean.getUsers().get(i).getMemo());
                        user.setMemo(bean.getUsers().get(i).getNickName());
                        String local_nickname = MySharedPreferences.getStorageFromSharedPreference(user.getUsername() + SharedPreferencesManager.USER_NICK_NAME);
                        String local_headimage = MySharedPreferences.getStorageFromSharedPreference(user.getUsername() + "_headimage");
                        String local_memo = MySharedPreferences.getStorageFromSharedPreference(user.getUsername() + "_memo");
                        if (!local_nickname.equals(user.getNick())) {
                            MySharedPreferences.StorageBySharedPreference(user.getUsername() + SharedPreferencesManager.USER_NICK_NAME, user.getNick());
                        }
                        if (!local_headimage.equals(user.getAvatar())) {
                            MySharedPreferences.StorageBySharedPreference(user.getUsername() + "_headimage", user.getAvatar());
                        }
                        if (!local_memo.equals(user.getMemo())) {
                            MySharedPreferences.StorageBySharedPreference(user.getUsername() + "_memo", user.getMemo());
                        }
                        users.put(bean.getUsers().get(i).getId(), user);
                        // easeUsers.add(user);
                    }
                   /* UserDao dao = new UserDao(BaseApplication.getInstance());
                    dao.saveContactList(easeUsers);*/
                    callBack.updateEvent(users);
                } else if (bean.get_Status().equals("-1")) {
                    SmallUtil.reLogin(context);
                } else {
                    callBack.updateEvent(users);
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {

            }
        });
    }


    //根据环信id获取头像昵称
    public static void updateChatUserInfo(final Context context, final String groupId, final List<String> userList, final UpdateChatCallBack callBack) {
        final Map<String, EaseUser> users = new HashMap<>();
        StringBuffer user_id = new StringBuffer();
        for (String userName : userList) {
            if (userName.startsWith("zym")) {
                if (TextUtils.isEmpty(user_id)) {
                    user_id.append(userName);
                } else {
                    user_id.append("," + userName);
                }
            }
        }
        if (TextUtils.isEmpty(user_id)) {
            callBack.updateEvent(users);
            return;
        }
        Call call = Http.getHttpService().GetUserPhotoInfo(groupId, user_id.toString(), CatUtils.getId(), CatUtils.getToken());
        call.enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                UserBean bean = response.body();
                if (bean.get_Status().equals("1")) {
                    //List<EaseUser> easeUsers = new ArrayList<EaseUser>();
                    for (int i = 0; i < bean.getUsers().size(); i++) {
                        EaseUser user = new EaseUser(bean.getUsers().get(i).getId());
                        user.setAvatar(bean.getUsers().get(i).getHeadImgUrl());
                        user.setNick(bean.getUsers().get(i).getMemo());
                        user.setMemo(bean.getUsers().get(i).getNickName());
                        String local_nickname = MySharedPreferences.getStorageFromSharedPreference(user.getUsername() + SharedPreferencesManager.USER_NICK_NAME);
                        String local_headimage = MySharedPreferences.getStorageFromSharedPreference(user.getUsername() + "_headimage");
                        String local_memo = MySharedPreferences.getStorageFromSharedPreference(user.getUsername() + "_memo");
                        if (!local_nickname.equals(user.getNick())) {
                            MySharedPreferences.StorageBySharedPreference(user.getUsername() + SharedPreferencesManager.USER_NICK_NAME, user.getNick());
                        }
                        if (!local_headimage.equals(user.getAvatar())) {
                            MySharedPreferences.StorageBySharedPreference(user.getUsername() + "_headimage", user.getAvatar());
                        }
                        if (!local_memo.equals(user.getMemo())) {
                            MySharedPreferences.StorageBySharedPreference(user.getUsername() + "_memo", user.getMemo());
                        }
                        users.put(bean.getUsers().get(i).getId(), user);
                        // easeUsers.add(user);
                    }
                   /* UserDao dao = new UserDao(BaseApplication.getInstance());
                    dao.saveContactList(easeUsers);*/
                    callBack.updateEvent(users);
                } else if (bean.get_Status().equals("-1")) {
                    SmallUtil.reLogin(context);
                } else {
                    callBack.updateEvent(users);
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {

            }
        });
    }


    //根据群组id获取头像昵称

    public static void updateGroup(final Context context, final String groupStr, final UpdateGroupCallBack callBack) {
        if (TextUtils.isEmpty(groupStr)) {
            callBack.updateEvent();
            return;
        }
        Call call = Http.getHttpService().GetGroupByIds(groupStr);
        call.enqueue(new Callback<GroupInfoBean>() {
            @Override
            public void onResponse(Call<GroupInfoBean> call, Response<GroupInfoBean> response) {
                GroupInfoBean bean = response.body();
                if (bean == null || bean.get_Status() == null)
                    return;
                if (bean.get_Status().equals("1")) {
                    for (int i = 0; i < bean.getGroupItems().size(); i++) {
                        String group_nick = SharedPreferencesManager.getInfo(bean.getGroupItems().get(i).getGroupId() + groupNick);
                        String group_head = SharedPreferencesManager.getInfo(bean.getGroupItems().get(i).getGroupId() + groupHead);
                        if (null != group_nick) {
                            SharedPreferencesManager.setInfo(bean.getGroupItems().get(i).getGroupId() + groupNick, bean.getGroupItems().get(i).getGroupName());
                        }
                        if (null != group_head) {
                            SharedPreferencesManager.setInfo(bean.getGroupItems().get(i).getGroupId() + groupHead, bean.getGroupItems().get(i).getGroupPhoto());
                        }
                    }
                    callBack.updateEvent();
                }/* else if (bean.get_Status().equals("-1")) {
                    SmallUtil.reLogin(context);
                    vProgressDialog.dismissProgressDlg();
                }*/ else {
                    callBack.updateEvent();
                }
            }

            @Override
            public void onFailure(Call<GroupInfoBean> call, Throwable t) {
            }
        });
    }

    public static String toUTF(String txt) {
        String result = "";
        try {
            result = URLEncoder.encode(txt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String fromUTF(String txt) {
        String result = "";
        try {
            result = URLDecoder.decode(txt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;

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

    //最后一个/后加入0.3-0
    public static String getThumbnailUrl(String s) {
        StringBuilder sb = new StringBuilder(s);
        String thumbUrl = String.valueOf(sb.insert(sb.lastIndexOf("/") + 1, "0.3-"));
        Logger.e(thumbUrl);
        return thumbUrl;
    }

    public interface OnToBottomListener {
        public void onToBottomEvent(View v);
    }

    public static void toBottom(final Context context, final ScrollView scrollView, final OnToBottomListener onToBottomListener) {
        View contentView = null;
        if (contentView == null) {
            contentView = scrollView.getChildAt(0);
        }
        final View finalContentView = contentView;
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            private int lastY = 0;
            private int touchEventId = -9983761;
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    View scroller = (View) msg.obj;
                    if (msg.what == touchEventId) {
                        if (lastY == scroller.getScrollY()) {
                            // 底部判断
                            if (finalContentView != null && finalContentView.getMeasuredHeight() <= scrollView.getScrollY() + scrollView.getHeight()) {
                                onToBottomListener.onToBottomEvent(scrollView);
                            }
                        } else {
                            handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 5);
                            lastY = scroller.getScrollY();
                        }
                    }
                }
            };

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.sendMessageDelayed(
                            handler.obtainMessage(touchEventId, v), 5);
                }
                return false;
            }
        });
    }


}
