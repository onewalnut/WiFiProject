package com.huyongxin.dianxin;

/*
 * 通过直接计算欧氏距离来挑选相似点，不用高斯模型*/

import java.sql.ResultSet;
import java.sql.SQLException;

public class Similarity_Euclidean {

    private static ResultSet rs;
    public static int L[];
    private static double distance[];
    private static double tempDistance[];

    public Similarity_Euclidean() {
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        L = new int[Main.SIMILARITYSIZE];
        distance = new double[Main.LOCATIONSIZE];
        tempDistance = new double[Main.APSIZE];
        for(int i = 0; i < L.length; i ++) {
            L[i] = 0;
        }
        for(int i = 0; i < distance.length; i ++) {
            distance[i] = 0.0;
        }
        for(int i = 0; i < tempDistance.length; i ++) {
            tempDistance[i] = 0.0;
        }
    }

    public void caculateDistance(double X[]) {
        try {
            int i = 0;
            double temp = 0.0;
            rs = DB.stmt.executeQuery("select * from " + Main.TABLENAME);
            while(rs.next()) {
                double s = 0;
                for(int j = 0; j < Main.APSIZE; j ++) {
                    temp = X[j];
                    if(temp <= -70) {  //认为RSS值小于-75的可信度不高，舍去
                        temp = rs.getFloat(j + 4);
                    }
                    tempDistance[j] = rs.getFloat(j + 4) - temp;
                    s = tempDistance[j] * tempDistance[j] + s;
                }
                distance[i] = Math.sqrt(s);
                i ++;
            }
            sortDistance(distance);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void sortDistance(double[] S) {
        // TODO Auto-generated method stub
        double[] S1 = new double[Main.LOCATIONSIZE];
        for(int i = 0; i < Main.LOCATIONSIZE; i++) {
            S1[i] = S[i];
        }
        for(int i = 0; i < Main.LOCATIONSIZE - 1; i++) {
            for(int j = 0; j < Main.LOCATIONSIZE - 1 - i; j++) {
                if(S1[j] > S1[j + 1]) {
                    double tem = 0;
                    tem = S1[j + 1];
                    S1[j + 1] = S1[j];
                    S1[j] = tem;
                }
            }
        }
        pickDistance(S,S1);
    }

    private void pickDistance(double[] S, double[] S1) {
        // TODO Auto-generated method stub
        for(int i = 0; i < Main.SIMILARITYSIZE; i++) {
            for(int j = 0; j < Main.LOCATIONSIZE; j++) {
                if(S1[i] == S[j]) {
                    L[i] = j + 1;
                }
            }
        }
		/*for(int i = 0; i < L.length; i ++) {
			System.out.print(L[i] + "    ");
		}
		System.out.println();*/
    }

    public void printD() {
        for(int i = 0; i < distance.length; i ++) {
            System.out.println(distance[i]);
        }
    }
}

