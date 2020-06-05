package com.dreamteam.corona.quarantine.repository;

import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.quarantine.model.Quarantine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QuarantineRepository extends JpaRepository<Quarantine, Long> {

//    @Query(
//            value = "SELECT q " +
//                    "FROM Quarantine q " +
//                    "WHERE " +
//                    " q.user = ?1 " +
//                    " AND q.startDate <= ?2 " +
//                    " AND q.endDate >= ?3 "
//    )
//    Optional<Quarantine> findActiveQuarantineForUser(User user, LocalDateTime startDate, LocalDateTime endDate);

    Optional<Quarantine> findByUserAndActiveTrue(User user);

    List<Quarantine> findAllByActiveTrue();

    Long countByUser(User user);
}
