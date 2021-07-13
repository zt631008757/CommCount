package com.android.baselibrary.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.tool.SPUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/6/12.
 */

public class Util {

    /**
     * 获取对象反射类型
     */
    public static Type getReflectType(Object object) {
        Type type;
        Type[] types = object.getClass().getGenericInterfaces();
        if (types.length > 0) {
            // 如果这个监听对象是直接实现了接口
            type = ((ParameterizedType) types[0]).getActualTypeArguments()[0];
        } else {
            // 如果这个监听对象是通过类继承
            type = ((ParameterizedType) object.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return type;
    }

    static String[] goodImgs = {
            "https://www.ikea.cn/category-images/Category_sofas-and-armchairs.jpg?imwidth=300",
            "https://www.ikea.cn/ext/ingkadam/m/61ffc30f8edb181c/original/PH172132-crop003.jpg?f=xs",
            "https://www.ikea.cn/cn/zh/images/products/strandmon-si-jia-meng-kao-bei-yi-si-ke-te-bo-huang-se__0837297_PE601176_S5.JPG?f=xxs",
            "https://www.ikea.cn/revamp/childrens-desks_24714.jpg?imwidth=300",
            "https://www.ikea.cn/revamp/childrens-chests-of-drawers_18708.jpg?imwidth=300",
            "https://www.ikea.cn/revamp/decorative-accessories_24924.jpg?imwidth=300",
            "https://www.ikea.cn/category-images/Category_vases-and-bowls.jpg?imwidth=300",
            "https://www.ikea.cn/ext/ingkadam/m/164a00bb513632d7/original/PH171286-crop001.jpg?f=xs",
            "https://www.ikea.cn/cn/zh/images/products/pjaetteryd-pi-ya-te-tu-pian-cai-se-xun-lu__0689874_PE723123_S5.JPG?f=xxs",
            "https://www.ikea.cn/category-images/Category_childrens-boxes-and-baskets.jpg?imwidth=500",
            "https://www.ikea.cn/ext/ingkadam/m/3e3dc77eda5453c/original/PH173338-crop001.jpg?f=xs",
            "https://www.ikea.cn/images/jia1-ju1-yong4-pin3-b7571f84430217bc546ce5ebd5657127.jpg?f=xs",
            "https://www.ikea.cn/ext/ingkadam/m/58e107bc54ff5cf2/original/PH171927-crop001.jpg?f=xs",
            "https://www.ikea.cn/ext/ingkadam/m/1dc1af1325642950/original/PH175423-crop001.jpg?f=xs",
            "https://www.ikea.cn/ext/ingkadam/m/5c703b8229836cc7/original/PH174039-crop001.jpg?f=xl"
    };

    static String[] headImgs = {
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201712%2F16%2F20171216164236_HMGVu.gif&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612974260&t=7fc9ded801bd4303548b3fcc01e808ed",
            "data:image/jpeg;base64,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",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1812993978,4158651947&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2752148155,155093617&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1549131754,2955370505&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3335200364,664435613&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3461339905,3407964579&fm=11&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=249424419,860932438&fm=11&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2184463790,438646211&fm=11&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3156043369,3023538154&fm=11&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3815481620,1710167837&fm=11&gp=0.jpg"
    };

    public static String getGoodsImg()
    {
        int random = new Random().nextInt(goodImgs.length);
        return goodImgs[random];
    }

    public static String getHeadImg()
    {
        int random = new Random().nextInt(headImgs.length);
        return headImgs[random];
    }



    // 计算出该TextView中文字的长度(像素)
    public static float getTextViewLength(TextView textView, String text) {
        TextPaint paint = textView.getPaint();
        // 得到使用该paint写上text的时候,像素为多少
        float textLength = paint.measureText(text);
        return textLength;
    }

    public static int random(int start, int end) {
        //创建Random类对象
        Random random = new Random();
        //产生随机数
        int number = random.nextInt(end - start + 1) + start;
        return number;
    }

    /**
     * 判断是否安装了QQ
     *
     * @param context
     * @return
     */
    public static boolean isQQClientInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName.toLowerCase(Locale.ENGLISH);
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }


    //处理账号， 带*号
    public static String getAccount(String account) {
        String str = "";
        if (!TextUtils.isEmpty(account)) {
            if (account.length() == 11)   //手机号
            {
                str = account.substring(0, 3) + "****" + account.substring(7, 11);
            } else if (account.contains("@")) {
                int index = account.indexOf("@");
                if (index > 3) {
                    str = account.substring(0, 3) + "****" + account.substring(index);
                } else {
                    str = "****" + account.substring(index);
                }
            } else if (account.length() >= 15) {
                str = account.substring(0, 4) + " **** **** " + account.substring(account.length() - 3);
            } else if (account.length() >= 4) {
                str = account.substring(0, 3) + "****" + account.substring(account.length() - 3);
            } else {
                str = account;
            }
        }
        return str;
    }


    //数字显示处理，去掉末尾的0
    public static String numClearZero(String num) {
        if (TextUtils.isEmpty(num)) {
            num = "";
        } else {
            if (num.contains(".")) {
                while (num.endsWith("0")) {
                    num = num.substring(0, num.length() - 1);
                }
                while (num.endsWith(".")) {
                    num = num.substring(0, num.length() - 1);
                }
            }
        }
        return num;
    }


    //数字显示处理，保留几位小数点   length: 几位小数点
    public static String numDecimalFormat(String num, int length) {
        try {
            String decimalStr = "0";
            if (length > 0) {
                decimalStr += ".";
                for (int i = 0; i < length; i++) {
                    decimalStr += "0";
                }
            }
            DecimalFormat format = new DecimalFormat(decimalStr);
            String a = format.format(new BigDecimal(num));
            return a;
        } catch (Exception e) {
            return num;
        }
    }

    public static void closeApp() {
//        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static String getUUID(Context context) {
        String uuid = SPUtil.getStringValue(context, "PREFS_DEVICE_ID", "");
        if (TextUtils.isEmpty(uuid)) {
            final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            try {
                if (!"9774d56d682e549c".equals(androidId)) {
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                } else {
                    @SuppressLint("MissingPermission") final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            SPUtil.putValue(context, "PREFS_DEVICE_ID", uuid);
        }
        return uuid.replace("-", "_");
    }


    /**
     * 获取app当前的渠道号或application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String channelNumber = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelNumber = applicationInfo.metaData.get(key) + "";
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelNumber;
    }


    public static boolean isWifi(Context context) {
        if (getNetworkClass(context) == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else {
            return false;
        }
    }

    private static int getNetworkClass(Context context) {
        int networkType = ConnectivityManager.TYPE_MOBILE;
        try {
            final NetworkInfo network = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (network != null && network.isAvailable() && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = ConnectivityManager.TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    networkType = telephonyManager.getNetworkType();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return networkType;
    }


    //万 级转换
    public static String numberFormat(int num) {
        String numStr = num + "";
        if (num >= 10000) {
            DecimalFormat format = new DecimalFormat("0.0");
            numStr = format.format(num / 10000) + "万";
        }
        return numStr;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void copy(Context context, String msg, boolean... ishowTost) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("code", msg);
        clipboard.setPrimaryClip(clip);
        if (ishowTost.length > 0 && (boolean) ishowTost[0]) {
            CommToast.showToast(context, "已复制");
        }
    }

    /**
     * 获取手机大小（分辨率）
     */
    public static DisplayMetrics getScreenPix(Activity activity) {
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
        return displaysMetrics;
    }

    /**
     * 判断SDCard是否可用
     */
    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 获取手机状态栏高度
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static List<PackageInfo> getAppList(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        // 判断是否系统应用：
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo pak = (PackageInfo) packageInfoList.get(i);
            //判断是否为系统预装的应用
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // 第三方应用
                apps.add(pak);
            } else {
                //系统应用
            }
        }
        return apps;
    }

    //启动一个app
    public static void startAPP(Context context, String appPackageName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
        } catch (Exception e) {
            CommToast.showToast(context, "没有安装");
        }
    }


