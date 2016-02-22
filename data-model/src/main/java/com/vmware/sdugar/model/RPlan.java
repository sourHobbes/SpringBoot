/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.model;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * Author: sdugar
 * Date:   12/28/15
 * Time:   2:42 PM
 */
@Entity
public class RPlan {
   @Id
   //@org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
   @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
   @GeneratedValue(generator = "uuid-gen")
   private UUID planId;

   @Column(unique = true)
   private String planName;

   @OneToMany(mappedBy="rp", cascade = CascadeType.ALL, orphanRemoval = true,
      fetch = FetchType.EAGER)
   private List<RpVm> vms = new ArrayList<>();

   public String getPlanName() {
      return planName;
   }

   public void setPlanName(String planName) {
      this.planName = planName;
   }

   public UUID getPlanId() {
      return planId;
   }

   public List<RpVm> getVms() {
      return vms;
   }

   public void setVms(List<RpVm> vms) {
      this.vms = vms;
   }
}
