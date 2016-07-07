/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.rest;

import com.vmware.sdugar.db.UserRepository;
import com.vmware.sdugar.model.PasswordEncoderConfig;
import com.vmware.sdugar.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

   @Autowired
   private PasswordEncoder encoder;

   @RequestMapping("/create")
   public User create(@RequestBody User user) {
      if (users.findByUserName(user.getUserName()) != null) {
         throw new User.UserAlreadyExistsException(
                 user.getUserName(), null);
      }
      user.setPassword(encoder.encode(user.getPassword()));
      user = users.save(user);
      String authToken =
              PasswordEncoderConfig.createJWT(user.getId().toString(),
                      "SOURABH", "AUTH", user.getPassword());
      user.setJwtToken(authToken);
      return new User(user.getName(), user.getPhone(), user.getUserName(),
              "", user.getJwtToken());
   }

   @RequestMapping(value = "/userName", method = RequestMethod.GET)
   public User getUser(@RequestParam("userName") String userName) {
      return users.findByUserName(userName);
   }

   @ExceptionHandler(HttpMessageNotReadableException.class)
   @ResponseStatus(value = HttpStatus.BAD_REQUEST)
   public void handleException(HttpMessageNotReadableException ex,
                               HttpServletResponse response) {

      log.info("Handling " + ex.getClass().getSimpleName(), ex);
      throw ex;
   }

   @RequestMapping(value = "/match")
   public boolean matchPassword(@RequestParam("pass") String password,
                                @RequestParam("user") String userName) {
      User user = users.findByUserName(userName);
      if (user != null) {
         log.info("Found user, trying to match password {} with {}",
                 password, user.getPassword());
         return encoder.matches(password, user.getPassword());
      } else {
         throw new IllegalArgumentException("No such user");
      }
   }

   @RequestMapping(value = "/verify")
   public boolean verifyJwtToken(@RequestParam("token") String token,
                                 @RequestParam("user") String userName) {
      User user = users.findByUserName(userName);
      PasswordEncoderConfig.parseJWT(token, user.getPassword());
      return true;
   }

   @RequestMapping(value = "/api/admin")
   //@PreAuthorize("hasRole('ROLE_ANON')")
   public void getBodyWithApiAdmin(@RequestBody String body) {
      log.info("The request body is :\n{}\n\n", body);
   }
}
