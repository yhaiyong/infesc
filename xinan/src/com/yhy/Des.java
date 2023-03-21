package com.yhy;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/***
 * DES算法
 * @author: 杨海勇
 **/

public class Des {
    public static void main(String[] args) {
        //密钥
        byte[] des_key = new byte[8];

        //明文
        byte[] des_input = new byte[8];

        //默认明文为1111111111111111
        //默认密钥为1111111111111111
        for (int i = 0; i < 8; i++) {
            des_key[i] = 0x11;
            des_input[i] = 0x11;
        }

        //加密后的密文为F40379AB9E0EC533
        byte[] des_output = encrypt(des_key, des_input);
        System.out.println("=============================================Des算法==============================================");

       System.out.println("明文：1111111111111111");
       System.out.println("密钥：1111111111111111");
        //输出加密结果
        System.out.println("密文为： " + byteArrayToHex(des_output));

        BitsArray key = new BitsArray(des_key);
        BitsArray input = new BitsArray(des_input);
        BitsArray output = new BitsArray(des_output);


        //选择想改变位数的是明文或者密钥（当输入其他数值时自动终止程序）
        System.out.println("实现功能==================================================================");
        System.out.println("       1.给定某个Sbox的输入差分情况下，计算所有输入对和所有Sbox输出差分的分布情况 ");
        System.out.println("       2.统计DES算法在密钥固定情况，输入明文改1-64位时，输出密文位数的变化情况。");
        System.out.println("       3.统计DES算法在明文固定情况，输入密钥改1-64位时，输出密文位数的变化情况。");
        System.out.print("请选择：");
        Scanner scan1 = new Scanner(System.in);
        int select = scan1.nextInt();
        if (select > 3 || select < 1) {
            System.out.println("输入不合法，请输入正确的数字：");
            System.exit(0);
        }

        if (select == 1) {
            //差分表构造
            differMain();
        } else if (select == 2) {
            //分别计算改变1~64位时的平均改变位数并输出
            for (int bits = 1; bits <= 64; bits++) {
                int count = 0; //记录总改变的位数的数量

                //总共进行十次测试
                for (int j = 0; j < 10; j++) {
                    //利用Set中元素不能重复的特性随机得到要修改位所在的位置
                    HashSet<Integer> hs = new HashSet<Integer>();
                    for (int i = 0; i < bits; i++) {
                        while (hs.size() == i) hs.add((int) (Math.random() * 64));
                    }

                    //将元素放进ArrayList以供调用
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    for (int i : hs) list.add(i);

                    //克隆一份原明文的备份
                    BitsArray inputCopy = input.clone();

                    //对明文进行指定位数的修改
                    for (int i = 0; i < bits; i++) {
                        int pos = list.get(i); //要修改的位的位置

                        if (input.toString().charAt(pos) == '0') input.setOne(pos);
                        else if (input.toString().charAt(pos) == '1') input.setZero(pos);
                    }

                    //将修改后的明文输出为byte数组
                    des_input = input.toByteArray();

                    //使用修改后的明文和原来的密钥进行加密运算，得到新的密文byte数组
                    byte[] des_newOutput = encrypt(des_key, des_input);

                    //将新的密文byte数组转化为位串对象
                    BitsArray newOutput = new BitsArray(des_newOutput);

                    //与原来输出的密文的位串进行异或操作
                    newOutput.xor(output);

                    //计算异或之后位串中1的个数，即为改变的位数
                    count += newOutput.OnesCount();

                    //重置已被修改的明文为原明文
                    input = inputCopy;
                }

                System.out.println("明文改变位数：" + bits + ". 密文平均改变位数：" + ((double) count / 10));
            }
        } else if (select == 3) {
            //分别计算改变1~64位时的平均改变位数并输出
            for (int bits = 1; bits <= 64; bits++) {
                int count = 0; //记录总改变的位数的数量

                //总共进行十次测试
                for (int j = 0; j < 10; j++) {
                    //利用Set中元素不能重复的特性随机得到要修改位所在的位置
                    HashSet<Integer> hs = new HashSet<Integer>();
                    for (int i = 0; i < bits; i++) {
                        while (hs.size() == i) hs.add((int) (Math.random() * 64));
                    }

                    //将元素放进ArrayList以供调用
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    for (int i : hs) list.add(i);

                    //克隆一份原密钥的备份
                    BitsArray keyCopy = key.clone();

                    //对密钥进行指定位数的修改
                    for (int i = 0; i < bits; i++) {
                        int pos = list.get(i); //要修改的位的位置

                        if (key.toString().charAt(pos) == '0') key.setOne(pos);
                        else if (key.toString().charAt(pos) == '1') key.setZero(pos);
                    }

                    //将修改后的密钥输出为byte数组
                    des_key = key.toByteArray();

                    //使用修改后的密钥和原来的明文进行加密运算，得到新的密文byte数组
                    byte[] des_newOutput = encrypt(des_key, des_input);

                    //将新的密文byte数组转化为位串对象
                    BitsArray newOutput = new BitsArray(des_newOutput);

                    //与原来输出的密文的位串进行异或操作
                    newOutput.xor(output);

                    //计算异或之后位串中1的个数，即为改变的位数
                    count += newOutput.OnesCount();

                    //重置已被修改的密钥为原密钥
                    key = keyCopy;
                }

                System.out.println("密钥改变位数：" + bits + ".密文平均改变位数：" + ((double) count / 10));
            }
        }

        scan1.close();
        //scan2.close();
    }

