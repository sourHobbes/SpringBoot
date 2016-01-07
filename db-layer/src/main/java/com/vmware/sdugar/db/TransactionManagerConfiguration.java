/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.db;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Author: sdugar
 * Date:   12/28/15
 * Time:   4:24 PM
 */
@Configuration
@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = {"com.vmware.sdugar.db"})
public class TransactionManagerConfiguration {
   @Autowired
   EntityManagerFactory emf;

   @Value("#{dataSource}")
   private DataSource dataSource;

   @Bean(name = "transactionManager")
   public PlatformTransactionManager transactionManager() {
      JpaTransactionManager tm =
            new JpaTransactionManager();
      tm.setEntityManagerFactory(emf);
      tm.setDataSource(dataSource);
      return tm;
   }
}
