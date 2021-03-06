/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.vmware.sdugar.db.RPlanRepository;

/**
 * Author: sdugar
 * Date:   12/8/15
 * Time:   2:38 PM
 */
@EnableAutoConfiguration
@ComponentScan(
      basePackages = {"com.vmware.sdugar.db",
                      "com.vmware.sdugar.app",
                      "com.vmware.sdugar.rest",
                      "com.vmware.sdugar.model"}
)
public class App {

   public static void main(String[] args) throws Throwable {
      /*
      ClassPathXmlApplicationContext applicationContext =
            new ClassPathXmlApplicationContext(new String[] {"classpath:application-context.xml"});
      System.out.println("application context is " + applicationContext);
      */
      SpringApplication app = new SpringApplication(App.class);
      app.setBannerMode(Banner.Mode.CONSOLE);
      app.run(args);
   }
}
