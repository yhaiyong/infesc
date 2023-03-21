package com.yhy.gui;

import com.yhy.Caesar;
import com.yhy.Hill;
import com.yhy.Playfair;
import com.yhy.tools.MyFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;


/**
 * 算法菜单界面
 * @author: 杨海勇
 **/
public class Menu extends JFrame {
    private JTextArea passwordField;//密钥
    private JTextArea textField;//输入内容
    private JTextArea keyMatrixArea;//密钥矩阵
    private JTextArea matrixArea;//矩阵阶数
    private JTextArea resultField;//输出内容
    private JTextArea formatField;//格式化明文
    private JLabel label5;
    private JLabel label4;
    private JLabel label3;
    static String plain;//明文
    static String cipher;//密文
    private JComboBox cmb = new JComboBox<>();//下拉选择菜单
    private JPanel panel = new JPanel();//组件
    private JButton encode;//加密按钮
    private JButton decode;//解密按钮
    int m = 3;//密钥矩阵默认值
    String changed = "Caesar"; //默认值为 Caesar算法

    public static void main(String[] args) {
        new Menu();
    }


    public Menu() {
        JFrame frame = new JFrame("信息安全概论古典算法");
        Toolkit toolkit=Toolkit.getDefaultToolkit();
        URL xmlpath = this.getClass().getClassLoader().getResource("title.png");
        Image icon = toolkit.getImage(xmlpath);
        frame.setIconImage(icon);
        frame.setBounds(650, 200, 800, 500);
        panel.setLayout(null);

        cmb.addItem("Caesar");
        cmb.addItem("Playfair");
        cmb.addItem("Hill");
        cmb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                changed = e.getItem().toString();
                if (changed == "Hill") {
                    decode.setVisible(false);
                    label4.setVisible(true);
                    matrixArea.setVisible(true);
                    formatField.setVisible(false);
                    label5.setVisible(false);
                    label3.setVisible(false);
                    keyMatrixArea.setVisible(false);

                }else if(changed == "Playfair") {
                    decode.setVisible(true);
                    formatField.setVisible(true);
                    label5.setVisible(true);
                    matrixArea.setVisible(true);
                    label4.setVisible(true);
                    label3.setVisible(true);
                    keyMatrixArea.setVisible(true);
                }else {
                    decode.setVisible(true);
                    formatField.setVisible(false);
                    label5.setVisible(false);
                    label4.setVisible(false);
                    matrixArea.setVisible(false);
                    label3.setVisible(true);
                    keyMatrixArea.setVisible(true);


                }

            }
        });
        cmb.setBounds(35, 80, 120, 40);
        cmb.setFont(MyFont.setFont());
        panel.add(cmb);

        //标签
        JLabel label = new JLabel("输入内容：");
        label.setBounds(180, 100, 100, 15);
        label.setFont(MyFont.setFont());
        panel.add(label);

        JLabel label1 = new JLabel("密钥：");
        label1.setBounds(180, 200, 100, 15);
        label1.setFont(MyFont.setFont());
        panel.add(label1);

        JLabel label2 = new JLabel("输出内容：");
        label2.setBounds(180, 350, 100, 15);
        label2.setFont(MyFont.setFont());
        panel.add(label2);

        label3 = new JLabel("密钥矩阵");
        label3.setBounds(640, 50, 100, 15);
        label3.setFont(MyFont.setFont());
        panel.add(label3);

        label4 = new JLabel("密钥矩阵阶数：");
        label4.setBounds(150, 280, 150, 15);
        label4.setFont(MyFont.setFont());
        label4.setVisible(false);
        panel.add(label4);

        label5 = new JLabel("格式化明文：");
        label5.setBounds(350, 280, 150, 15);
        label5.setFont(MyFont.setFont());
        label5.setVisible(false);
        panel.add(label5);

        //文本内容输入框
        textField = new JTextArea();
        textField.setBounds(280, 50, 300, 100);
        textField.setLineWrap(true);
        textField.setFont(MyFont.setFont());
        panel.add(textField);

        //密钥输入框
        passwordField = new JTextArea();
        passwordField.setBounds(280, 160, 300, 100);
        passwordField.setFont(MyFont.setFont());
        panel.add(passwordField);

        //文本内容输出框
        resultField = new JTextArea();
        resultField.setBounds(280, 320, 300, 100);
        resultField.setFont(MyFont.setFont());
        panel.add(resultField);
        //密钥矩阵
        keyMatrixArea = new JTextArea();
        keyMatrixArea.setBounds(600, 80, 150, 150);
        keyMatrixArea.setFont(MyFont.setFont());
        keyMatrixArea.setLineWrap(true);
        panel.add(keyMatrixArea);
        //矩阵阶数
        matrixArea = new JTextArea();
        matrixArea.setBounds(280, 275, 50, 28);
        matrixArea.setFont(MyFont.setFont());
        matrixArea.setVisible(false);
        panel.add(matrixArea);
        //格式化明文
        formatField = new JTextArea();
        formatField.setBounds(460, 275, 200, 28);
        formatField.setFont(MyFont.setFont());
        formatField.setVisible(false);
        panel.add(formatField);

        //加密按钮
        encode = new JButton("加密");
        encode.setFont(MyFont.setFont());
        encode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (changed) {
                    case "Caesar":
                        plain = textField.getText();
                        int k = Integer.parseInt(passwordField.getText());
                        if (k < 1 || k > 25) {
                            JOptionPane.showMessageDialog(null, "输入错误，密钥k的取值范围是1~25");
                            return;
                        }

                        resultField.setText(Caesar.encrypt(plain, k));
                        //字符频率
                        new CountGui(plain, "明文字符频率统计");
                        new CountGui(Caesar.encrypt(plain, k), "密文字符频率统计");
                        break;

                    case "Playfair":
                        plain = textField.getText();
                        String key = passwordField.getText();
                        String result = Playfair.encode(plain, key);
                        // 用StringBuffer类来将字符数组转为字符串
                        char[][] matrix = Playfair.getKeyMatrix(key);
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 5; j++) {
                                sb.append(matrix[i][j]);
                                sb.append(" ");
                            }
                            sb.append("\n");
                        }
                        formatField.setText(Playfair.decode(result, key));
                        keyMatrixArea.setText(sb.toString());
                        resultField.setText(result);

                        //字符频率
                        new CountGui(plain, "明文字符频率统计");
                        new CountGui(result, "密文字符频率统计");
                        break;

                    case "Hill":
                        m = Integer.parseInt(matrixArea.getText());
                        key = passwordField.getText();
                        plain = textField.getText();

                        //String[] 转 int[]
                        String[] strings = key.split(" ");
                        int[] one = new int[strings.length];
                        for (int i = 0; i < strings.length; i++) {
                            one[i] = Integer.parseInt(strings[i]);
                        }
                        int[][] keyMatrix = Hill.oneToTwo(one, m);
                        cipher = Hill.encrypt(plain, keyMatrix);
                        String str = key.replaceAll(" ", ",");
                        String[] strings1 = str.split(",");
                        char[] c = str.toCharArray();
                        char[][] charKey = Hill.oneToTwoChar(c, m);
                        StringBuffer s = new StringBuffer();
                        for (int i = 0; i < m; i++) {
                            for (int j = 0; j < m; j++) {
                                s.append(charKey[i][j]);
                                s.append(" ");
                            }
                            s.append("\n");
                        }
