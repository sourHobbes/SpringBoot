/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.vmware.sdugar.model.User;

/**
 * Author: sdugar
 * Date:   12/8/15
 * Time:   2:08 PM
 */
//@Repository
//@ImportResource("classpath:META-INF/data_source.xml")
public class PgSpring {
   protected final Logger logger = org.slf4j.LoggerFactory.getLogger(PgSpring.class);

   protected JdbcTemplate jdbcTemplate;
   protected JdbcTemplate jdbcTemplateSecondary;

   @Autowired
   @Resource(name = "dataSource2")
   @SuppressWarnings("unused")
   public void setDataSource(DataSource dataSource) {
      jdbcTemplate = new JdbcTemplate(dataSource);
   }

   @Autowired
   @Resource(name = "dataSource")
   @SuppressWarnings("unused")
   public void setSecondaryDataSource(DataSource dataSource) {
      jdbcTemplateSecondary = new JdbcTemplate(dataSource);
   }

   public User getUser(long id) {
      User user = null;
      try {
         user = jdbcTemplate.queryForObject("SELECT * FROM sb_user WHERE id=?", userMapper, id);
      } catch (CannotGetJdbcConnectionException ex) {
         logger.error("Caught exception while querying db", ex);
         logger.info("Switching to secondary data source for the read operation");
         user = jdbcTemplateSecondary.queryForObject(
               "SELECT * FROM sb_user WHERE id=?", userMapper, id);
      }
      return user;
   }

   public int createUser(long id, String name, String phone) {
      return jdbcTemplate.update("INSERT INTO sb_user values (?, ?, ?)", id, name, phone);
   }

   private static final RowMapper<User> userMapper = new RowMapper<User>() {
      public User mapRow(ResultSet rs, int rowNum) throws SQLException {
         User user = new User(rs.getLong("id"), rs.getString("name"), rs.getString("phone"));
         return user;
      }
   };
}
