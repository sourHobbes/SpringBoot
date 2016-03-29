package com.vmware.sdugar.db;

import com.vmware.sdugar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sourabhdugar on 3/26/16.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Integer countByName(String name);

    User findByUserName(String userName);
}
