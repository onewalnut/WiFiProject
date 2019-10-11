package com.huyongxin.dianxin;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationPower {

    private static ResultSet rs;
    private static int[][] Location;  //用于保存相似点的坐标
    private static double[] T = new double[Main.SIMILARITYSIZE];  //T为临时数组，没什么意义
    private static double[] W = new double[Main.SIMILARITYSIZE];  //W为位置加权过程中的权重数组
    public static double[][] finalLocation = new double[1][2];  //保存最后点的位置

    public LocationPower() {
        initArrays();
    }

    private void initArrays() {
        // TODO Auto-generated method stub
        Location = new int[Main.SIMILARITYSIZE][2];
        for(int i = 0; i < Location.length; i ++) {
            for(int j = 0; j < Location[i].length; j ++) {
                Location[i][j] = 0;
            }
        }
    }

    public void caculateLocation(double[] X) {
        caculateW(X);  //首先计算W
        for(int i = 0; i < Main.SIMILARITYSIZE; i++) {
            try {
                //rs = DB.stmt.executeQuery("select * from " + Main.TABLENAME + " where id = " + com.Huyongxin.Demo.Location.L[i]);  //用高斯建模获得的最近的点
                rs = DB.stmt.executeQuery("select * from " + Main.TABLENAME + " where id = " + Similarity_Euclidean.L[i]);  //用欧式距离获得的最近的点
                while(rs.next()) {  //相似点的xy坐标
                    Location[i][0] = rs.getInt(2);
                    Location[i][1] = rs.getInt(3);
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        double x = 0,y = 0;
        for(int i = 0; i < Main.SIMILARITYSIZE; i++) {
            x = Location[i][0] * W[i] + x;
            //System.out.print(W[i] + "   ");
            y = Location[i][1] * W[i] + y;
            //System.out.println(W[i]);
        }
        com.huyongxin.dianxin.Location.FL[0][0] = x;
        com.huyongxin.dianxin.Location.FL[0][1] = y;
    }

    private void caculateW(double[] X) {
        // TODO Auto-generated method stub
        caculatew(X);  //计算W时先计算w
        double S = 0.0;
        for(int i = 0; i < Main.SIMILARITYSIZE; i++) {
            S = S + T[i];
        }
        for(int i = 0; i < Main.SIMILARITYSIZE; i++) {
            W[i] = T[i] / S;
        }
    }

    private void caculatew(double[] X) {
        // TODO Auto-generated method stub
		/*double[] M = new double[Main.SIMILARITYSIZE];
		for(int i = 0; i < Main.SIMILARITYSIZE; i++) {
			try {
				double[] T1 = new double[Main.APSIZE];
				//rs = DB.stmt.executeQuery("select * from locationdemo where id = " + com.Huyongxin.Demo.Location.L[i]);  //用高斯*加权获得的最相近的点
				rs = DB.stmt.executeQuery("select * from locationdemo where id = " + com.Huyongxin.Demo.Similarity_Euclidean.L[i]);  //用欧式距离获得的最近的点
				System.out.println(com.Huyongxin.Demo.Similarity_Euclidean.L[i] + "----------");
				//T1数组用来保存待定位点和近似点  每一个AP的RSSI差值
				while(rs.next()) {
					for(int j = 0; j < Main.APSIZE; j ++) {
						T1[j] = (rs.getFloat(j + 4) - X[j]);
					}
				}
				double s = 0;
				for(int m = 0; m < Main.APSIZE; m++) {
					s = s + T1[m] * T1[m];
				}
			//M数组用于保存待定位点和近似点的模         即公式中  Xi-X`的值  欧式距离
			M[i] = Math.sqrt(s);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		double S = 0;    //S为Xi-X`的和
		for(int i = 0; i < Main.SIMILARITYSIZE; i++) {
			S = M[i] + S;
		}
		//T为wi`的权值
		for(int i = 0; i < Main.SIMILARITYSIZE; i++) {
			T[i] = S / M[i];
		}*/

        /*
         * w的计算：计算所有相似点与待测试点的欧式距离的和 / 每个相似点与待
         * 测试点的欧式距离*/
        double W = 0.0;
        for(int i = 0; i < Main.SIMILARITYSIZE; i ++) {
            try {
                double s = 0.0;
                //rs = DB.stmt.executeQuery("select * from " + Main.TABLENAME + " where id = " + com.Huyongxin.Demo.Location.L[i]);  //用高斯建模获得的最近的点
                rs = DB.stmt.executeQuery("select * from " + Main.TABLENAME + " where id = " + Similarity_Euclidean.L[i]);    //用欧式距离计算出最近的点
                while(rs.next()) {
                    for(int j = 0; j < Main.APSIZE; j ++) {
                        if(X[j] > -70) {
                            s = s + (rs.getFloat(j + 4) - X[j]) * (rs.getFloat(j + 4) - X[j]);
                        }
                    }
                    T[i] = Math.abs(s);
                    W = W + T[i];
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        for(int i = 0; i < T.length; i ++) {
            T[i] = W / T[i];
        }
    }
}
