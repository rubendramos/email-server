package com.example.emailservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SpamUpdater {

	private final String url;

	private final WebClient webClient;

	public SpamUpdater(@Value("${setAsSpamApiRest.url}") String url, WebClient webClient) {
		this.webClient = webClient;
		this.url = url;
	}

	/**
	 * Send mail invoques ApiRest
	 * 
	 * @param mail
	 * @return
	 */
	public Flux<Void> updateSpam(String address) {
		//final BodyInserter<Mail, ReactiveHttpOutputMessage> body = BodyInserters.fromObject();
		return this.webClient.mutate().baseUrl(this.url).build()
				.post().uri(address)
			//	.body(body)
				.header("Content-Type", "application/json")
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError,
						clientResponse -> Mono.error(new RuntimeException("Error updating  email address to Spam")))
				.bodyToFlux(Void.class);
	}

}
