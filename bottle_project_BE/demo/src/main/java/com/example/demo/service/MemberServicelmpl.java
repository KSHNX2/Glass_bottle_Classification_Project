package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.MemberVO;
import com.example.demo.persistence.MemberRepo;

@Service
public class MemberServicelmpl implements MemberService {
	public MemberVO tempMemberVO;
	
	@Autowired
	private MemberRepo memberRepo;	
	
	@Override
	public List<MemberVO> getMemberList() {
		// TODO Auto-generated method stub
		return (List<MemberVO>) memberRepo.findAll();
	}

	@Override
	public void insertMember(MemberVO memberVO) {
		// TODO Auto-generated method stub
		tempMemberVO=memberVO;
		memberRepo.save(memberVO);		
	}

	@Override
	public MemberVO getMember(MemberVO memberVO) {
		// TODO Auto-generated method stub
		return memberRepo.findById(memberVO.getMember_name()).get();
	}

	@Override
	public void updateMember(MemberVO memberVO) {
		// TODO Auto-generated method stub
		MemberVO findMemberVO = memberRepo.findById(memberVO.getMember_name()).get();
		findMemberVO.setMember_pass(memberVO.getMember_pass());
		memberRepo.save(findMemberVO);
	}

	@Override
	public void deleteMember(MemberVO memberVO) {
		// TODO Auto-generated method tub
		memberRepo.deleteById(memberVO.getMember_id());
		
	}

	@Override
	public List<MemberVO> getMemberList(MemberVO memberVO) {
		// TODO Auto-generated method stub
		return null;
	}

}
