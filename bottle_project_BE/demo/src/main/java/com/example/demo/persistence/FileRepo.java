package com.example.demo.persistence;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.FileVO;

public interface FileRepo extends JpaRepository<FileVO, Integer> {
	
}
