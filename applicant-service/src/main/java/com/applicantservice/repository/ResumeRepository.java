package com.applicantservice.repository;


import com.sharepersistence.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByApplicantId(Long applicantId);
    Optional<Resume> findFirstByApplicantId(Long applicantId);
}