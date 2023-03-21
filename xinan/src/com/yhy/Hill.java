package com.yhy;

import com.yhy.tools.Count;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Hill密码加密算法
 * @author: 杨海勇
 */
public class Hill {
    public static int m ;


		/* 课本测试案例：
		 	{17 17 5}
		k =	{21 18 21}
			{2 2 19}
        明文：
		pay more money
		*/

    /**
     * 加密算法
     */
    public static String encrypt(String plain,int[][] keyMatrix) {

        m = keyMatrix.length;
        int[][] k = keyMatrix;

        //获取明文，将明文中的空格删去，并全部转换为大写字母字符数
        String plaintext = plain;
        char[] plaintextCharArray = plaintext.replaceAll(" ", "").toUpperCase().toCharArray();

        //将字符数组中所有内容添加进ArrayList中
        ArrayList<Character> list = new ArrayList<Character>();
        for (int i = 0; i < plaintextCharArray.length; i++) list.add(plaintextCharArray[i]);

        //明文不足按m个字母一组分组时补字母x
        while (list.size() % m != 0) list.add('x');

        //输出密文 加密
        String cipher = new String();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size() + m; i = i + m) {

            if(i>=m)i = i-m;
            char[] p = new char[m];
            char[] c = new char[m];

            int j = 0;
            while (true) {
                p[j] = list.get(i);
                i++;
                j++;
                if (j >= m) break;
            }

            int j1 = 0;
            while (true) {
                int sum = 0;
                for (int t = 0; t < m; t++) {
                    sum = sum + (k[j1][t] * (p[t] - 65));
                }
                c[j1] = (char) (sum % 26 + 65);
                j1++;
                if (j1 >= m) break;
            }
            String str1 = new String(c);
            cipher=cipher.concat(str1);

        }
        return cipher;
    }

    /**
     * 一维数组转二维数组
     * @param data
     * @param m
     * @return
     */
    public static int[][] oneToTwo(int[] data,int m){
        int[][] keymatrix = new int[m][m];
        int k = 0;
        int hang = keymatrix.length;
        int lie = keymatrix[0].length;
        for (int i = 0; i < hang; i++) {
            for (int j = 0; j < lie; j++) {
                keymatrix[i][j] = data[k];
                k++;
            }
        }
        return keymatrix;
    }

    /**
     * 一维字符数组转二维字符数组
     * @param data
     * @param m
     * @return
     */
    public static char[][] oneToTwoChar(char[] data,int m){
        char[][] charmatrix = new char[m][m];
        int k = 0;
        int hang = charmatrix.length;
        int lie = charmatrix[0].length;
        for (int i = 0; i < hang; i++) {
            for (int j = 0; j < lie; j++) {
                charmatrix[i][j] = data[k];
                k++;
            }
        }
        return charmatrix;
    }

}

