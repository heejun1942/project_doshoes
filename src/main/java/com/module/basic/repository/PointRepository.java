package com.module.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.module.basic.model.Point;

public interface PointRepository extends JpaRepository <Point, Long> {
}