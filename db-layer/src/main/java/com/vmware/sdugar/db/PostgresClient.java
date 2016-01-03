/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Author: sdugar
 * Date:   12/8/15
 * Time:   1:18 AM
 */
public class PostgresClient {
   private final static String URL = "jdbc:postgresql://192.168.99.101:5432,192.168.99" +
         ".100:5432/myuser?targetServerType=master";
   private final static String READ_URL = "jdbc:postgresql://192.168.99.100:5432,192.168.99" +
         ".101:5432/myuser?targetServerType=preferSlave";
   static Properties prop = new Properties();
   static Connection conn = null;
   static Connection readConn = null;

   static {
      prop.setProperty("user", "myuser");
      prop.setProperty("password", "mypassword");

      try {
         conn = DriverManager.getConnection(URL, prop);
      } catch (SQLException e) {
         e.printStackTrace();
      }
      try {
         readConn = DriverManager.getConnection(READ_URL, prop);
      } catch (SQLException e) {
         e.printStackTrace();
      }
      try {
         System.out.println("Client info\n" + conn.getClientInfo().toString());
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public void queryDb() throws SQLException {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("select * from test2");

      while (rs.next()) {
         System.out.println(rs.getInt("i") + " " + rs.getString("state"));
      }
   }

   public static void main (String [] args) {
      PostgresClient client = new PostgresClient();
      try {
         client.queryDb();
      } catch (SQLException e) {
         e.printStackTrace();
         conn = readConn;
      }
      try {
         client.queryDb();
      } catch (SQLException e) {
         e.printStackTrace();
         conn = readConn;
      }
      try {
         client.queryDb();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }
}
