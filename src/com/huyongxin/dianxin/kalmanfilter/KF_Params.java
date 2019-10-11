package com.huyongxin.dianxin.kalmanfilter;

/**
 * @author HuYongxin
 * @description: 卡尔曼滤波算法参数类
 */
public class KF_Params {
    double B = 0;  //外部输入
    double u = 0;  //外部输入
    double[][] K;  //卡尔曼增益  (4, 2)
    public double[][] z;  //观测值z  (2, 1)

    double[][] P = new double[4][4];

    //初始状态 x,y,vx,vy, 函数外部提供初始化的状态，本例使用观察值进行初始化，Vx，Vy初始为0
    public double[][] x = new double[4][1];

    //状态转移矩阵A
    //和线性系统的预测机制有关，这里的线性系统是上一刻的位置加上速度等于当前时刻的位置，而速度本身保持不变
    double[][] A = new double[4][4];

    //预测噪声协方差矩阵Q：假设预测过程上叠加一个高斯噪声，协方差矩阵为Q
    //大小取决于对预测过程的信任程度。比如，假设认为运动目标在y轴上的速度可能不匀速，那么可以把这个对角矩阵的最后一个值调大。有时希望出来的轨迹更平滑，可以把这个调更小
    double[][] Q = new double[4][4];

    //观测矩阵H  z = H * x
    //这里的状态是（坐标x， 坐标y， 速度x， 速度y），观察值是（坐标x， 坐标y），所以H = eye(2, 4)
    double[][] H = new double[2][4];

    //观测噪声协方差矩阵：假设观测过程上存在一个高斯噪声，协方差矩阵为R
    //大小取决于对观察过程的信任程度。比如，假设观测结果中的坐标x值常常很准确，那么矩阵R的第一个值应该比较小
    double[][] R = new double[2][2];

    public KF_Params(double x, double y, double vx, double vy) {

        //初始化x
        this.x[0][0] = x;
        this.x[1][0] = y;
        this.x[2][0] = vx;
        this.x[3][0] = vy;
        //初始化P
        for (int i = 0; i < P.length; i ++) {
            for (int j = 0; j < P[i].length; j ++) {
                P[i][j] = 0;
            }
        }

        //初始化A
        for (int i = 0; i < A.length; i ++) {
            for (int j = 0; j < A[i].length; j ++) {
                if (i == j) {
                    A[i][j] = 1;
                } else {
                    A[i][j] = 0;
                }
            }
        }
        A[0][2] = 1;
        A[1][3] = 1;

        //初始化Q
        for (int i = 0; i < Q.length; i ++) {
            for (int j = 0; j < Q[i].length; j ++) {
                if (i == j) {
                    Q[i][j] = 0.001;
                } else {
                    Q[i][j] = 0;
                }
            }
        }

        //初始化H
        for (int i = 0; i < H.length; i ++) {
            for (int j = 0; j < H[i].length; j ++) {
                H[i][j] = 0;
            }
        }
        H[0][0] = 1;
        H[1][1] = 1;

        //初始化R
        for (int i = 0; i < R.length; i ++) {
            for (int j = 0; j < R[i].length; j ++) {
                if (i == j) {
                    R[i][j] = 2;
                } else {
                    R[i][j] = 0;
                }
            }
        }

    }
}
