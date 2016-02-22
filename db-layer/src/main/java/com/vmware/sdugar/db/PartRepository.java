package com.vmware.sdugar.db;

import com.vmware.sdugar.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * sourabhdugar
 * 2/21/16.
 */
public interface PartRepository extends JpaRepository<Part, Integer>,
        QueryDslPredicateExecutor<Part> {

}
