package com.example.demo.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class MemberVO {
	@Id
	@GeneratedValue
	private int member_num;
	private String member_name;
	private String member_email;
	private String member_id;
	private String member_pass;
	@CreatedDate
	private LocalDateTime createdDate;

//	@Override
//	public String toString() {
//		return "Member [num=" + num + ", name=" + name + ", pass=" + newpw+"]";
//	}

}