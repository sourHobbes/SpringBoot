/* **********************************************************************
 * Copyright 2016 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.vmware.sdugar.model.RpVm;

/**
 * Author: sdugar
 * Date:   1/5/16
 * Time:   11:58 PM
 */
@Repository
public interface RpVmRepository extends JpaRepository<RpVm, Long>,
      QueryDslPredicateExecutor<RpVm> {

}
