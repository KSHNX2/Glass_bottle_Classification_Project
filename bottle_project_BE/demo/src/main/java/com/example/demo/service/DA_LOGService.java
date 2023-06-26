package com.example.demo.service;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.domain.DA_LOG;
import com.example.demo.domain.QDA_LOG;
import com.example.demo.persistence.DDALogRepo;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
public class DA_LOGService {
	private final DDALogRepo daLogRepository;
	private final JPAQueryFactory queryFactory;

	@Autowired
	public DA_LOGService(DDALogRepo daLogRepository, EntityManager entityManager) {
		this.daLogRepository = daLogRepository;
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	public Page<DA_LOG> getDA_LOGs(String name1, String name2, String name3, LocalDateTime startDate,
			LocalDateTime endDate, String confidenceStart, String confidenceEnd, Pageable pageable) {
		QDA_LOG qDaLog = QDA_LOG.dA_LOG;
		Predicate predicate = qDaLog.isNotNull().or(qDaLog.isNull()); // Initial predicate, acts as a placeholder
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("ID").descending());

		if (name1 != null) {
			if (name2 != null) {
				if (name3 != null) {
					predicate = whereNameEquals(name1).and(predicate);
					predicate = whereNameEquals(name2).or(predicate);
					predicate = whereNameEquals(name3).or(predicate);
				} else {
					predicate = whereNameEquals(name1).and(predicate);
					predicate = whereNameEquals(name2).or(predicate);
				}
			} else {
				predicate = whereNameEquals(name1).and(predicate);
			}
		}

		if (startDate != null && endDate != null) {
			predicate = whereCreatedDateBetween(startDate, endDate).and(predicate);
		}

		if (confidenceStart != null && confidenceEnd != null) {
			predicate = whereConfidenceBetween(confidenceStart, confidenceEnd).and(predicate);
		}

		return daLogRepository.findAll(predicate, pageable);
	}

	private BooleanExpression whereNameEquals(String name) {
		return QDA_LOG.dA_LOG.name.eq(name);
	}

	private BooleanExpression whereCreatedDateBetween(LocalDateTime start, LocalDateTime end) {
		return QDA_LOG.dA_LOG.createdDate.between(start, end);
	}

	private BooleanExpression whereConfidenceBetween(String start, String end) {
		return QDA_LOG.dA_LOG.confidence.between(start, end);
	}
}
