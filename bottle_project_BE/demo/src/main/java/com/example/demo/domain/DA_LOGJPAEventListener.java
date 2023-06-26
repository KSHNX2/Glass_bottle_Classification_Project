package com.example.demo.domain;

import java.io.IOException;

import javax.persistence.PostPersist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.service.MyWebSocketHandler;

@Component
public class DA_LOGJPAEventListener {
    @Autowired
    private MyWebSocketHandler webSocketHandler;

    @SuppressWarnings("static-access")
	@PostPersist
    public void DA_LOGPostPersist(DA_LOG dalog) throws IOException {
        webSocketHandler.sendEntityLog(dalog);
    }
}