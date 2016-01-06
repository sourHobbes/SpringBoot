/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.db;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.vmware.sdugar.model.QRPlan;
import com.vmware.sdugar.model.QRpVm;
import com.vmware.sdugar.model.RPlan;
import com.vmware.sdugar.model.RpVm;

/**
 * Author: sdugar
 * Date:   12/28/15
 * Time:   4:16 PM
 */
@Configurable
@Configuration
///*not needed */@ImportResource("classpath:META-INF/data_source.xml")
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class PgDbService {
   @Autowired
   private RPlanRepository planRepository;

   @Autowired
   private RpVmRepository vmRepository;

   protected final Logger log = LoggerFactory.getLogger(getClass());

   /*
   //not needed
   @PersistenceContext
   private EntityManager entityManager;
   */

   @Transactional(rollbackFor = NullPointerException.class)
   public String createPlan(String planName) {
      RPlan plan = new RPlan();
      plan.setPlanName(planName);
      RPlan createdPlan = planRepository.save(plan);
      log.info("Finding a non existing plan");
      RPlan created_plan = planRepository.findByPlanName("thisDoesNotExist");
      //planRepository.delete(createdPlan.getPlanId());
      log.info("Search for non existent plan failed, plan is {}", created_plan);
      return planName;
      //return created_plan.getPlanId().toString();
   }

   @Transactional(readOnly = true)
   public List<RpVm> queryPlanbyName(String planName) {
      Predicate nameIs = QRPlan.rPlan.planName.containsIgnoreCase(planName);
      Iterable<RPlan> plans = planRepository.findAll(nameIs);
      Predicate selectVms = QRpVm.rpVm.rp.planName.containsIgnoreCase(planName);
      Iterable<RpVm> vms = vmRepository.findAll(selectVms);
      return Lists.newArrayList(vms);
   }
}
