package com.example.workmanager.substitute.domain.repository;

import com.example.workmanager.substitute.domain.entity.SubstituteRequest;
import com.example.workmanager.substitute.domain.entity.SubstituteRequestStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubstituteRequestRepository extends JpaRepository<SubstituteRequest, Long> {

    List<SubstituteRequest> findAllByStoreMemberIdOrderByRequestDateDesc(Long storeMemberId);

    List<SubstituteRequest> findAllByStoreMemberStoreIdAndRequestDateAndStatus(
            Long storeId, LocalDate requestDate, SubstituteRequestStatus status);
}