//                        keyMatrixArea.setText(s.toString());
                        resultField.setText(cipher);
                        new CountGui(plain, "明文字符频率统计");
                        new CountGui(cipher, "密文字符频率统计");
                        break;
                }

            }
        });
        encode.setBounds(50, 200, 80, 50);
        panel.add(encode);

        //解密按钮
        decode = new JButton("解密");
        decode.setFont(MyFont.setFont());
        decode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (changed) {
                    case "Caesar":
                        cipher = textField.getText();
                        int k = Integer.parseInt(passwordField.getText());
                        if (k < 1 || k > 25) {
                            JOptionPane.showMessageDialog(null, "输入错误，密钥k的取值范围是1~25");
                        }
                        resultField.setText(Caesar.decrypt(cipher, k));

                        //字符频率
//                        new CountGui(cipher, "密文字符频率统计");
//                        new CountGui(Caesar.decrypt(cipher, k), "明文字符频率统计");
                        break;

                    case "Playfair":
                        cipher = textField.getText();
                        String key = passwordField.getText();
                        char[][] matrix = Playfair.getKeyMatrix(key);
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 5; j++) {
                                sb.append(matrix[i][j]);
                                sb.append(" ");
                            }
                            sb.append("\n");
                        }
                        keyMatrixArea.setText(sb.toString());
                        resultField.setText(Playfair.decode(cipher, key));

                        //字符频率
//                        new CountGui(cipher, "密文字符频率统计");
//                        new CountGui(Playfair.decode(cipher, key), "明文字符频率统计");
                        break;

                    case "Hill":
                        //未实现
                }
            }
        });
        decode.setBounds(50, 300, 80, 50);
        panel.add(decode);

        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}





