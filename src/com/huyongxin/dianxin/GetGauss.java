package com.huyongxin.dianxin;

import java.sql.ResultSet;
import java.sql.SQLException;


public class GetGauss {

    private static ResultSet rs;
    public static double[][] F;  //F数组用来保存测试点与数据库中所有点的高斯概率

    public GetGauss() {
        initArrays();
    }

    private void initArrays() {
        // TODO Auto-generated method stub
        F = new double[Main.LOCATIONSIZE][Main.APSIZE];
        for(int i = 0; i < F.length; i++) {
            for(int j = 0; j < F[i].length; j++) {
                F[i][j] = (double) 0.0;
            }
        }
    }

    public void caculateGauss(double[] X) {  //X为测试点数组
        // TODO Auto-generated method stub
        try {
            int i = 0;
            rs = DB.stmt.executeQuery("select * from " + Main.TABLENAME);
            while(rs.next()) {
                for(int j = 0; j < F[i].length;j ++) {
                    if(X[j] > -70) {
                        //F[i][j] = gaussExperience(X[j],rs.getFloat(j + 4),rs.getFloat(j + 12));//gzdemo
                        F[i][j] = gaussExperience(X[j],rs.getFloat(j + 4),rs.getFloat(j + 6));
                    }
                }
                i++;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private double gaussExperience(double x, float u, float d) {  //x为待测试点的RSS值，u为数据库中点的均值，d为方差
        // TODO Auto-generated method stub
        double m = -(((x - u) * (x - u))/(2 * d *d));
        double a = Math.pow(Math.E, m);
        return Math.abs((a * (1/(Math.sqrt(2 * Math.PI) * d))));
    }

}
