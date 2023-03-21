package com.yhy.gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class CountBarChart extends JPanel{

    private double[] dataValue={200,140,100,60,40};
    private String[] dataName = {"CS", "Math", "Chem", "Biol", "Phys"};
    private Color[] colors = {Color.red, Color.yellow, Color.green,
        Color.blue, Color.cyan, Color.magenta, Color.orange, Color.pink,
        Color.darkGray};
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(dataValue==null)return;
        double max=dataValue[0];
        for(int i=1;i<dataValue.length;++i)
            max=Math.max(max, dataValue[i]);
        int barWidth = (int)((getWidth() - 20.0)/dataValue.length - 10);
        int maxBarHeight = getHeight() - 30;
        g.drawLine(5, getHeight()-10, getWidth()-5, getHeight()-10);
        int x=15;
        for(int i=0;i<dataValue.length;++i)
        {
            g.setColor(colors[i%colors.length]);
            int newHeight=(int)(maxBarHeight*dataValue[i]/max);
            int y=getHeight()-10-newHeight;
            g.fillRect(x, y, barWidth, newHeight);
            g.setColor(Color.black);
            if((dataName!=null)&&(i<dataName.length))
                g.drawString(dataName[i], x, y-7);
            x+=barWidth+10;
        }
    }
    public void setData(String[] dataName,double[] dataValue)
    {
        this.dataName=dataName;
        this.dataValue=dataValue;
        repaint();
    }

    public CountBarChart() {

    }

}