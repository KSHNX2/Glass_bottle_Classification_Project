package com.example.demo.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.demo.domain.DA_LOG;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

	@Autowired
	private FileService fileService;

	private static Set<WebSocketSession> sessions = new HashSet<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		sessions.add(session);
		Pageable pageable = PageRequest.of(0, 15, Sort.by("ID").descending());

		try {
			session.sendMessage(new TextMessage(convertObjectToJson(fileService.getdalogpage(pageable))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
	}

// 웹소켓으로 메시지를 전송
	public static void sendEntityLog(DA_LOG daLog) throws IOException {
		for (WebSocketSession session : sessions) {
			try {
				session.sendMessage(new TextMessage(convertObjectToJson(daLog)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

//	private static byte[] convertToBytes(Object object) throws IOException {
//		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				ObjectOutputStream out = new ObjectOutputStream(bos)) {
//			out.writeObject(object);
//			return bos.toByteArray();
//		}
//	}

	private static String convertObjectToJson(Object obj) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}