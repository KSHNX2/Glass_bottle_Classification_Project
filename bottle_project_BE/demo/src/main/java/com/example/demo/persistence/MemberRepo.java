package com.example.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.MemberVO;

public interface MemberRepo extends JpaRepository<MemberVO, String> {

}