    /**
     * 设置键盘展现和关闭
     *
     * @param bShow
     * @param context
     * @param view
     */
    public static void showKeyBoard(boolean bShow, Context context, View view) {
        if (bShow) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } else {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    //手机号检查
    public static boolean isChinaPhoneLegal(String str) {
        String regExp = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 获取版本号名称
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本号code
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //通过反射获取类的属性和属性值
    public static Map<String, String> getFieldMap(Object object) {
        Map<String, String> map = new HashMap<>();
        Field fields[] = object.getClass().getDeclaredFields();//cHis 是实体类名称
        try {
            Field.setAccessible(fields, true);
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                String value = "";
                if (fields[i].get(object) != null) {
                    value = fields[i].get(object).toString();
                }
                map.put(name, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    public static File uri2File(Context context, Uri uri) {
        String img_path;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (actualimagecursor == null) {
            img_path = uri.getPath();
        } else {
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            img_path = actualimagecursor
                    .getString(actual_image_column_index);
        }
        File file = new File(img_path);
        return file;
    }

    //压缩图片
    public static File compressImageToFile(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (bitmap == null) {
            return null;
        }
        // 0-100 100为不压缩
        int options = 50;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        try {
            File temp_file = new File(Environment.getDataDirectory(), "temp_pic.jpg");
            FileOutputStream fos = new FileOutputStream(temp_file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            return temp_file;
        } catch (Exception e) {
            e.printStackTrace();
            return file;
        }
    }

    /**
     * 将字符串的集合用逗号间隔拼接为字符串
     *
     * @param strList 字符串的集合
     * @return
     */
    public static String listToString(List<String> strList) {
        if (strList == null || strList.isEmpty()) {
            return null;
        }
        String str = "";
        for (String s : strList) {
            str = (TextUtils.isEmpty(str) ? s : str + "," + s);
        }
        return str;
    }

    //double科学计算法处理  maxLenth最高保留几位 minLength最少保留几位
    public static String formatDouble(double d, int maxLenth, int minLength) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(minLength);
        //设置保留多少位小数
        nf.setMaximumFractionDigits(maxLenth);
        // 取消科学计数法
        nf.setGroupingUsed(false);
        //返回结果
        return nf.format(d);
    }


    //double科学计算法处理  maxLenth最高保留几位 minLength最少保留几位
    public static String formatDouble(double d, int maxLenth, int minLength, boolean isUp, boolean isDown) {
        String s = d + "";
        if (!TextUtils.isEmpty(s) && s.contains(".")) {
            int a = s.length() - s.indexOf(".") - 1;
            if (a > 2) { //小数位大于2向上取整
                if (isUp) {
                    d = new BigDecimal(d).setScale(3, BigDecimal.ROUND_DOWN).doubleValue();
                    BigDecimal b = new BigDecimal(d);
                    d = b.setScale(2, BigDecimal.ROUND_UP).doubleValue();
                }
            }
            if (a > 5) {
                if (isDown) {
                    BigDecimal b = new BigDecimal(d);
                    d = b.setScale(5, BigDecimal.ROUND_DOWN).doubleValue();
                }
            }
        }

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(minLength);
        //设置保留多少位小数
        nf.setMaximumFractionDigits(maxLenth);
        // 取消科学计数法
        nf.setGroupingUsed(false);
        //返回结果
        return nf.format(d);
    }

    /**
     * 控制输入框输入数字保留小数点位数  在addTextChangedListener里面调用
     *
     * @param s     Editext  CharSequence
     * @param et    Editext 对象
     * @param digit 保留几位小数
     */
    public static void mangeEditextDigit(CharSequence s, EditText et, int digit) {
        String str = s.toString();
        int len = s.length();
        if (str.contains(".")) {
            //控制小数点后面位数
            if (len - 1 - str.indexOf(".") > digit) {
                s = str.subSequence(0,
                        str.indexOf(".") + digit + 1);
                et.setText(s.toString());
                et.setSelection(s.length());
            }
        }
        if (str.substring(0).equals(".")) {
            //输入点，自动补充成0.
            s = "0" + s;
            et.setText(s.toString());
            et.setSelection(s.length());
        }

        if (str.startsWith("0")
                && len > 1) {
            //以0开头不允许输入连续的0如00，000
            if (!str.substring(1, 2).equals(".")) {
                et.setText(s.subSequence(0, 1).toString());
                et.setSelection(s.subSequence(0, 1).toString().length());
            }
        }
        if (len - str.replace(".", "").length() > 1) {
            //不允许输入1个以上的小数点
            et.setText(s.subSequence(0, len - 1).toString());
            et.setSelection(s.subSequence(0, len - 1).toString().length());
        }
    }

    //至少包含两个点
    public static boolean containMorethanTwoPoint(String str) {
        Pattern pattern = Pattern.compile("^.*[.].*[.].*$");
        return pattern.matcher(str).matches();
    }

    //禁止重复点击
    public static void unallowQuickClick(final View view) {
        view.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 1000);
    }


    //禁止重复点击
    public static void unallowQuickClickWithTimes(final View view, long mills) {
        view.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, mills);
    }


    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context, String phoneNum) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            context.startActivity(intent);
        } catch (Exception e) {
            CommToast.showToast(context, "您的设备不支持打电话");
        }
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return by Hankkin at:2015-10-07 21:15:59
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return by Hankkin at:2015-10-07 21:16:13
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 随机指定范围内N个不重复的数 在初始化的无重复待选数组中随机产生一个数放入结果中，
     * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换 然后从len-2里随机产生下一个随机数，如此类推
     *
     * @param max 指定范围最大值
     * @param min 指定范围最小值
     * @param n   随机数个数
     * @return int[] 随机数结果集
     */
    public static List<Integer> randomArray(int min, int max, int n) {
        int len = max - min + 1;
        if (max < min || n > len) {
            return null;
        }
        // 初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }
        int[] result = new int[n];
        List<Integer> listNumber = new ArrayList<>();
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            // 待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            // 将随机到的数放入结果集
            result[i] = source[index];
            // 将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
            listNumber.add(result[i]);
        }
        return listNumber;
    }


}
