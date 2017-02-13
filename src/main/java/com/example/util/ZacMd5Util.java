package com.example.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.catalina.util.HexUtils;

public class ZacMd5Util {
    // 为什么说 MD5 是不可逆的？
    // https://www.zhihu.com/question/22651987

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String zacKey = "test";
        String startDate = "2016-08-01";
        String endDate = "2017-12-31";
        String pageNo = "0";
        String content = "{\"order_status\":\"1\",\"date_type\":\"1\",\"start_time\":\"" + startDate + "\",\"end_time\":\"" + endDate
                + "\",\"page_no\":\"" + pageNo + "\",\"page_size\":\"100\"}";

        System.err.println("Content:[" + content + "]");
        String hexString = string2Md5((content + zacKey), "UTF-8");
        System.err.println("MD5:[" + hexString + "]");

    }

    /*
     * @param content 待加密的字符串
     * 
     * @param formatName Unicode Transformation Format
     * 
     * @return md5加密后的字符串
     */
    public static String string2Md5(String content, String formatName) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(content.getBytes(formatName));
            return HexUtils.convert(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.err.println(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println(e);
        }
        return null;
    }
}