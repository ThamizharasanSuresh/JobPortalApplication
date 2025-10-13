package com.jobservice.repository;

import com.sharepersistence.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j WHERE " +
            "LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.skills) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Job> searchJobsIgnoreCase(@Param("keyword") String keyword);

    List<Job> findByEmploymentTypeIgnoreCase(String employmentType);

    List<Job> findByLocationIgnoreCase(String location);

    @Query("SELECT j FROM Job j WHERE LOWER(j.company.name) LIKE LOWER(CONCAT('%', :companyName, '%'))")
    List<Job> findByCompanyNameIgnoreCase(@Param("companyName") String companyName);

    @Query("SELECT j FROM Job j WHERE j.company.id = :companyId")
    List<Job> findByCompanyId(@Param("companyId") Long companyId);
}
