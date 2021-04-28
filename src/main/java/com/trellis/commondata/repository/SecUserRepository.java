package com.trellis.commondata.repository;

import com.trellis.commondata.model.SecUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;

@Repository
@PersistenceContext(name = "commondataEM")
public interface SecUserRepository extends JpaRepository<SecUser, Long> {


}
