package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.DA_LOG;
import com.example.demo.domain.FileVO;
import com.example.demo.persistence.DALogRepo;
import com.example.demo.persistence.FileRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FileService {
	String TempDAfilepath;
	@Value("${file.upload-dir}")
	private String fileDir;
	@Value("${file.DA-dir}")
	private String DADir;
	@Autowired
	private FileRepo fileRepo;
	@Autowired
	private DALogRepo dalogRepo;

// 파일 업로드
	public ArrayList<Object> saveFile(MultipartFile files) throws IOException, URISyntaxException {
		if (files.isEmpty())
			return null;

		FileVO fileVO = new FileVO();
		ArrayList<Object> daLogList = new ArrayList<>();

		fileVO.setOriginalFileName(files.getOriginalFilename());
		fileVO.setSavedFileName(UUID.randomUUID().toString()
				+ files.getOriginalFilename().substring(files.getOriginalFilename().lastIndexOf(".")));
		fileVO.setFilePath(fileDir + fileVO.getSavedFileName());
		files.transferTo(new File(fileVO.getFilePath()));
		System.out.println(fileVO.toString());
		fileRepo.save(fileVO);

// DA로 post 요청
		Path imagePath = Paths.get(fileVO.getFilePath());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("image", new FileSystemResource(imagePath.toFile()));

		RequestEntity<MultiValueMap<String, Object>> requestEntity = RequestEntity
				.post(new URI("http://10.125.121.228:55000/api/predict")).headers(headers).body(body);

//json 파싱
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
//		response.getStatusCode();
		String jsonResponse = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(jsonResponse);

// Parse image
		JsonNode imageNode = rootNode.get("image");
		String DAsavedfilename = "DA_" + fileVO.getSavedFileName();
		String DAfilepath = DADir + DAsavedfilename;
		TempDAfilepath = DAfilepath;

// 이미지 파일 저장
		byte[] imageData = Base64.getDecoder().decode(imageNode.get("data").asText());

		try {
			Files.write(Paths.get(DAfilepath), imageData, StandardOpenOption.CREATE);
		} catch (IOException e) {
			System.out.println("이미지 저장 중 오류가 발생했습니다: " + e.getMessage());
		}
// Parse bbox
		JsonNode bboxNode = rootNode.get("bbox");
		if (bboxNode.isArray()) {
			for (JsonNode bboxItemNode : bboxNode) {
				DA_LOG daLog = new DA_LOG();
				daLog.setSavedFileName(DAsavedfilename);
				daLog.setOriginalFileName(fileVO.getSavedFileName());
				daLog.setFilePath(DAfilepath);
				daLog.setName(bboxItemNode.get("name").asText());
				daLog.setConfidence(Double.toString(bboxItemNode.get("confidence").asDouble()));
				daLog.setX_max(Double.toString(bboxItemNode.get("xmax").asDouble()));
				daLog.setX_min(Double.toString(bboxItemNode.get("xmin").asDouble()));
				daLog.setY_max(Double.toString(bboxItemNode.get("ymax").asDouble()));
				daLog.setY_min(Double.toString(bboxItemNode.get("ymin").asDouble()));
				dalogRepo.save(daLog);
				daLogList.add(daLog);
			}
		}
		daLogList.add(imageNode);
		return daLogList;

	}

// 파일 다운로드
	public ResponseEntity<byte[]> downFile() throws IOException {
		String filepath = TempDAfilepath;
		Path imagePath = Paths.get(filepath);
		byte[] imageBytes = Files.readAllBytes(imagePath);
// 이미지 파일의 확장자에 따라 Content-Type 헤더를 동적으로 설정
		String extension = StringUtils.getFilenameExtension(filepath);
		MediaType mediaType = MediaType.IMAGE_JPEG;
		if ("png".equalsIgnoreCase(extension)) {
			mediaType = MediaType.IMAGE_PNG;
		} else if ("gif".equalsIgnoreCase(extension)) {
			mediaType = MediaType.IMAGE_GIF;
		}
		return ResponseEntity.ok().contentType(mediaType).body(imageBytes);
	}

	public ResponseEntity<byte[]> downFile(Integer id) throws IOException {
		// TODO Auto-generated method stub

		Path imagePath = Paths.get(dalogRepo.findById(id).get().getFilePath());

		byte[] imageBytes = Files.readAllBytes(imagePath);

		String extension = StringUtils.getFilenameExtension(imagePath.toString());
		MediaType mediaType = MediaType.IMAGE_JPEG;
		if ("png".equalsIgnoreCase(extension)) {
			mediaType = MediaType.IMAGE_PNG;
		} else if ("gif".equalsIgnoreCase(extension)) {
			mediaType = MediaType.IMAGE_GIF;
		}
		return ResponseEntity.ok().contentType(mediaType).body(imageBytes);
	}

	public List<DA_LOG> getdaloglist() {
		// TODO Auto-generated method stub
		return (List<DA_LOG>) dalogRepo.findAll();
	}
//
	public Page<DA_LOG> getdalogpage(Pageable pageable) {
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("ID").descending());
		return dalogRepo.findAll(pageable);
	}
//
//	public Page<DA_LOG> getdalogfindname(Pageable pageable, String name) {
//		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("ID").descending());
//		return dalogRepo.findByName(name, pageable);
//	}
//
//	public Page<DA_LOG> getdalogfinddate(Pageable pageable, LocalDateTime start, LocalDateTime end) {
//		// TODO Auto-generated method stub
//		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("ID").descending());
//		return dalogRepo.findBycreatedDateBetween(start, end, pageable);
//	}
//
//	public Page<DA_LOG> getdalogfinddatename(Pageable pageable, LocalDateTime start, LocalDateTime end, String name) {
//		// TODO Auto-generated method stub
//		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("ID").descending());
//		return dalogRepo.findByNameAndCreatedDateBetween(name, start, end, pageable);
//	}
//
//	public Page<DA_LOG> getdalogfindconfidence(Pageable pageable, String start, String end) {
//		// TODO Auto-generated method stub
//		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("ID").descending());
//		return dalogRepo.findByconfidenceBetween(start, end, pageable);
//	}

}
