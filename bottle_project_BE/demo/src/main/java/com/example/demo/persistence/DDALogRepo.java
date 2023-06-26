package com.example.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.demo.domain.DA_LOG;

public interface DDALogRepo extends JpaRepository<DA_LOG, Integer>, QuerydslPredicateExecutor<DA_LOG> {

}
