/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.model;

import javax.persistence.*;

/**
 * Author: sdugar
 * Date:   12/28/15
 * Time:   3:07 PM
 */
@Entity
public class RpVm {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column()
   @Basic(optional = false)
   private String Name;

   @ManyToOne(optional = false)
   @JoinColumn(name="rplan_planid")
   private RPlan rp;

   public Long getId() {
      return id;
   }

   public RPlan getRp() {
      return rp;
   }

   public void setRp(RPlan rp) {
      this.rp = rp;
   }

   public String getName() {
      return Name;
   }

   public void setName(String name) {
      Name = name;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      RpVm rpVm = (RpVm) o;

      return id != null ? id.equals(rpVm.id) : rpVm.id == null;
   }

   @Override
   public int hashCode() {
      return id != null ? id.hashCode() : 0;
   }
}
