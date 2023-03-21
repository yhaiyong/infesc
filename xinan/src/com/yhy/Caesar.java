package com.yhy;

import com.yhy.tools.Count;
import javax.swing.*;

/**
 * Caesar算法
 * 字符频率统计在控制台输出
 *
 * @author: 杨海勇
 **/
public class Caesar extends JFrame {
    static String plain;//明文
    static String cipher;//密文

    /**
     * 加密
     * @param data
     * @param k
     * @return
     */
     public static String encrypt(String data, int k) {
        StringBuilder result = new StringBuilder();
        for (char c : data.toCharArray()) {
            // 如果字符串中的某个字符是小写字母
            if (c >= 'a' && c <= 'z') {
                c += k % 26 - 32; // 移动key%26位
                if (c < 'A')
                    c += 26; // 向左超界
                if (c > 'Z')
                    c -= 26; // 向右超界
            }
            // 如果字符串中的某个字符是大写字母
            else {
                JOptionPane.showMessageDialog(null, "输入错误，加密内容为小写字母 ");
                return null;
            }
            result.append(c);
        }
        cipher = result.toString();
        return cipher;
    }

    /**
     * 解密
     * @param data
     * @param k
     * @return
     */
    public static String decrypt(String data, int k) {
        k = 0 - k;
        StringBuilder result = new StringBuilder();
        for (char c : data.toCharArray()) {
            // 如果字符串中的某个字符是大写字母
            if (c >= 'A' && c <= 'Z') {
                c += k % 26 + 32; // 移动key%26位
                if (c < 'a')
                    c += 26;// 同上
                if (c > 'z')
                    c -= 26;// 同上
            }
            // 如果字符串中的某个字符不是大写字母
            else {

                JOptionPane.showMessageDialog(null, "输入错误，解密内容只能为大写字母 ");
                return null;

            }
            result.append(c);

        }

        plain = result.toString();
        return plain;
    }
}


