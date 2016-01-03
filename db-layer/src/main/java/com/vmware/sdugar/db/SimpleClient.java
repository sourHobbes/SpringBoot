/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/

/**
 * Author: sdugar
 * Date:   11/30/15
 * Time:   6:45 PM
 */
package com.vmware.sdugar.db;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.set;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class SimpleClient {
   private Cluster cluster;
   private Session session;

   static final String dc1ReplicationMap = "{'class' : 'NetworkTopologyStrategy', 'dc1' : 1}";
   static final String dc2ReplicationMap = "{'class' : 'NetworkTopologyStrategy', 'dc2' : 1}";
   static final String dc1dc2ReplicationMap = "{'class' : 'NetworkTopologyStrategy'," +
         " 'dc2' : 1, 'dc1' : 1}";

   public Session getSession() {
      return this.session;
   }

   public void connect(String node) {
      cluster = Cluster.builder()
            .addContactPoint(node)
            .build();
      Metadata metadata = cluster.getMetadata();
      System.out.printf("Connected to cluster: %s\n",
            metadata.getClusterName());
      for ( Host host : metadata.getAllHosts() ) {
         System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n",
               host.getDatacenter(), host.getAddress(), host.getRack());
      }
      session = cluster.connect();
   }

   public void createSchema(String ksName, String replicationMapString) {
      session.execute("CREATE KEYSPACE IF NOT EXISTS " + ksName + " WITH replication = " +
            replicationMapString);
   }

   public void loadData() {
      session.execute(
            "INSERT INTO simplex4.songs (id, title, album, artist, tags) " +
                  "VALUES (" +
                  "756716f7-2e54-4715-9f00-91dcbea6cf50," +
                  "'La Petite Tonkinoise'," +
                  "'Bye Bye Blackbird'," +
                  "'Joséphine Baker'," +
                  "{'jazz', '2013'})" +
                  ";");
      session.execute(
            "INSERT INTO simplex4.playlists (id, song_id, title, album, artist) " +
                  "VALUES (" +
                  "2cc9ccb7-6221-4ccb-8387-f22b6a1b354d," +
                  "756716f7-2e54-4715-9f00-91dcbea6cf50," +
                  "'La Petite Tonkinoise'," +
                  "'Bye Bye Blackbird'," +
                  "'Joséphine Baker'" +
                  ");");
   }

   public void lwtUpdate() {

      /*Statement stmnt = new SimpleStatement("UPDATE simplex4.songs SET title='Tonk La Petite'" +
                         "WHERE id=756716f7-2e54-4715-9f00-91dcbea6cf50");*/

      Statement stmnt = QueryBuilder.update("simplex3", "songs")
            .with(set("title", "La PetiteX1"))
            .where(eq("id", UUID.fromString("756716f7-2e54-4715-9f00-91dcbea6cf50")))
            .onlyIf(eq("title", "La Petite4"));
      stmnt.setConsistencyLevel(ConsistencyLevel.ALL);
      ResultSet results = session.execute(stmnt);

   }

   public void querySchema() {
      Statement stmt = new SimpleStatement("SELECT * FROM simplex3.songs " +
            "WHERE id = 756716f7-2e54-4715-9f00-91dcbea6cf50;");
      stmt.setConsistencyLevel(ConsistencyLevel.ONE);
      ResultSet results = session.execute(stmt);
      System.out.println(String.format("%-30s\t%-20s\t%-20s\n%s", "title", "album", "artist",
            "-------------------------------+-----------------------+--------------------"));
      for (Row row : results) {
         System.out.println(String.format("%-30s\t%-20s\t%-20s", row.getString("title"),
               row.getString("album"),  row.getString("artist")));
      }
      System.out.println();
   }

   public void insertInts(String ksName, String tbName) {
      int i = 0;
      for (; i < 100; i++) {
         Statement stmt = new SimpleStatement(String.format(
               "INSERT INTO " + ksName + "." + tbName + "(id, state) values (" + i + ", " +
                     "'" + i+1 +
                     "');"
         ));
         Statement stmt2 = QueryBuilder.insertInto(ksName, tbName)
               .value("id", i)
               .value("state", Integer.valueOf(i + 1000).toString());
               //.ifNotExists();
         /*
         System.out.println(String.format(
               "INSERT INTO " + ksName + "." + tbName + "(id, state) values (" + i + ",'" + i+1 +
                     "');"
         ));
         */
         try {
            session.execute(stmt2);
         } catch (Exception ex) {
            System.out.println("Failed to make an LWT insert with error " + ex.getMessage());
         }
      }
   }

   public void insertInts(String ksName, String tbName, int from) {
      int i = from;
      for (; i < from + 100; i++) {
         Statement stmt2 = QueryBuilder.insertInto(ksName, tbName)
               .value("id", i)
               .value("state", Integer.valueOf(i).toString());

         try {
            session.execute(stmt2);
         } catch (Exception ex) {
            System.out.println("Failed to make an insert with error " + ex.getMessage());
         }
      }
   }

   public void deleteInts(String ksName, String tbName, int from) {
      int i = from;
      for (; i < 500; i++) {
         Statement stmt = QueryBuilder.delete().from(ksName, tbName).where(eq("id", i));
         session.execute(stmt);
      }
   }

   public void insertIntsBet200And500(String ksName, String tbName) {
      int i = 200;
      for (; i < 500; i++) {
         Statement stmt2 = QueryBuilder.insertInto(ksName, tbName)
               .value("id", i)
               .value("state", Integer.valueOf(i).toString());

         try {
            session.execute(stmt2);
         } catch (Exception ex) {
            System.out.println("Failed to make an LWT insert with error " + ex.getMessage());
         }
      }
   }

   public void updateIntsLwt(String ksName, String tbName) {
      int i = 0;
      for (; i < 100; i++) {
         Statement stmt = QueryBuilder.update(ksName, tbName)
               .with(set("state", Integer.valueOf(i).toString()))
               .where(eq("id", i))
               .onlyIf(eq("state", Integer.valueOf(i + 1000).toString()));
         try {
            session.execute(stmt);
         } catch (Exception ex) {
            System.out.println("Caught exception while doing an LWT update" + ex.getMessage());
         }
      }
   }

   public void updateIntsLwt(String ksName, String tbName, int from) {
      int i = from;
      for (; i < from + 100; i++) {
         Statement stmt = QueryBuilder.update(ksName, tbName)
               .with(set("state", Integer.valueOf(i).toString()))
               .where(eq("id", i + 900))
               .onlyIf(eq("state", Integer.valueOf(900 + i).toString()));
         try {
            session.execute(stmt);
         } catch (Exception ex) {
            System.out.println("Caught exception while doing an LWT update" + ex.getMessage());
         }
      }
   }

   public class IntRowComparator implements Comparator<Row> {

      public int compare(Row o1, Row o2) {
         if (o1.getInt("id") > o2.getInt("id")) {
            return 1;
         } else if (o1.getInt("id") == o2.getInt("id")) {
            return 0;
         } else {
            return -1;
         }
      }
   }

   public void alterKs(String ksName, String replicationMap) {
      Statement stmt = new SimpleStatement("alter keyspace " + ksName + " with replication = "
         + replicationMap + ";");
      session.execute(stmt);
   }


   public void queryInts() {
      Statement stmt = new SimpleStatement("select * from ints;");
      ResultSet results = session.execute(stmt);
      List<Row> rowList = results.all();
      Collections.sort(rowList, new IntRowComparator());
      for (Row row : rowList) {
         System.out.println(String.format("%-30s	%-20s", row.getInt("id"),
               row.getString("state")));
      }
   }

   public void useSchema(String ksName) {
      Statement stmt = new SimpleStatement("use " + ksName);
      stmt.setConsistencyLevel(ConsistencyLevel.LOCAL_ONE);
      ResultSet results = session.execute(stmt);
      System.out.println(results.toString());
   }

   public void close() {
      session.close();
      cluster.close();
   }

   public void test1() {
      this.deleteInts("dc2_users", "ints", 200);
      this.alterKs("dc2_users", dc1dc2ReplicationMap);
      this.insertIntsBet200And500("dc2_users", "ints");
   }

   public void test2() {
      this.queryInts();
      this.updateIntsLwt("dc2_users", "ints");
   }

   public void test3() {
      this.insertIntsBet200And500("dc2_users", "ints");
      this.queryInts();
   }

   public void singleFailureInsertNewData() {
      this.insertInts("dc2_users", "ints", 900);
   }

   public void singleFailureUpdateLwt() {
      this.updateIntsLwt("dc2_users", "ints", 0);
   }

   public static void main(String[] args) {
      SimpleClient clientDc1 = new SimpleClient();

      System.out.println("The ip supplied for connection is " + args[0].toString());
      clientDc1.connect(args[0]);
      clientDc1.createSchema("dc2_users", dc1dc2ReplicationMap);
      clientDc1.useSchema("dc2_users");
      SchemaLoader.loadSchema(clientDc1.getSession(), "META-INF/users.cdl");
      /*
      //clientDc1.alterKs("dc2_users", dc2ReplicationMap);
      SchemaLoader.loadSchema(clientDc1.getSession(), "META-INF/users.cdl");
      //clientDc1.deleteInts("dc2_users", "ints");
      clientDc1.insertInts("dc2_users", "ints");
      //clientDc1.updateIntsLwt("dc1_users", "ints");
      clientDc1.insertIntsGt100("dc2_users", "ints");
      clientDc1.alterKs("dc2_users", dc1dc2ReplicationMap);
      clientDc1.insertIntsBet200And500("dc2_users", "ints");
      clientDc1.queryInts();
      */
      /*
      clientDc1.test1();
      clientDc1.queryInts();
      */
      /*
      clientDc1.test2();
      clientDc1.queryInts();
      */
      /*
      clientDc1.singleFailureInsertNewData();
      clientDc1.queryInts();
      */
      clientDc1.queryInts();
      /*
      clientDc1.test3();
      clientDc1.singleFailureUpdateLwt();
      clientDc1.queryInts();
      */
      System.exit(0);
   }
}
