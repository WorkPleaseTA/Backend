package com.example.workmanager.substitute.domain.repository;

import com.example.workmanager.substitute.domain.entity.SubstituteRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubstituteRequestRepository extends JpaRepository<SubstituteRequest, Long> {

    List<SubstituteRequest> findAllByStoreMemberIdOrderByRequestDateDesc(Long storeMemberId);
}