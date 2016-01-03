/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.vmware.sdugar.model.RPlan;

/**
 * Author: sdugar
 * Date:   12/28/15
 * Time:   3:19 PM
 */
@Transactional(readOnly = true)
public interface RPlanRepository extends JpaRepository<RPlan, Long> {

   Page<RPlan> findByPlanId(Long Id, Pageable page);

   RPlan findByPlanName(String planName);
}
