/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.db;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.vmware.sdugar.model.RPlan;

/**
 * Author: sdugar
 * Date:   12/28/15
 * Time:   4:16 PM
 */
@Configurable
@Configuration
@ImportResource("classpath:META-INF/data_source.xml")
public class PgDbService {
   @Autowired
   private RPlanRepository planRepository;
   /*
   @PersistenceContext
   private EntityManager entityManager;
   */
   public String createPlan(String planName) {
      RPlan plan = new RPlan();
      plan.setPlanName(planName);
      RPlan createdPlan = planRepository.save(plan);
      return createdPlan.getPlanId().toString();
   }
}
