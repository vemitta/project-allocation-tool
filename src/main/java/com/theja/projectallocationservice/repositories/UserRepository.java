package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.models.DBUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<DBUser, Long> {

    @Query(value = "SELECT * FROM users WHERE id not in (SELECT user_id FROM users_projects);", nativeQuery = true)
    Page<DBUser> getFreePoolUsers(Pageable pageable);

    @Query(value = "SELECT * FROM users WHERE id in (SELECT user_id FROM users_projects WHERE allocated_date between :startDate and :endDate);", nativeQuery = true)
    List<DBUser> getAllAllocatedUsers(Date startDate, Date endDate);

    // You can define additional custom query methods here if needed

}