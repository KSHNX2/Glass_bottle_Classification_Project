package com.example.demo.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.DA_LOG;
import com.example.demo.domain.MemberVO;
import com.example.demo.service.DA_LOGService;
import com.example.demo.service.FileService;
import com.example.demo.service.MemberService;

@CrossOrigin("*")
@RestController

public class MemberController {
	@Autowired
	private 
	MemberService memberService;
	@Autowired
	private FileService fileService;
	@Autowired
	private DA_LOGService daLogService;

	@GetMapping("/getMember")
	public List<MemberVO> getMemberList() {
		return memberService.getMemberList();
	}

	@PostMapping(value = "/insertMember", produces = "application/json; charset=UTF-8")
	public void insertMember(@RequestBody MemberVO memberVO) {
		memberService.insertMember(memberVO);
	}

	@DeleteMapping("/deleteMember")
	public void deleteMember(MemberVO memberVO) {
		memberService.deleteMember(memberVO);
	}

	@PutMapping("/updateMember")
	public void updateMember(MemberVO memberVO) {
		memberService.updateMember(memberVO);
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ArrayList<Object> saveFile(@RequestParam MultipartFile file) throws URISyntaxException {

		try {
			return fileService.saveFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Failed to upload file", e);
		}
	}

	@GetMapping("/download")
	public ResponseEntity<byte[]> downFile() throws IOException {
		return fileService.downFile();
	}

	@GetMapping("/download/picture={id}")
	public ResponseEntity<byte[]> downFile(@PathVariable("id") Integer id) throws IOException {
		return fileService.downFile(id);
	}

	@GetMapping("/getdalog")
	public List<DA_LOG> getdaloglist() {
		return fileService.getdaloglist();
	}
//
//	@GetMapping("/getdalogpage")
//	public Page<DA_LOG> getdalogpage(Pageable pageable) {
//		return fileService.getdalogpage(pageable);
//	}
//
//	@GetMapping("/getdalogname")
//	public Page<DA_LOG> getdalogname(Pageable pageable, String name) {
//		return fileService.getdalogfindname(pageable, name);
//	}
//
//	@GetMapping("/getdalogdate")
//	public Page<DA_LOG> getdalogdate(Pageable pageable,
//			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
//			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
//		return fileService.getdalogfinddate(pageable, start, end);
//	}
//
//	@GetMapping("/getdalogconfidence")
//	public Page<DA_LOG> getdalogconfidence(Pageable pageable, @RequestParam("start") String start,
//			@RequestParam("end") String end) {
//		return fileService.getdalogfindconfidence(pageable, start, end);
//	}
//
//	@GetMapping("/getdalogdatename")
//	public Page<DA_LOG> getdalogdatename(Pageable pageable,
//			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
//			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end, String name) {
//		return fileService.getdalogfinddatename(pageable, start, end, name);
//	}

	@GetMapping("/getdalogsearch")
	public Page<DA_LOG> getDA_LOGs(String name1, String name2, String name3,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, String confidenceStart,
			String confidenceEnd, Pageable pageable) {
		return daLogService.getDA_LOGs(name1, name2, name3, startDate, endDate, confidenceStart, confidenceEnd, pageable);

	}
}
