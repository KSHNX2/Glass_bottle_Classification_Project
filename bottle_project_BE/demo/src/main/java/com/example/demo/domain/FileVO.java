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

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@ToString
@Table(indexes = { @Index(columnList = "id"), @Index(columnList = "OriginalFileName"), @Index(columnList = "FilePath"),
		@Index(columnList = "SavedFileName"), @Index(columnList = "createdDate") })
public class FileVO {
	@Id
	@GeneratedValue
	private Integer id;
	private String OriginalFileName;
	private String FilePath;
	private String SavedFileName;
	@CreatedDate
	private LocalDateTime createdDate;

	@PrePersist // 시간대 강제지정
	public void prePersist() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		createdDate = LocalDateTime.now(ZoneId.systemDefault());
	}
//	@OneToMany(fetch=FetchType.EAGER)
//	@JoinColumn(name="member_id")
//	private MemberVO memberVO;

}
