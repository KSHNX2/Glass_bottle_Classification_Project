package com.example.demo.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EntityListeners({ AuditingEntityListener.class, DA_LOGJPAEventListener.class })
@Getter
@Setter
@ToString
@Entity
@Table(indexes = { @Index(columnList = "ID"), @Index(columnList = "OriginalFileName"), @Index(columnList = "FilePath"),
		@Index(columnList = "SavedFileName"), @Index(columnList = "x_min"), @Index(columnList = "x_max"),
		@Index(columnList = "y_max"), @Index(columnList = "y_min"), @Index(columnList = "name"),
		@Index(columnList = "confidence"), @Index(columnList = "createdDate") })
public class DA_LOG {

	@Id
	@GeneratedValue
	private Integer ID;
	private String OriginalFileName;
	private String FilePath;
	private String SavedFileName;
	private String x_min;
	private String x_max;
	private String y_max;
	private String y_min;
	private String name;
	private String confidence;
	@CreatedDate
	private LocalDateTime createdDate;

	@PrePersist // 시간대 강제지정
	public void prePersist() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		createdDate = LocalDateTime.now(ZoneId.systemDefault());
	}

}
