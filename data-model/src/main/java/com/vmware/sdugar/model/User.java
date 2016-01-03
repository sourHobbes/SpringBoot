/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/

package com.vmware.sdugar.model;
/**
 * Author: sdugar
 * Date:   12/8/15
 * Time:   2:03 PM
 */
public class User {
   public long id;
   public String name;
   public String phone;

   public User (long id, String name, String phone) {
      this.id = id;
      this.name = name;
      this.phone = phone;
   }
}
