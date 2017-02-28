package com;

import org.junit.Test;

import java.sql.*;

/**
 * Created by Administrator on 2017/2/27.
 */
public class MySql {

    @Test
    public void testQuery(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:///kaishengit_db","root","root");

            String sql = "{call p_1()}";
            CallableStatement stat = conn.prepareCall(sql);
            ResultSet rs = stat.executeQuery();
            while(rs.next()){
                String prodName = rs.getString("prod_desc");
                System.out.println(prodName);
            }
            rs.close();
            stat.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInparam(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:///kaishengit_db","root","root");
            String sql = "{call p_2(?)}";
            CallableStatement stat = conn.prepareCall(sql);
            stat.setString(1,"abc");
            stat.executeUpdate();

            stat.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




}
