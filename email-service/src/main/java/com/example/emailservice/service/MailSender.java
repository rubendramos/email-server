package com.example.emailservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.emailservice.domain.Mail;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MailSender {


	private final String url;

	private final WebClient webClient;
	public MailSender(@Value("${sendMailApiRest.url}") String url, WebClient webClient) {		
		this.webClient = webClient;
		this.url = url;
	}

	/**
	 * Send mail invoques ApiRest
	 * @param mail
	 * @return
	 */
	public Flux<Void> send(Mail mail) {
		 final BodyInserter<Mail, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(mail);
			    return this.webClient.mutate().baseUrl(this.url).build().post()
			        .body(body)			        
			        .header("Content-Type","application/json")
			        .retrieve()
			        .onStatus(HttpStatus::is4xxClientError, clientResponse ->
			            Mono.error(new RuntimeException("Error sending mail"))
			        ).bodyToFlux(Void.class);
		
	}
	
	

}
