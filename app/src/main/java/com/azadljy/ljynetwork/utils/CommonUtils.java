package com.azadljy.ljynetwork.utils;

import android.util.Base64;
import android.util.Log;

import com.azadljy.ljynetwork.utils.AES256;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CommonUtils {

//    private static SharedPreferences preferences = SharedPreferencesUtlis.getInstance().getSharedPreferences();
//    private static SharedPreferences.Editor editor = preferences.edit();

    public static String Md5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            System.out.println("result: " + buf.toString());//32位的加密
            System.out.println("result: " + buf.toString().substring(8, 24));//16位的加密
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }



    public static String aesEncrypt(String data, String key) {
        try {
            // base64加密
            String base64 = Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
            // aes加密
            return AES256.encrypt(base64, key);
        } catch (Exception e) {
            Log.e("base64加密错误", "：" + e);
        }
        return null;
    }
}
