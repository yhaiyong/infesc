package com.yhy;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.swing.*;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Playfair算法
 *用字符*替代字母 I/J,明文分组不够时补充字母 X
 * @author: 杨海勇
 **/
public class Playfair {
    static String key;//密钥
    static String plain;//明文
    static String cipher;//密文
    static char[][] KeyMatrix;//密钥矩阵
    static String[] formatP;//格式化后的明文
    static String[] formatC;//格式化后的密文
    static String cipherString;
    static int k = 0;

    /**
     * 构造密钥矩阵
     *
     * @param key
     * @return
     */
    public static char[][] getKeyMatrix(String key) {
        key = format(key);
        char[] array = key.toCharArray();//输入的密钥
        char[] arraykey = new char[array.length];
        String keykey = "";//去重复后的密钥
        char[] zimu = new char[26];//替换掉i、j后的26个字母
        String zimu1 = "";
        char[] letter = new char[26];//去掉密钥后的字母
        char[][] matrix = new char[5][5];

        //把i和j当作一个字母*
        for (int i = 0; i < 26; i++) {
            if ((char) (65 + i) == 'I') {
                zimu1 += '*';
                i++;
            } else if ((char) (65 + i) != 'J')
                zimu1 += (char) (65 + i);

        }
        zimu = zimu1.toCharArray();

        //密钥去重：如果有重复字母，只输入最后一次出现的字母,密钥中的i、j换成*
        for (int i = 0; i < array.length; i++) {
            int r = 0;
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] == array[j]) {
                    r = 1;
                    break;
                }
            }
            if (r == 0) {
                if (array[i] == 'I' || array[i] == 'J') {
                    keykey += '*';
                } else
                    keykey += array[i];
            }
        }
        arraykey = keykey.toCharArray();

        //去除密钥字母后剩下的字母
        int num = 0;
        for (int i = 0; i < zimu.length; i++) {
            int flag = 0;
            for (int j = 0; j < arraykey.length; j++) {
                if (zimu[i] == arraykey[j]) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                letter[num] = zimu[i];
                num++;
            }
        }

        int index = 0;
        int k = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                //先把密钥写入矩阵
                if (index < arraykey.length) {
                    matrix[i][j] = arraykey[index];
                    index++;
                }
                //把不重复的字母写入矩阵
                else if (k < letter.length) {
                    matrix[i][j] = letter[k];
                    k++;
                } else ;
            }
        }
        return matrix;
    }

    /**
     * 明文转化字母对
     *
     * @param plain
     * @return
     */
    public static String[] formatPlain(String plain) {
        Stack<Character> stack = new Stack<Character>();
        String[] dual_letter = new String[24];
        Character[] outs = new Character[2];
        char[] arrayplain1 = plain.toCharArray();
        String stringplain = "";
        for (int i = 0; i < arrayplain1.length; i++) {
            if (arrayplain1[i] != 'I') {
                if (arrayplain1[i] == 'J')
                    stringplain += '*';
                else
                    stringplain += arrayplain1[i];
            } else
                stringplain += '*';
        }
        char[] arrayplain = stringplain.toCharArray();
        int num = 0;
        int index = 0;
        for (int i = 0; i < arrayplain.length; i++) {
            dual_letter[index] = "";
            if (num < 2) {
                stack.push(arrayplain[i]);
                num++;
            }
            //最后剩下一个字母时
            if (i == arrayplain.length - 1 && num == 1) {
                stack.push('X');
                num = 2;
            }

            while (num == 2) {
                //如果字母对的两个字母是相同的情况
                if (arrayplain[i] == arrayplain[i - 1]) {
                    stack.pop();
                    stack.push('X');
                    i--;
                } else {
                    while (!stack.isEmpty()) {
                        for (int k = 1; k >= 0; k--) {
                            outs[k] = (Character) stack.pop();
                        }
                        for (int q = 0; q < outs.length; q++) {
                            //System.out.print(outs[q]);
                            dual_letter[index] += outs[q];
                        }
                        index++;
                    }
                    num = 0;
                    break;
                }
            }
        }
        return dual_letter;
    }

    /**
     * 格式化处理
     *
     * @param str
     * @return
     */
    public static String format(String str) {
        String regEx = "[^(A-Za-z)]";  //正则表达式
        Pattern p = Pattern.compile(regEx);   //
        Matcher getStr = p.matcher(str);
        str = getStr.replaceAll("").trim();
        str = str.toUpperCase();
        return str;
    }

    /**
     * 加密
     * @param plain
     * @param key
     * @return
     */
    public static String encode(String plain, String key) {

        plain = format(plain);
        key = format(key);
        KeyMatrix = getKeyMatrix(key);
        formatP = formatPlain(plain);

        cipherString = "";

        int m = 0;
        String[] EnCipher = new String[24];//加密后的密文
        int n = 0;


        while (formatP[m] != null) {
            EnCipher[n] = "";
            int[][] position = getPosition(formatP[m], KeyMatrix);
            int rowA = position[0][0];
            int colA = position[0][1];
            int rowB = position[1][0];
            int colB = position[1][1];
            int temp;

            //同一行
            if (rowA == rowB) {
                EnCipher[n] += KeyMatrix[rowA][(colA + 1) % 5];
                EnCipher[n] += KeyMatrix[rowA][(colB + 1) % 5];
            }
            //同一列
            else if (colA == colB) {
                EnCipher[n] += KeyMatrix[(rowA + 1) % 5][colA];
                EnCipher[n] += KeyMatrix[(rowB + 1) % 5][colA];
            } else {
                temp = colA;
                colA = colB;
                colB = temp;
                EnCipher[n] += KeyMatrix[rowA][colA];
                EnCipher[n] += KeyMatrix[rowB][colB];
            }
            cipherString += EnCipher[n];
            n++;
            m++;
        }

        return cipherString;
    }

    /**
     * 字母位置
     *
     * @param dual
     * @param KeyMatrix
     * @return
     */
    public static int[][] getPosition(String dual, char[][] KeyMatrix) {
        //位置矩阵的值代表字母在密钥矩阵中的行和列
        //[0][0]:字母A的行;[0][1]:字母A的列;
        //[1][0]:字母B的行;[1][1]:字母B的列;

        int[][] position = new int[2][2];
        //确保是双字母的情况
        if (dual.length() == 2) {
            char a = dual.charAt(0);
            char b = dual.charAt(1);
            for (int i = 0; i < KeyMatrix.length; i++) {
                for (int j = 0; j < KeyMatrix[0].length; j++) {
                    if (a == KeyMatrix[i][j]) {
                        position[0][0] = i;
                        position[0][1] = j;
                    }
                    if (b == KeyMatrix[i][j]) {
                        position[1][0] = i;
                        position[1][1] = j;
                    }
                }
            }
        }
        return position;
    }

    /**
     * 格式化密文
     * @param cipher
     * @return
     */
    public static String[] formatCipher(String cipher) {
        String[] arrayC = new String[cipher.length()];
        cipher = cipher.toUpperCase();
        int index = 0;
        if (cipher.length() % 2 == 0) {
            for (int i = 0; i < cipher.length() - 1; index++, i += 2) {
                arrayC[index] = cipher.substring(i, i + 2);
            }
        } else {
            JOptionPane.showMessageDialog(null, "密文不正确，请输入正确密文！");
            k++;
            decode(cipher, key);
        }

        return arrayC;
    }

    /**
     * 解密
     * @param cipher
     * @param key
     * @return
     */
    public static String decode(String cipher, String key) {

        plain = "";
        cipher = format(cipher);
        key = format(key);
        formatC = formatCipher(cipher);
        if (k == 0) {
            KeyMatrix = getKeyMatrix(key);
        }


        int m = 0;
        String[] DeCipher = new String[24];//解密后的明文
        int n = 0;

        while (formatC[m] != null) {
            DeCipher[n] = "";
            int[][] position = getPosition(formatC[m], KeyMatrix);
            int rowA = position[0][0];
            int colA = position[0][1];
            int rowB = position[1][0];
            int colB = position[1][1];
            int temp;

            //同一行
            if (rowA == rowB) {
                if (colA == 0)
                    colA += 5;
                if (colB == 0)
                    colB += 5;
                DeCipher[n] += KeyMatrix[rowA][(colA - 1) % 5];
                DeCipher[n] += KeyMatrix[rowA][(colB - 1) % 5];
            }
            //同一列
            else if (colA == colB) {
                if (rowA == 0)
                    rowA += 5;
                if (rowB == 0)
                    rowB += 5;
                DeCipher[n] += KeyMatrix[(rowA - 1) % 5][colA];
                DeCipher[n] += KeyMatrix[(rowB - 1) % 5][colA];
            } else {
                temp = colA;
                colA = colB;
                colB = temp;
                DeCipher[n] += KeyMatrix[rowA][colA];
                DeCipher[n] += KeyMatrix[rowB][colB];
            }
            plain +=DeCipher[n];
            n++;
            m++;
        }
        return plain;

    }


}
