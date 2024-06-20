package com.ffi.backofficehq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ffi.backofficehq.entity.User;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM m_staff WHERE photo = :token AND ROWNUM = 1", nativeQuery = true)
    Optional<User> findFirstByPhoto(@Param("token") String token);
    
    @Query(value = "SELECT * FROM m_staff WHERE staff_code = :staffCode AND ROWNUM = 1", nativeQuery = true)
    Optional<User> findFirstByStaffCode(@Param("staffCode") String staffCode);

}
