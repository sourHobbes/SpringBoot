/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.rest;

import com.vmware.sdugar.db.UserRepository;
import com.vmware.sdugar.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
   private UserRepository users;

   @RequestMapping("/create")
   public User create(@RequestBody User user) {
      if (users.findByUserName(user.getUserName()) != null) {
         throw new User.UserAlreadyExistsException(
                 user.getUserName(), null);
      }
      return users.save(user);
   }

   @RequestMapping(value = "/userName", method = RequestMethod.GET)
   public User getUser(@RequestParam("userName") String userName) {
      return users.findByUserName(userName);
   }
}
