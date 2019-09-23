package com.huyongxin.dianxin;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Map extends JFrame implements MouseWheelListener{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final int length = 1151;
    private static final int width = 648;
    private MyPanel mp = null;
    private Container ct;
    //private double[][] Ratio;

    public Map(double[][] location){
        ct = this.getContentPane();
        this.setLayout(null);
        mp = new MyPanel(15,location,(new ImageIcon("D:/WIFILocation/电信研究院/dxyjy.png")).getImage());
        mp.setBounds(0, 0, length, width);
        ct.add(mp);
        this.setSize(length, width);
        this.setLocation(100,100);
        this.setVisible(true);
        this.setResizable(false);
        this.addMouseWheelListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    class MyPanel extends JPanel {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        int r;
        double[][] location;
        Graphics gr;
        Image im;

        public MyPanel(int r, final double[][] location, Image im) {
            this.r = r;
            this.im = im;
            init(location);
            this.setOpaque(true);
        }

        public void init(double[][] location) {
            this.location = new double[Main.TESTLOCATIONSIZE][2];
            for(int i = 0; i < location.length; i ++) {
                this.location[i][0] = location[i][0];
                this.location[i][1] = location[i][1];
            }
			/*
			Ratio = new double[Main.TESTLOCATIONSIZE][2];
			for(int i = 0; i < Ratio.length; i ++) {
				Ratio[i][0] = (double)location[i][0] / length;
				Ratio[i][1] = (double)location[i][1] / width;
			}
			*/
        }

        public void paint(Graphics g){
            gr = getGraphics();
            paintComponent(g);
            drawPoint(g, location);
        }

        private void drawPoint(Graphics g, double[][] location2) {
            // TODO Auto-generated method stub
            g.setColor(Color.RED);
            for(int i = 0; i < location2.length; i ++) {
                g.fillOval((int)location2[i][0], (int)location2[i][1], r, r);
                g.drawString(Integer.toString(i + 1), (int)location2[i][0], (int)location2[i][1]);
            }
            g.setColor(Color.BLUE);
            int[][] lo = {{1036, 748}, {520, 288}, {1035, 521}, {616, 657}, {1200, 287}};  //55, 11, 35, 42, 17
            //int[][] lo = {{615, 523}, {1202, 399}, {1210, 654}, {832, 288}, {1038, 396}};  //32, 26, 46, 14, 25
            //int[][] lo = {{1320, 285}, {611, 754}, {906, 751}, {624, 393}, {1100, 288}};  //18, 52, 54, 22, 16
            for(int i = 0; i < lo.length; i ++) {
                g.fillOval(lo[i][0] - 385, lo[i][1] - 193, r, r);
                g.drawString(Integer.toString(i + 1), lo[i][0] - 385, lo[i][1] - 193);
            }
        }

        /*public void drawPoint(double[] location,int i) {

            gr.setColor(Color.RED);
            gr.fillOval((int)location[0], (int)location[1], r, r);
            //System.out.println("X:  " + (int)location[0] + "  Y:  " + (int)location[1]);
            gr.drawString(Integer.toString(i + 1), (int)location[0], (int)location[1]);
        }
        */
        public void paintComponent(Graphics g) {
            super.paintComponents(g);
            gr = getGraphics();
            g.drawImage(im, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // TODO Auto-generated method stub
		/*if(e.getWheelRotation() == -1) {

			if(length <= 1470 && width <= 690) {
				length += 10;
				width += 10;
				for(int i = 0; i < mp.location.length; i ++) {
					mp.location[i][0] = Ratio[i][0] * length;
					mp.location[i][1] = Ratio[i][1] * width;
				}
				mp.setBounds(0, 0, length, width);
			}
		}
		if(e.getWheelRotation() == 1) {
			if(length >= 1170 && width >= 390) {
				length -= 10;
				width -= 10;
				for(int i = 0; i < mp.location.length; i ++) {
					mp.location[i][0] = Ratio[i][0] * length;
					mp.location[i][1] = Ratio[i][1] * width;
				}
				mp.setBounds(0, 0, length, width);
			}
		}*/
    }

}
