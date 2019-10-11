package com.huyongxin.dianxin;

import java.io.*;
import java.net.Socket;
import com.alibaba.fastjson.JSONObject;
import com.huyongxin.dianxin.kalmanfilter.KF_Params;
import com.huyongxin.dianxin.kalmanfilter.KalmanFilter;

public class GetRssFromClient implements Runnable {

    Socket socket;
    int i = 1;
    KalmanFilter kalmanFilter = new KalmanFilter();
    KF_Params kf_params;

    GetRssFromClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("WiFi Socket Connected!");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String str;
            double[] target;
            JSONObject jsonObject;
            while ((str = bufferedReader.readLine()) != null) {
                jsonObject = JSONObject.parseObject(str);
                System.out.println(jsonObject.toJSONString());
                target = new double[Main.APSIZE];
                for (int i = 0; i < Main.APSIZE; i ++) {
                    target[i] = Double.parseDouble(jsonObject.get(Main.AP[i]).toString());
                }
                Main.l.startLocation(target);
                System.out.println((Main.l.FL[0][0] - 385) + "   " + (Main.l.FL[0][1] - 193));
                if (i == 1) {
                    kf_params = kalmanFilter.initKalman(Main.l.FL[0][1] - 385, Main.l.FL[0][1] - 193, 0, 0);
                    i ++;
                } else {
                    double[][] z = new double[2][1];
                    z[0][0] = Main.l.FL[0][0] - 385;
                    z[1][0] = Main.l.FL[0][1] - 193;
                    kf_params.z = z;
                    kf_params = kalmanFilter.updateParams(kf_params);
                }
//                jsonObject.put("x", Main.l.FL[0][0] - 385);
//                jsonObject.put("y", Main.l.FL[0][1] - 193);
                jsonObject.put("x", kf_params.x[0][0]);
                jsonObject.put("y", kf_params.x[1][0]);
                System.out.println(jsonObject.get("x") + "   " + jsonObject.get("y"));
                System.out.println("---------------------------------------");
                bufferedWriter.write(jsonObject.toJSONString() + "\r\n");
                bufferedWriter.flush();
            }
            System.out.println("socket closed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
