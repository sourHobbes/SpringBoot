/* **********************************************************************
 * Copyright 2016 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.db;

import java.beans.Transient;
import java.util.Random;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.vmware.sdugar.model.RPlan;

/**
 * Author: sdugar
 * Date:   1/7/16
 * Time:   1:24 PM
 */
public class RPlanRepositoryImpl implements CustomRplanRepository {

   private TransactionTemplate template;
   private EntityManager em;

   @Autowired
   public RPlanRepositoryImpl(PlatformTransactionManager tm, EntityManager em) {
      this.template = new TransactionTemplate(tm);
      this.em = em;
   }

   @Transactional(value = Transactional.TxType.REQUIRES_NEW)
   public String customFindRPlanId(final String name) {
      return template.execute(new TransactionCallback<String>() {
         public String doInTransaction(TransactionStatus status) {

            RPlan plan = new RPlan();
            Query createQuery = em.createNativeQuery(
                  "insert into rpvm values (:id, (select planid from rplan where planname like " +
                        "'newd'));");
            Random random = new Random();
            createQuery.setParameter("id", random.nextInt());
            createQuery.executeUpdate();

            Query query = em.createNativeQuery("select * from rplan where planname like :name",
                  RPlan.class);
            query.setParameter("name", name);
            return ((RPlan)query.getSingleResult()).getPlanName();
         }
      });
   }
}
