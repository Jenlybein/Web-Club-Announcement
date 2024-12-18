package com.announce.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5Util {

    /**
     * 对给定的字符串进行 MD5 加密
     *
     * @param strSrc 需要加密的源字符串
     * @return 返回加密后的 MD5 字符串（十六进制表示）
     */
    public static String encrypt(String strSrc) {
        try {
            // 十六进制字符集，用于将 MD5 加密后的字节转换为十六进制字符
            char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                    '9', 'a', 'b', 'c', 'd', 'e', 'f' };

            // 获取字符串的字节数组
            byte[] bytes = strSrc.getBytes();

            // 获取 MD5 MessageDigest 实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 更新 MD5 消息摘要
            md.update(bytes);

            // 获取 MD5 加密后的字节数组
            bytes = md.digest();

            // 结果字节数组的长度
            int j = bytes.length;

            // 用于存储最终的 MD5 十六进制字符串
            char[] chars = new char[j * 2];

            int k = 0;
            // 将字节数组转化为十六进制字符
            for (byte b : bytes) {
                // 获取高四位和低四位，转换为十六进制字符
                chars[k++] = hexChars[b >>> 4 & 0xf];  // 高四位
                chars[k++] = hexChars[b & 0xf];         // 低四位
            }

            // 返回 MD5 加密后的十六进制字符串
            return new String(chars);
        } catch (NoSuchAlgorithmException e) {
            // 打印异常堆栈信息并抛出运行时异常
            e.printStackTrace();
            throw new RuntimeException("MD5加密出错！！+" + e);
        }
    }
}
