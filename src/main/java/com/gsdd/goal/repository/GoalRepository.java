package com.gsdd.goal.repository;

import com.gsdd.goal.persistence.entities.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
