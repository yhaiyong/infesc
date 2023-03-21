package com.yhy;

import java.util.*;

/**
 * DH 密钥协商协议
 * @author: 杨海勇
 **/
public class DH {
    static int p;//素数
    static int g;//生成元
    static int X;//A
    static int Y;//B
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        genarateKey();

        //A
        Human A = new Human(g, p);
        System.out.println();
        System.out.println("A选择的随机数x为：" + A.getN());
        System.out.println("X(g^x mod p)的值为:" + A.getKey());
        X = A.getKey();

        //B
        Human B = new Human(g, p);
        System.out.println();
        System.out.println("B选择的随机数y为：" + B.getN());
        System.out.println("Y(g^y mod p)的值为:" + B.getKey());
        Y = B.getKey();

        //计算K
        A.setPrivateKey(Y);
        System.out.println("==============================");
        System.out.println("A计算k的值：" + A.getPrivateKey());
        System.out.println();

        B.setPrivateKey(X);
        System.out.println("B计算k的值：" + B.getPrivateKey());


    }

    static class Human {
        int n;
        int privateKey;
        int key;

        Human(int g, int p) {
            n = getRandom(1, p - 1);
            key = expMod(g, n, p);
        }

        int getN() {
            return n;
        }

        void setPrivateKey(int key) {
            privateKey = expMod(key, n, p);
        }

        int getPrivateKey() {
            return privateKey;
        }

        int getKey() {
            return key;
        }
    }

    /**
     * 构造共同参数 p g
     */
    public static void genarateKey() {
        System.out.println("=================DH密钥协商协议==================");
        System.out.print("请输入素数p：");
        int temp = scan.nextInt();

        boolean isPrime = isPrime(temp);//判断是否是素数

        while (!isPrime) {
            System.out.print("该数字不是素数，请重新输入：");
            temp = scan.nextInt();
            isPrime = isPrime(temp);
        }
        p = temp;
        System.out.print("素数" + p + "的生成元有：");
        int[] a = searchPrimeRoot(p);
        for (int i = 0; i < a.length; i++) {
            if(a[i] != 0){
                System.out.print(" " + a[i] + " ");
            }

        }
        System.out.println();

        System.out.print("请输入生成元g：");
        int temp1 = scan.nextInt();

        boolean isPrimeRoot = isPrimeRoot(temp1, p);

        while (isPrimeRoot) {
            System.out.print("该数字不是p的生成元，请重新输入：");
            temp1 = scan.nextInt();
            isPrimeRoot = isPrimeRoot(temp1, p);

        }
        g = temp1;
    }


    /**
     * 判断n是素数
     *
     * @param n
     * @return
     */
    public static boolean isPrime(int n) {
        for (int i = 2; i <= n - 1; ++i) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * 计算 a b最大因子
     *
     * @param a
     * @param b
     * @return ans 最大公因子
     */
    public int GCD(int a, int b) {
        int ans = euclidean_algorithm(a, b);
        return ans;
    }


    /**
     * 辗转相除法构造一个递归函数
     *
     * @param a
     * @param b
     * @return
     */
    public static int euclidean_algorithm(int a, int b) {
        if (b == 0) return a;
        return euclidean_algorithm(b, a % b);
    }


    /**
     * 计算 b^n mod m
     *
     * @param b 底数
     * @param n 幂
     * @param m 模数
     * @return ans 取幂运算结果
     */
    public static int expMod(int b, int n, int m) {
        int ans;

        if (n == 0) return 1 % m;
        if (n == 1) return b % m;

        ans = expMod(b, n / 2, m);
        ans = ans * ans % m;

        //如果n是奇数，需多乘一次b
        if ((n & 1) == 1) ans = ans * b % m;

        return ans;
    }


    static int[] searchPrimeRoot(int p) {
        int[] ans = new int[p];
        int j = 0;
        for (int i = 0; i < p; i++) {
            if (!isPrimeRoot(i, p)) {
                ans[j] = i;
                ++j;
            }
        }

        return ans;
    }

    /**
     * 判断g是否是p的生成元
     *
     * @param g
     * @param p
     * @return
     */
    public static boolean isPrimeRoot(int g, int p) {
        int[] ans = new int[p - 1];
        for (int i = 1; i <= p - 1; i++) {
            ans[i - 1] = expMod(g, i, p);
        }
        return isRepeat(ans);
    }

    /**
     * 判断数组是否有重复值
     *
     * @param m
     * @return
     */
    static boolean isRepeat(int[] m) {
        boolean flag = false;
        List<Integer> list = new ArrayList<Integer>(); //list可以添加重复的元素
        for (int x : m) {
            list.add(x);
        }

        Set<Integer> set = new HashSet<>(list);//把list转为set,为了去重
        for (int s : set) {
            int temp = s;
            int num = 0;
            for (int l : list) {    //对于每一个set集合，list集合里每出现一次，计数一次
                if (l == temp) {
                    num++;
                }
            }
            if (num >= 2) {          //num>=2 说明已经有重复的元素了，后面的没必要做了，直接退出
                flag = true;
                break;
            }
        }
        return flag;
    }


    /**
     * 获取指定范围 [1,p-1] 随机数
     *
     * @param min
     * @param max
     * @return 指定范围随机数
     */
    public static int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        //int s = random.nextInt(max-1)+1;
        return s;
    }

}
