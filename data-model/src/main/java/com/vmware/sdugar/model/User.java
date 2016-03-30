/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/

package com.vmware.sdugar.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.UUID;

/**
 * Author: sdugar
 * Date:   12/8/15
 * Time:   2:03 PM
 */
@Entity
public class User {

   @Id
   @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
   @GeneratedValue(generator = "uuid-gen")
   private UUID id;

   @Autowired
   @Transient
   private PasswordEncoder encoder;

   @Column
   private String name;

   @Column
   private String phone;

   @Column(unique = true)
   private String userName;

   @Column
   private String password;

   @Transient
   private String jwtToken;

   public User() {

   }

   public User(String name, String phone,
               String userName, String password,
               String jwtToken) {
      this.name = name;
      this.phone = phone;
      this.userName = userName;
      this.password = password;
      this.jwtToken = jwtToken;
   }

   public UUID getId() {
      return this.id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPassword() {
      return password;
   }

   public static class UserAlreadyExistsException
           extends RuntimeException {
      public UserAlreadyExistsException(String userName, Throwable cause) {
         super("User name " + userName + " already taken!", cause);
      }
   }

   public String getJwtToken() {
      return jwtToken;
   }

   public void setJwtToken(String jwtToken) {
      this.jwtToken = jwtToken;
   }
}
