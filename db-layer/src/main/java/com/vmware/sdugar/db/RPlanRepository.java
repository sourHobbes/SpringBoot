/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.db;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.vmware.sdugar.model.RPlan;

/**
 * Author: sdugar
 * Date:   12/28/15
 * Time:   3:19 PM
 */
@Transactional(readOnly = true)
public interface RPlanRepository extends JpaRepository<RPlan, UUID>,
      QueryDslPredicateExecutor<RPlan> {

   Page<RPlan> findByPlanId(UUID id, Pageable page);

   RPlan findByPlanName(String planName);
}
