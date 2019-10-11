package com.huyongxin.dianxin.kalmanfilter;

/**
 * @author HuYongxin
 * @description: 利用卡尔曼滤波进行优化
 */
public class KalmanFilter {
    public KF_Params initKalman(double x, double y, double vx, double vy) {
        return new KF_Params(x, y, vx, vy);
    }

    /*
     * @description:对参数进行更新，求出真实的值
     * @param [kf_params]
     * @return com.HuYongxin.TileJunction.KF_Params
     * @author HuYongxin
     **/
    public KF_Params updateParams(KF_Params kf_params) {
        double[][] x_;
        x_ = calcux_(kf_params.A, kf_params.x, kf_params.B, kf_params.u);  //(4, 1)
        double[][] p_;
        p_ = calcp_(kf_params.A, kf_params.P, kf_params.Q);  //(4, 4)
        kf_params.K = updateK(p_, kf_params.H, kf_params.R);
        kf_params.x = updateX(x_, kf_params.K, kf_params.z, kf_params.H);  //(4, 1)
        kf_params.P = updateP(p_, kf_params.K, kf_params.H);  //(4, 4)
        return kf_params;
    }

    private double[][] updateP(double[][] p_, double[][] k, double[][] h) {
        double[][] tmp1 = multMatrix(k, h);  //(4, 4)
        double[][] tmp2 = multMatrix(tmp1, p_);  //(4, 4)
        for (int i = 0; i < tmp2.length; i ++) {
            for (int j = 0; j < tmp2[0].length; j ++) {
                tmp2[i][j] = p_[i][j] - tmp2[i][j];
            }
        }
        return tmp2;
    }

    private double[][] calcp_(double[][] a, double[][] p, double[][] q) {
        double[][] tmp1 = multMatrix(a, p);  //(4, 4)
        double[][] reverseA = reverseArray(a);
        double[][] tmp2 = multMatrix(tmp1, reverseA);  //(4, 4)
        for (int i = 0; i < tmp2.length; i ++) {
            for (int j = 0; j < tmp2[i].length; j ++) {
                tmp2[i][j] += q[i][j];
            }
        }
        return tmp2;
    }

    private double[][] calcux_(double[][] a, double[][] x, double b, double u) {
        double[][] x_ = multMatrix(a, x);  //(4, 1)
        for (int i = 0; i < x_.length; i ++) {
            for (int j = 0; j < x_[0].length; j ++) {
                x_[i][j] += b * u;
            }
        }
        return x_;
    }

    private double[][] multMatrix(double[][] a, double[][] b) {
        if (a[0].length != b.length) {
            System.out.println("矩阵维度不相等!");
            return null;
        }
        double[][] c = new double[a.length][b[0].length];
        for (int i = 0; i < c.length; i ++) {
            for (int j = 0; j < c[i].length; j ++) {
                c[i][j] = 0;
            }
        }
        for (int i = 0; i < a.length; i ++) {
            for (int j = 0; j < b[0].length; j ++) {
                for (int k = 0; k < a[0].length; k ++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }

    private double[][] updateX(double[][] x_, double[][] k, double[][] z, double[][] h) {
        double[][] tmp1 = multMatrix(h, x_);  //(2, 1)
        for (int i = 0; i < tmp1.length; i ++) {
            for (int j = 0; j < tmp1[0].length; j ++) {
                tmp1[i][j] = z[i][j] - tmp1[i][j];
            }
        }
        double[][] tmp2 = multMatrix(k, tmp1);  //(4, 1)
        for (int i = 0; i < x_.length; i ++) {
            for (int j = 0; j < x_[i].length; j ++) {
                tmp2[i][j] = x_[i][j] + tmp2[i][j];
            }
        }
        return tmp2;
    }

    private double[][] updateK(double[][] p_, double[][] h, double[][] r) {
        //求H矩阵的转置
        double[][] h1 = reverseArray(h);
        double[][] tmp1 = multMatrix(p_, h1);  //(4, 2)
        double[][] tmp2 = multMatrix(h, p_);  //(2, 4)
        double[][] tmp3 = multMatrix(tmp2, h1);  //(2, 2)
        for (int i = 0; i < tmp3.length; i ++) {
            for (int j = 0; j < tmp3[0].length; j ++) {
                tmp3[i][j] += r[i][j];
            }
        }
        //求tmp3的逆，由于tmp3是二阶矩阵，直接利用公式
        double l = tmp3[0][0] * tmp3[1][1] - tmp3[0][1] * tmp3[1][0];
        if (l == 0) {
            System.out.println("无法求出逆矩阵!");
            return null;
        }
        double t = tmp3[0][0];
        tmp3[0][0] = tmp3[1][1];
        tmp3[1][1] = t;
        tmp3[0][1] = -tmp3[0][1];
        tmp3[1][0] = -tmp3[1][0];
        for (int i = 0; i < tmp3.length; i ++) {
            for (int j = 0; j < tmp3[i].length; j ++) {
                tmp3[i][j] /= l;
            }
        }
        double[][] tmpk = multMatrix(tmp1, tmp3);  //(4, 2)
        return tmpk;
    }

    private double[][] reverseArray(double[][] a) {
        double[][] b = new double[a[0].length][a.length];
        for (int i = 0; i < b.length; i ++) {
            for (int j = 0; j < b[0].length; j ++) {
                b[i][j] = a[j][i];
            }
        }
        return b;
    }
}