    /**
     * 将字节数组输出为16进制串
     *
     * @param bs 所需转换的字节数组
     * @return 转换得到的16进制字符串
     */
    public static String byteArrayToHex(byte[] bs) {
        StringBuilder res = new StringBuilder();

        for (byte b : bs) res.append(String.format("%02X", (b & 0xff)));

        return res.toString();
    }

    /**
     * DES加密算法
     *
     * @param des_key   密钥
     * @param des_input 明文输入
     * @return 密文输出
     */
    public static byte[] encrypt(byte[] des_key, byte[] des_input) {
        //DES加密算法
        Cipher des = null;

        //加密后的输出
        byte[] des_output = null;

        //创建DES密钥
        SecretKey secretKey = new SecretKeySpec(des_key, "DES");

        //创建DES密码算法对象，指定电码本模式和无填充方式
        try {
            des = Cipher.getInstance("DES/ECB/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        //初始化DES算法
        try {
            des.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        //加密
        try {
            des_output = des.doFinal(des_input);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return des_output;
    }

    /**
     * 差分构造主函数
     */
    public static void differMain() {
        System.out.println("=======================差分分析=========================");
        System.out.print("指定输入差分（格式：1 0 1 1 0 1）：");
        Scanner sc = new Scanner(System.in);
        int[] b = new int[6];
        for (int i = 0; i < b.length; i++) {
            b[i] = sc.nextInt();
        }

        int[] In = new int[6];
        for (int i = 0; i < 6; i++) {
            In[i] = b[i];
        }

        /*for (int k = 0; k <= 5; k++) {                     //打印当前输入异或
            System.out.print(In[k]);
        }*/
        differ(In);                       //调用差分方法
    }

    /**
     * 差分构造函数
     *
     * @param In
     */
    public static void differ(int[] In) {   //定义s盒

        int[][] s = new int[][]{};
        //S盒
        int[][] s1 = new int[][]{{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7}
                , {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8}
                , {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0}
                , {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}};
        int[][] s2 = new int[][]{{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}};
        int[][] s3 = new int[][]{{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}};

        int[][] s4 = new int[][]{{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}};

        int[][] s5 = new int[][]{{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}};

        int[][] s6 = new int[][]{{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}};
        int[][] s7 = new int[][]{{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}};
        int[][] s8 = new int[][]{{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};

        int[] b = new int[6]; //可能的输入
        int[] d = new int[6];
        //n[4]是输出差分0000~1111；
        int[] n = new int[4];
        int[] e = new int[4];
        int[] f = new int[4];
        int[] Out = new int[4];
        int flag;
        int l;
        int k;
        int i, j;
        int count;    //定义一个计数器
        System.out.print("请选择S盒（1-8）：");
        Scanner scanner = new Scanner(System.in);
        int select = scanner.nextInt();
        while (select < 1 || select > 8) {
            System.out.print("s输入错误，请重新输入：");
            select = scanner.nextInt();
        }
        switch (select) {
            case 1:
                s = s1;
                break;
            case 2:
                s = s2;
                break;
            case 3:
                s = s3;
                break;
            case 4:
                s = s4;
                break;
            case 5:
                s = s5;
                break;
            case 6:
                s = s6;
                break;
            case 7:
                s = s7;
                break;
            case 8:
                s = s8;
                break;
        }

        System.out.println("==============================================================");
        System.out.print("S" + select +"的输出差分\t可能的输入\n");

        for (i = 0; i <= 15; i++) {
            {
                int t;                  //遍历输出异或
                t = i;                    //四位二进制数0000~1111存在数组n中
                n[3] = t % 2;
                t = t / 2;
                n[2] = t % 2;
                t = t / 2;
                n[1] = t % 2;
                t = t / 2;
                n[0] = t % 2;
            }
            //System.out.print("输出差分:");
            for (k = 0; k <= 3; k++)          //打印当前的输出异或
            {
                System.out.print(n[k]);
            }
            System.out.print("\t\t");


            count = 0;                   //将当前输出异或分布初始化为0次
            for (j = 0; j <= 63; j++)              //遍历输入六元组
            {

                {
                    int t;
                    t = j;
                    b[5] = t % 2;
                    t = t / 2;
                    b[4] = t % 2;
                    t = t / 2;
                    b[3] = t % 2;
                    t = t / 2;
                    b[2] = t % 2;
                    t = t / 2;
                    b[1] = t % 2;
                    t = t / 2;
                    b[0] = t % 2;
                }
                for (l = 0; l <= 5; l++)
                    d[l] = b[l] ^ In[l];         //将数组进行按位异或操作，求出当前输入异或下x对应的x*，并保存在数组d[]中
                s_box(b, e, s);               //b是输入x ，e是s盒在输入为b时的输出
                s_box(d, f, s);               //d是b在输入异或为in[l]对应下的输入x*，f是s盒在输入为b时的输出
                for (k = 0; k <= 3; k++)
                    Out[k] = e[k] ^ f[k];       //求出输出差分，存在Out数组中
                flag = 1;                     //并将标志位置1；
                for (k = 0; k <= 3; k++) {
                    if (Out[k] != n[k]) {
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {//符合当前输出差分的时候，当前输出差分的计数器加一
                    count = count + 1;
                    for (int z = 0; z < 6; z++) {
                        System.out.print(b[z]);
                    }
                    System.out.print(",");
                }

            }
            if (count == 0)
                System.out.print("没有可能输入");
            else
                System.out.print("\t输入数量为:" + count);

            System.out.print("\n");
        }
        System.out.print("=============================================================\n");
    }

    /**
     * 根据4个比特位取S盒中的值
     *
     * @param array
     * @param brray
     * @param crray
     */
    public static void s_box(int[] array, int[] brray, int[][] crray) {
        int p, q, t;
        p = 2 * array[0] + array[5];                               //用b1b6比特决定s盒的行取值为p
        q = 2 * 2 * 2 * array[1] + 2 * 2 * array[2] + 2 * array[3] + array[4];   //用b2b3b4b5决定s盒的列取值为q
        t = crray[p][q];                                       //t就是s盒在输入为array[6]时的输出
        brray[3] = t % 2;
        t = t / 2;
        brray[2] = t % 2;
        t = t / 2;
        brray[1] = t % 2;
        t = t / 2;
        brray[0] = t % 2;                                        //把t转换成二进制数存储在brray中
    }
}

class BitsArray {
    private String str;

    /**
     * 构造一个指定长度的位串，初始化位值为0
     *
     * @param length 位串的长度
     */
    BitsArray(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) sb.append("0");
        str = sb.toString();
    }

    /**
     * 从字符数组构造位串
     *
     * @param bs 用于构造位串的byte数组
     */
    BitsArray(byte[] bs) {
        fromByteArray(bs);
    }

    /**
     * 计算位串的长度
     *
     * @return 位串的长度
     */
    int length() {
        return str.length();
    }

    /**
     * 完成从byte数组到位串的转换
     *
     * @param bs 要转换为位串的byte数组
     */
    void fromByteArray(byte[] bs) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < bs.length; i++) {
            int length = Long.toString(bs[i] & 0xff, 2).length();
            String str = "";
            if (length < 8) {
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < 8 - length; j++) sb.append("0");
                str = sb.toString();
            }
            result.append(str + Long.toString(bs[i] & 0xff, 2));
        }
        str = result.toString();
    }

    /**
     * 将位串对象转换为byte数组
     *
     * @return 位串对象对应的byte数组
     */
    byte[] toByteArray() {
        String[] temp = new String[8];
        int pos = 0;
        for (int i = 0; i < 8; i++) {
            temp[i] = str.substring(pos, pos + 8);
            pos += 8;
        }

        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) b[i] = Long.valueOf(temp[i], 2).byteValue();

        return b;
    }

    /**
     * 与另一个位串进行异或操作
     *
     * @param other 用于与该位串进行异或操作的位串
     */
    void xor(BitsArray other) {
        String otherStr = other.toString();
        char[] cs = str.toCharArray();
        for (int i = 0; i < otherStr.length(); i++) {
            if (str.charAt(i) == otherStr.charAt(i)) cs[i] = '0';
            else cs[i] = '1';
        }
        str = Arrays.toString(cs).replaceAll("[\\[\\]\\s,]", "");
    }

    /**
     * 计算位串中1的个数
     *
     * @return 位串中1的个数
     */
    int OnesCount() {
        int count = 0;
        for (int i = 0; i < str.length(); i++) if (str.charAt(i) == '1') count++;

        return count;
    }

    /**
     * 克隆一个自身的拷贝
     */
    @Override
    protected BitsArray clone() {
        byte[] bs = toByteArray();
        BitsArray bitsArray = new BitsArray(bs);

        return bitsArray;
    }

    /**
     * 将指定索引位置的值设定为1
     *
     * @param index 指定的索引
     */
    void setOne(int index) {
        char[] cs = str.toCharArray();
        cs[index] = '1';
        str = Arrays.toString(cs).replaceAll("[\\[\\]\\s,]", "");
    }

    /**
     * 将指定索引位置的值设定为0
     *
     * @param index 指定的索引
     */
    void setZero(int index) {
        char[] cs = str.toCharArray();
        cs[index] = '0';
        str = Arrays.toString(cs).replaceAll("[\\[\\]\\s,]", "");
    }

    /**
     * 设置指定索引位置的值
     *
     * @param index 指定的索引
     * @param value 所需设定的值
     */
    void set(int index, int value) {
        char[] cs = str.toCharArray();
        cs[index] = (char) ('0' + value);
        str = Arrays.toString(cs).replaceAll("[\\[\\]\\s,]", "");
    }

    /**
     * 返回位串的字符串形式
     */
    @Override
    public String toString() {
        return str.toString();
    }
}

