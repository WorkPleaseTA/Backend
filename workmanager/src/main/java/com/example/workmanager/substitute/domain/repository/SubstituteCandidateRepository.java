package com.example.workmanager.substitute.domain.repository;

import com.example.workmanager.substitute.domain.entity.SubstituteCandidate;
import com.example.workmanager.substitute.domain.entity.SubstituteCandidateStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubstituteCandidateRepository extends JpaRepository<SubstituteCandidate, Long> {

    List<SubstituteCandidate> findAllBySubstituteRequestId(Long substituteRequestId);

    List<SubstituteCandidate> findAllByStoreMemberIdOrderByCreatedAtDesc(Long storeMemberId);

    List<SubstituteCandidate> findAllBySubstituteRequestIdAndStatus(
            Long substituteRequestId, SubstituteCandidateStatus status);
}