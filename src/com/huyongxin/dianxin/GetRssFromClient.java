package com.huyongxin.dianxin;

import java.io.*;
import java.net.Socket;
import com.alibaba.fastjson.JSONObject;

public class GetRssFromClient implements Runnable {

    Socket socket;

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
                jsonObject.put("x", Main.l.FL[0][0] - 385);
                jsonObject.put("y", Main.l.FL[0][1] - 193);
                System.out.println(jsonObject.get("x") + "   " + jsonObject.get("y"));
                bufferedWriter.write(jsonObject.toJSONString() + "\r\n");
                bufferedWriter.flush();
            }
            System.out.println("socket closed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
