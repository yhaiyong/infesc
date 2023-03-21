package com.yhy.gui;

import com.yhy.Playfair;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CountGui extends JFrame{

    private JPanel panel = new JPanel();
    private CountBarChart histogram=new CountBarChart();
    String text ;

    public CountGui(String str,String title) {
        text = Playfair.format(str);
        JFrame frame = new JFrame();
        frame.setBounds(650, 200, 800, 500);
        panel.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //统计部分
        int count[]=countLetters(text);
        double[] data=new double[26];
        for(int i=0;i<26;++i)
            data[i]=count[i];
        String[] letters=new String[26];
        for(int i=0;i<26;++i)
            letters[i]=""+(char)('A'+i);
        //绘图部分
        histogram.setData(letters, data);
        histogram.repaint();

        frame.add(histogram);
        frame.pack();
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setTitle(title);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    }

        public int[] countLetters(String str)
        {
            int[] count=new int[55];
            text = str;
            for(int i=0;i<text.length();++i)
            {
                char character=text.charAt(i);
                if((character>='A')&&(character<='Z'))
                    count[(int)character-65]++;
                else if((character>='a')&&(character<='z'))
                    count[(int)character-95]++;
            }
            return count;
        }

    }
