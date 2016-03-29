/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/

package com.vmware.sdugar.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Author: sdugar
 * Date:   12/8/15
 * Time:   2:03 PM
 */
@Entity
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public long id;

   @Column
   public String name;

   @Column
   public String phone;

   @Column(unique = true)
   public String userName;

   @Column(nullable = false)
   public String password;

   public User() {

   }

   public User (long id, String name, String phone,
                String userName, String password) {
      this.id = id;
      this.name = name;
      this.phone = phone;
      this.userName = userName;
      this.password = password;
   }

   public long getId() {
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
}
