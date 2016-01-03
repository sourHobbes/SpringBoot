/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.vmware.sdugar.db.PgDbService;
import com.vmware.sdugar.db.PgSpring;
import com.vmware.sdugar.db.RPlanRepository;
import com.vmware.sdugar.model.RPlan;
import com.vmware.sdugar.model.User;

/**
 * Author: sdugar
 * Date:   12/8/15
 * Time:   2:29 PM
 */
@RestController
@RequestMapping("user")
@SuppressWarnings("unused")
public class UserController {
   protected final Logger log = LoggerFactory.getLogger(getClass());

   @Autowired
   private PgSpring users;

   @Autowired
   private PgDbService dbService;

   @RequestMapping("test")
   public String test() {
      log.info("Test");
      return "OK";
   }

   @RequestMapping("user")
   public User getUser(@RequestParam("id") long id) {
      log.info("fetching user with id {}", id);
      return users.getUser(id);
   }

   @RequestMapping("create_user")
   public String createUser(@RequestParam("id") long id,
                         @RequestParam("name") String name,
                         @RequestParam("phone") String phone) {
      log.info("Creating user with id : {}, name : {}, phone : {}", id, name, phone);
      users.createUser(id, name, phone);
      return "OK";
   }

   @RequestMapping("create_plan")
   public String createPlan(@RequestParam("name") String planName) {
      log.info("Creating a recovery plan with name : {}", planName);
      return dbService.createPlan(planName);
   }

   /*
   @RequestMapping("users")
   public List<User> getUsers(@RequestParam("ids") long[] ids) {
      log.info("Get users");
      return users.getUsers(ids);
   }
   */
}
