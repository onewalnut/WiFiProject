package com.huyongxin.dianxin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    private static String url="jdbc:mysql://localhost:3306/map";
    private static String name="root";
    private static String password="123456";
    private static Connection conn;
    public static Statement stmt;

    public DB() {
        try {
            //加载驱动程序；
            Class.forName("com.mysql.jdbc.Driver");
            //获得数据库连接；
            conn=DriverManager.getConnection(url,name,password);
            //通过数据库的连接操作数据库，实现增删改查。
            stmt=conn.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
