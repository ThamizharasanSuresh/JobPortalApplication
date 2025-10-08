package com.applicantservice.repository;


import com.sharepersistence.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByApplicantId(Long applicantId);
}
