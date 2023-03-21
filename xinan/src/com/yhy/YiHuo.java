package com.yhy;

import com.yhy.tools.YiHuoUtils;

import java.io.File;
import java.util.Scanner;

/**
 * 异或算法
 *
 * @author: 杨海勇
 **/
public class YiHuo {
    public static void main(String[] args) throws Exception {
        String content = "";
        String inFilePath;
        String outFilePath;
        String key = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("========================异或算法=================");
        System.out.println(" 1.加密");
        System.out.println(" 2.解密");
        System.out.print("请选择：");
        int k = 0;
        k = scanner.nextInt();
        /*
        案例
        测试路径 G:\infesc\xinan\src\com\yhy\test\测试文本文档.txt
        保存路径 G:\infesc\xinan\src\com\yhy\test\测试保存文档.txt
        密钥：12345678
         */
        switch(k) {
            case 1:
                System.out.println("=======================加密====================");
                Scanner scanner1 = new Scanner(System.in);
                System.out.print("请输入密钥：");
                key = scanner1.nextLine();
                Scanner reader = new Scanner(System.in);
                System.out.print("请输入文件路径：");
                inFilePath = reader.nextLine();
                System.out.print("请输入加密文件保存路径：");
                outFilePath = scanner1.nextLine();
                YiHuoUtils.encryptFile(new File(inFilePath), new File(outFilePath), key.getBytes());
                System.out.println("加密完成！！！");
                break;
            case 2:
                System.out.println("=======================解密====================");
                Scanner scanner2 = new Scanner(System.in);
                System.out.print("请输入密钥：");
                key = scanner2.nextLine();
                Scanner reader1 = new Scanner(System.in);
                System.out.print("请输入解密文件路径：");
                inFilePath = reader1.nextLine();
                System.out.print("请输入解密文件保存路径：");
                outFilePath = scanner2.nextLine();
                YiHuoUtils.encryptFile(new File(inFilePath), new File(outFilePath), key.getBytes());
                System.out.println("解密完成！！！");
                break;
        }
//加密数据测试部分
//        // 加密数据, 返回密文
//        byte[] cipherBytes = YiHuoUtils.encrypt(content.getBytes(), key.getBytes());
//        // 解密数据, 返回明文
//        byte[] plainBytes = YiHuoUtils.encrypt(cipherBytes, key.getBytes());
//        System.out.println(new String(plainBytes));

    }


}
