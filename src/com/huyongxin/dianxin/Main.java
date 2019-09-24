package com.huyongxin.dianxin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static final int TESTLOCATIONSIZE = 1;    //用于测试的点的数量
    public static final int LOCATIONSIZE = 36;    //已经打点的个数
    public static final int APSIZE = 3;    //AP个数
    public static final int SIMILARITYSIZE = 6;    //相似点的个数
    public static double[][] points;    //测试点数组
    public static double[][] history;   /*用于保存定位结果*/
    public static final String TABLENAME = "dianxin";  //表名称
    public static Location l = null;
    public static final int port = 8888; //端口号
    public static String[] AP = {"48:8A:D2:0B:C5:54", "A8:57:4E:2D:D7:2C", "B0:89:00:E3:25:10"};


    public static void main(String args[]) {

        new DB();
        //对所有数组进行初始化
        initArrays();
        l = new Location();
        Socket wifiSocket;
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Waiting Connect!");
            while (true) {
                wifiSocket = server.accept();
                new Thread(new GetRssFromClient(wifiSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //double[][] points1 = new double[TESTLOCATIONSIZE][APSIZE];
//        double[][] p = {{-50, -50, -35}, {-53, -43, -52}, {-45, -37, -40}, {-47, -55, -39}, {-57, -40, -38}};  //55, 11, 35, 42, 17
//        //double[][] p = {{-45, -52, -47}, {-51, -43, -41}, {-46, -44, -22}, {-58, -48, -40}, {-46, -37, -34}};  //32, 26, 46, 14, 25
//        //double[][] p = {{-63, -45, -45}, {-39, -65, -52}, {-43, -55, -41}, {-63, -50, -44}, {-64, -20, -40}};  //18, 52, 54, 22, 16
//        double[][] h = new double[p.length][2];
//		for (int i = 0; i < p.length; i ++) {
//			l.startLocation(p[i]);
//			h[i][0] = l.FL[0][0] - 385;
//			h[i][1] = l.FL[0][1] - 193;
//		}
//
//		for (int i = 0; i < h.length; i ++) {
//			System.out.println(h[i][0] + "   " + h[i][1]);
//		}
        //Map map = new Map(h);




        //读取采集的数据进行定位，history保存定位结果(因为有5条定位结果)
		/*for(int i = 0; i < points1.length; i ++) {
			l.startLocation(points1[i]);
			history[i][0] = l.FL[0][0];
			history[i][1] = l.FL[0][1];
		}
        //将历史定位结果展示到地图上
        Map mp = new Map(history);
		for (int i = 0; i < history.length; i ++) {
			System.out.println(history[i][0] + "   " + history[i][1]);
		}
        A207Map map = new A207Map(history);*/
        //Ratio r = new Ratio();
		/*for(int i = 0; i < history.length; i ++) {
			for(int j = 0; j < history[i].length; j ++) {
				System.out.print(history[i][j] + "   ");
			}
			System.out.println();
		}*/
        //r.sortRSSI(points1);
    }


    private static void initArrays() {
        // TODO Auto-generated method stub
        points = new double[TESTLOCATIONSIZE][APSIZE];
        for(int i = 0; i < points.length; i ++) {
            for(int j = 0; j < points[i].length; j ++) {
                points[i][j] = 0.0;
            }
        }
        history = new double[TESTLOCATIONSIZE][2];
        for(int i = 0; i < history.length; i ++) {
            for(int j = 0; j < history[i].length; j ++) {
                history[i][j] = 0.0;
            }
        }
    }

}
