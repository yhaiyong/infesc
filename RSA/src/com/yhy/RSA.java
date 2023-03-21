package com.yhy;


import java.util.Scanner;

import static java.lang.System.in;
import static sun.java2d.cmm.ColorTransform.In;

/**
 * RSA算法
 * @author: 杨海勇
 **/
public class RSA
{
    int p = 0, q = 0, e = 0, d = 0, n = 0;
    static int x = -1, y = -1;
    private Scanner scan = new Scanner(in);

    public static void main(String[] args)
    {
        RSA rsa = new RSA();
        System.out.println("======================RSA算法====================");
        rsa.generateKey();

        System.out.println();
        System.out.println("公钥 KU = {" + rsa.e + ", " + rsa.n + "}");
        System.out.println("私钥 KR = {" + rsa.d + ", " + rsa.n + "}");
        System.out.println();

        int ciphertext = rsa.encrypt();
        System.out.println("加密后的密文 " + ciphertext);
        System.out.println();

        int plaintext = rsa.decrypt();
        System.out.println("解密后的明文 " + plaintext);
    }

    /**
     * 生成密钥
     */
    void generateKey()
    {
        //选择两个素数p和q
        primeNumberJudgment();

        //公钥和私钥的公共部分
        this.n = this.p * this.q;

        //欧拉函数
        int euler = (this.p - 1) * (this.q - 1);

        //选择满足与欧拉函数结果互质的整数e，作为公钥的一部分
        this.e = selectE(euler);

        //计算d，作为私钥的一部分
        this.d = expGcd(e, euler);
        this.d = this.d < 0 ? this.d + euler : this.d;
    }

    /**
     * 扩展欧几里得算法
     * @param a x的参数
     * @param b y的参数
     * @return 求得的x的值
     */
    private int expGcd(int a,int b)
    {
        if(b == 0)
        {
            x = 1;
            y = 0;
            return x;
        }
        else
        {
            expGcd(b, a % b);
            int t = x;
            x = y;
            y = t - a / b * y;
            return x;
        }
    }

    /**
     * 选择整数e
     * @param euler 欧拉函数的值
     * @return 对输入进行筛选得到的选择的整数e
     */
    private int selectE(int euler)
    {
        System.out.print("请输入数字e: ");
        int temp = scan.nextInt();

        if(gcd(euler, temp) == 1) return temp;
        else
        {
            System.out.println("输入数字不合法，请重新输入!");
            return selectE(euler);
        }
    }

    /**
     * 欧几里得算法
     * @param m 正整数m
     * @param n 正整数n
     * @return m和n的最大公约数
     */
    private int gcd(int m, int n)
    {
        if(n == 0) return m;
        int r = m % n;
        return gcd(n,r);
    }

    /**
     * 对输入的数字进行素数判断并对素数进行保存
     */
    private void primeNumberJudgment()
    {
        System.out.print("请选择两个素数p和q：");
        int temp = scan.nextInt();

        int root = (int) Math.sqrt(temp);
        boolean isPrime = true;

        for(int i = 2; i <= root; i++)
        {
            if(temp % i == 0)
            {
                isPrime = false;
                break;
            }
        }

        if(isPrime == false)
        {
            System.out.println("该数字不是素数，请重新输入!");
            primeNumberJudgment();
        }
        else
        {
            if(this.p == 0)
            {
                this.p = temp;
                primeNumberJudgment();
            }
            else
            {
                if(this.q == 0) this.q = temp;
            }
        }
    }

    /**
     * 加密算法
     * @return 加密得到的密文
     */
    int encrypt()
    {
        System.out.println("=====================加密过程================");
        System.out.print("请输入明文: ");
        int m = scan.nextInt();

        if(m >= this.n)
        {
            System.out.println("输入不合法，请重新输入明文!");
            encrypt();
        }

        return (int) expMod(m, e, n);
    }

    /**
     * 解密算法
     * @return 解密得到的明文
     */
    int decrypt()
    {
        System.out.println("===============解密密文===============");
        System.out.print("请输入密文: ");
        int c = scan.nextInt();

        return (int) expMod(c, d, n);
    }

    /**
     * 快速幂取模a^n mod b
     * @param a 底数
     * @param n 幂
     * @param b 模数
     * @return 快速幂取模运算结果
     */
    private long expMod(long a, long n, long b)
    {
        long t;

        if(n == 0) return 1 % b;
        if(n == 1) return a % b;

        t = expMod(a, n / 2, b);
        t = t * t % b;

        //如果n是奇数，需多乘一次a
        if((n&1) == 1) t = t * a % b;

        return t;
    }
}
