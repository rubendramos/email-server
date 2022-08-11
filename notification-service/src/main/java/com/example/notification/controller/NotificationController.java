package com.example.notification.controller;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notification.domain.Mail;
import com.example.notification.service.NotificationSenderServiceImpl;

@RestController
@RequestMapping(value = "/notification")
public class NotificationController {

	@Autowired
	NotificationSenderServiceImpl notificationSender;


	@PostMapping(value = "/producer")
	public ResponseEntity<Mail> producer(@RequestBody Mail mail) throws IOException {
		notificationSender.sendNotification(mail);
		return ResponseEntity.ok(mail);
	}


}
