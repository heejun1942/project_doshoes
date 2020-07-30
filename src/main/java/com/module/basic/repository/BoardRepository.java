package com.module.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.module.basic.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}