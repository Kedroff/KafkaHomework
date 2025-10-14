package org.example.clientprocessing.repository;

import org.example.clientprocessing.model.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
}
