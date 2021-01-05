package com.example.demo;

import java.sql.*;
import java.time.LocalDateTime;

public class OneInsert {

    private static void addbyone() {
        String driverClassName = "com.mysql.jdbc.Driver";	//启动驱动
        String url = "jdbc:mysql://localhost:3306/test_db";	//设置连接路径
        String username = "test_db";	//数据库用户名
        String password = "test_db";	//数据库连接密码
        Connection con = null;		//连接
        PreparedStatement pstmt = null;	//使用预编译语句
        ResultSet rs = null;	//获取的结果集
        for(int i=0;i<100000;i++) {
            try {
                Class.forName(driverClassName); //执行驱动
                con = DriverManager.getConnection(url, username, password); //获取连接
                    String sql = "INSERT INTO orders (orderid, user_id, commodities, order_status, deliver_status, deliver_orderid, deliver_addr, total_price, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; //设置的预编译语句格式
                    pstmt = con.prepareStatement(sql);
                    pstmt.setLong(1, 0);
                    pstmt.setInt(2, 10000001);
                    pstmt.setString(3, "测试订单0001");
                    pstmt.setInt(4, 1);
                    pstmt.setString(5, "1");
                    pstmt.setString(6, "1000001");
                    pstmt.setString(7, "测试地址01");
                    pstmt.setInt(8, 10000);
                    pstmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
                    pstmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                    pstmt.executeUpdate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }finally {
                //关闭资源,倒关
                try {
                    if(rs != null) rs.close();
                    if(pstmt != null) pstmt.close();
                    if(con != null) con.close();  //必须要关
                } catch (Exception e) {
                }
            }
        }
    }

    private static void addbybatch() {
        String driverClassName = "com.mysql.jdbc.Driver";	//启动驱动
        String url = "jdbc:mysql://localhost:3306/test_db";	//设置连接路径
        String username = "test_db";	//数据库用户名
        String password = "test_db";	//数据库连接密码
        Connection con = null;		//连接
        PreparedStatement pstmt = null;	//使用预编译语句
        ResultSet rs = null;	//获取的结果集
        for(int i=0;i<2;i++) {
            try {
                Class.forName(driverClassName); //执行驱动
                con = DriverManager.getConnection(url, username, password); //获取连接


                    String sql = "INSERT INTO orders (orderid, user_id, commodities, order_status, deliver_status, deliver_orderid, deliver_addr, total_price, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; //设置的预编译语句格式
                    pstmt = con.prepareStatement(sql);

                    for(int m=0;m<50;m++) {
                        pstmt.setLong(1, 0);
                        pstmt.setInt(2, 10000001);
                        pstmt.setString(3, "测试订单0001");
                        pstmt.setInt(4, 1);
                        pstmt.setString(5, "1");
                        pstmt.setString(6, "1000001");
                        pstmt.setString(7, "测试地址01");
                        pstmt.setInt(8, 10000);
                        pstmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
                        pstmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                        pstmt.addBatch();
                        pstmt.executeBatch();
                    }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }finally {
                //关闭资源,倒关
                try {
                    if(rs != null) rs.close();
                    if(pstmt != null) pstmt.close();
                    if(con != null) con.close();  //必须要关
                } catch (Exception e) {
                }
            }
        }
    }

    public static void main(String args[]){
        long startTime=System.currentTimeMillis();   //获取开始时间
        addbybatch();  //程序运行时间:251512ms
//        addbyone();  //程序运行时间:2835155ms

        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
    }
}
