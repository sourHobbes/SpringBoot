/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.model;


import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;


/**
 * Author: sdugar
 * Date:   12/28/15
 * Time:   2:42 PM
 */
@Entity
public class RPlan {

   @Id
   @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
   @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
   @GeneratedValue(generator = "uuid-gen")
   private UUID planId;

   @Column(unique = true)
   private String planName;

   public String getPlanName() {
      return planName;
   }

   public void setPlanName(String planName) {
      this.planName = planName;
   }

   public UUID getPlanId() {
      return planId;
   }
}
