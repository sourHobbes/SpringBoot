package com.vmware.sdugar.db;

import com.vmware.sdugar.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * sourabhdugar
 * 2/21/16.
 */
public interface ProductRepository extends
        JpaRepository<Product, Integer>, QueryDslPredicateExecutor<Product> {

}
