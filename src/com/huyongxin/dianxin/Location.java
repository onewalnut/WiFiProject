package com.huyongxin.dianxin;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Location {

    LocationPower lp;  //定位加权
    GetGauss gg;  //计算高斯概率密度
    Similarity_Euclidean se;  //计算欧式距离
    Logarithmic lo;  //利用对数模型来进行计算相似度
    private static ResultSet rs;
    private static double[] S;
    private static double[][] W;  //加权最邻近算法中的W权值的计算数组
    private static double[][] w;  //加权最邻近算法中的w权值的计算数组，W中要用到
    public static int[] L;
    public static double[][] FL;

    public Location() {
        lp = new LocationPower();
        gg = new GetGauss();
        se = new Similarity_Euclidean();
        lo = new Logarithmic();
        initArrays();
    }

    public void startLocation(double[] points) {
        // TODO Auto-generated method stub
        //gg.caculateGauss(points);  //计算待定位点与数据库中的点的高斯概率密度
        //lo.caculate(points);  //计算对数模型
        //caculateSimilarity(points);  //计算相似度
        se.caculateDistance(points);    //直接利用欧式距离来挑选出最近的点
        lp.caculateLocation(points);
    }

    private void initArrays() {
        // TODO Auto-generated method stub
        S = new double[Main.LOCATIONSIZE];
        W = new double[Main.LOCATIONSIZE][Main.APSIZE];
        w = new double[Main.LOCATIONSIZE][Main.APSIZE];
        L = new int[Main.SIMILARITYSIZE];
        FL = new double[1][2];
        for(int i = 0; i < S.length; i++) {
            S[i] = 0.0;
        }
        for(int i = 0; i < W.length; i++) {
            for(int j = 0; j < W[i].length; j++) {
                W[i][j] = 0.0;
            }
        }
        for(int i = 0; i < w.length; i++) {
            for(int j = 0; j < w[i].length; j++) {
                w[i][j] = 0.0;
            }
        }
        for(int i = 0; i < L.length; i++) {
            L[i] = 0;
        }
    }


    public void caculateSimilarity(double X[]) {  //X为待测试点
        try {
            caculateW(X);  //先计算W
            int i = 0;
            rs = DB.stmt.executeQuery("select * from " + Main.TABLENAME);
            while(rs.next()) {
                for(int j = 0; j < Main.APSIZE; j++) {
                    S[i] = S[i] + GetGauss.F[i][j] * W[i][j];    //利用高斯建模计算相似度，相似度的计算就是用F*W，W为权重
                    //S[i] = S[i] + Logarithmic.F[i][j] * W[i][j];    //利用对数模型来计算相似点
                }
                i ++;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        sortSimilarity(S);
    }

    private void caculateW(double[] X) {  //计算W，W的计算是用w/Σw
        // TODO Auto-generated method stub
        try {
            caculatew(X);  //计算w
            int i = 0;
            rs = DB.stmt.executeQuery("select * from " + Main.TABLENAME);
            while(rs.next()) {
                double s = 0.0;
                for(int j = 0; j < Main.APSIZE; j++) {
                    s = s + w[i][j];
                }
                for(int j = 0; j < Main.APSIZE; j++) {
                    W[i][j] = w[i][j]/s;
                }
                i++;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void caculatew(double[] X) {  //计算w
        // TODO Auto-generated method stub
        try {
            int i = 0;
            rs = DB.stmt.executeQuery("select * from " + Main.TABLENAME);
            while(rs.next()) {
                double x = 0.0;
                for(int j = 0; j < Main.APSIZE; j ++) {
                    x = x + Math.abs(X[j] - rs.getFloat(j + 4));
                }
                for(int j = 0; j < Main.APSIZE; j ++) {
                    w[i][j] = x / (Math.pow(Math.E, Math.abs(X[j] - rs.getFloat(j + 4))));
                }
                i++;
            }
            for(int m = 0; m < Main.LOCATIONSIZE; m ++) {
                for(int n = 0; n < Main.APSIZE; n ++) {
                    w[m][n] = Math.abs(w[m][n]);
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void sortSimilarity(double[] S) {  //对计算出来的所有点的相似度进行排序
        // TODO Auto-generated method stub
        double[] S1 = new double[Main.LOCATIONSIZE];
        for(int i = 0; i < S1.length; i++) {
            S1[i] = S[i];
        }
        for(int i = 0; i < Main.LOCATIONSIZE - 1; i++) {
            for(int j = 0; j < Main.LOCATIONSIZE - 1 - i; j++) {
                if(S1[j] < S1[j + 1]) {
                    double tem = 0;
                    tem = S1[j + 1];
                    S1[j + 1] = S1[j];
                    S1[j] = tem;
                }
            }
        }
        pickSimilarity(S,S1);
        //System.out.println("");
    }

    private void pickSimilarity(double[] S, double[] S1) {  //挑选相似度最高的点
        // TODO Auto-generated method stub
        for(int i = 0; i < Main.SIMILARITYSIZE; i++) {
            for(int j = 0; j < Main.LOCATIONSIZE; j++) {
                if(S1[i] == S[j]) {
                    L[i] = j + 1;
                    //System.out.print(L[i] + "     ");
                }
            }
        }
    }


}
