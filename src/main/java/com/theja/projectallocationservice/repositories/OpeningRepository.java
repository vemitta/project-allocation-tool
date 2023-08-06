package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.models.DBOpening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpeningRepository extends JpaRepository<DBOpening, Long> {

    // You can define additional custom query methods here if needed
    List<DBOpening> findByProjectId(Long projectId);

    @Query(value = "SELECT o.* FROM openings o LEFT JOIN applications a ON o.id = a.opening_id\n" +
            "    WHERE ((:appliedBy IS null) OR (:appliedBy IS TRUE AND a.candidate_id = :loggedinUserId) OR (:appliedBy IS FALSE AND (a.candidate_id IS null\n" +
            "            OR o.id not in (SELECT op.id FROM openings op JOIN applications ap ON ap.opening_id = op.id WHERE ap.candidate_id = :loggedinUserId))))\n" +
            "    AND ((:postedBy IS null) OR (:postedBy IS TRUE AND o.created_by = :loggedinUserId) OR (:postedBy IS FALSE AND o.created_by != :loggedinUserId)) GROUP BY o.id", nativeQuery = true, countProjection = "*")
    Page<DBOpening> fetchOpenings(Boolean appliedBy, Boolean postedBy, Pageable pageable, Long loggedinUserId);

//    SELECT o.* FROM openings o LEFT JOIN applications a ON o.id = a.opening_id
//    WHERE ((:appliedBy IS null) OR (:appliedBy IS TRUE AND a.candidate_id = :loggedinUserId) OR (:appliedBy IS FALSE AND (a.candidate_id IS null
//            OR o.id not in (SELECT op.id FROM openings op JOIN applications ap ON ap.opening_id = op.id WHERE ap.candidate_id = :loggedinUserId))))
//    AND ((:postedBy IS null) OR (:postedBy IS TRUE AND o.created_by = :loggedinUserId) OR (:postedBy IS FALSE AND o.created_by != :loggedinUserId));
}

