package vip.mengqin.diandezhun.manager;

import android.util.Base64;
import com.android.baselibrary.tool.Log;
import com.android.baselibrary.tool.SPUtil;
import com.android.baselibrary.util.Util;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import vip.mengqin.diandezhun.MyApplication;
import vip.mengqin.diandezhun.constant.SPConstants;

public class SignManager {

    public static Map<String, Object> getHead() {
        Map<String, Object> headers = new HashMap<>();

        String timeDeltaStr = SPUtil.getStringValue(MyApplication.context, SPConstants.SysTimeDelta, "0");
        long timestamp = System.currentTimeMillis() / 1000 - Integer.parseInt(timeDeltaStr);
        String uuid = Util.getUUID(MyApplication.context);
        String secret = "375D6A9C31569E508DBB6D58630DBA7B";

        // 测试数据
//        timestamp = 1571640977;// 时间戳
//        uuid = "5d7db9ef-6451-47ac-9c30-6b666dfdb7ec"; // UUID
//        secret = "375D6A9C31569E508DBB6D58630DBA7B";// 秘钥

//        timestamp = 1618912227;// 时间戳
//        uuid = "eett56hn_f026_3f45_939b_ffa2a8drrt"; // UUID
//        secret = "375D6A9C31569E508DBB6D58630DBA7B";// 秘钥

        uuid = UUID.randomUUID().toString();


        String singature = getSignature(timestamp, uuid, secret);

        Log.i("OkGo","timestamp:" + timestamp);
        Log.i("OkGo","uuid:" + uuid);
        Log.i("OkGo","secret:" + secret);
        Log.i("OkGo","singature:" + singature);


        headers.put("Api-Version", "V1");
        headers.put("Api-Stage", "TEST");
        headers.put("App-Timestamp", timestamp);
        headers.put("App-Nonce", uuid);
        headers.put("App-Sign", encodeHeadInfo(singature));

        return headers;
    }

    public static Map<String, Object> getSimpleHead() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Api-Version", "V1");
        headers.put("Api-Stage", "TEST");
        return headers;
    }



    private static String encodeHeadInfo(String headInfo) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0, length = headInfo.length(); i < length; i++) {
            char c = headInfo.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                stringBuffer.append(String.format("\\u%04x", (int) c));
            } else {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }


    public static String HMACSHA256(String data, String key) {
        StringBuilder sb = new StringBuilder();
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString().toUpperCase();
    }

    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    /**
     * 获取签名
     *
     * @param timestamp 时间戳
     * @param uuid      uuid
     * @param secret    秘钥
     * @return 加密后的签名
     */
    private static String getSignature(long timestamp, String uuid, String secret) {
        // 加密结果
        String signature = null;

        try {
            String sign = "";
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 获取 sign 的字节数组
            byte[] bySignStr = (timestamp + uuid).getBytes();

            // 对字节数组处理
            md.update(bySignStr);

            // 进行哈希运算
            byte[] byResult = md.digest();

            sign = byteToString(byResult);
            // 打印 sign 的值
            System.out.println(sign);

            // Sha256加密
            String hashStr = "";
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");

            byte[] bySecret = secret.getBytes();
            byte[] bySign = sign.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(bySecret, "HmacSHA256");
            sha256HMAC.init(secretKeySpec);

            byte[] hashs = sha256HMAC.doFinal(bySign);

            hashStr = byteToString(hashs);
            // 打印加密字符串
            Log.i("hashs:" + hashStr);

            // Base64 编码
            signature = Base64.encodeToString(hashs, Base64.NO_WRAP);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return signature;
    }

    /**
     * 字节数组转换字符串
     *
     * @param bts 要转换的字节数组
     * @return 转换后的字符串
     */
    private static String byteToString(byte[] bts) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bts) {
            int bt = b & 0xff;
            if (bt < 16) {
                sb.append(0);
            }
            sb.append(Integer.toHexString(bt));
        }

        return sb.toString();
    }

}
