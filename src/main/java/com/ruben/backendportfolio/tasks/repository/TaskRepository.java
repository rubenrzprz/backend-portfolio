package com.ruben.backendportfolio.tasks.repository;

import com.ruben.backendportfolio.tasks.domain.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
