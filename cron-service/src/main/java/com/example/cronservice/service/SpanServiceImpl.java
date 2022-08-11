package com.example.cronservice.service;

import org.springframework.stereotype.Service;

@Service
public class SpanServiceImpl implements SpanService {

	@Override
	public String updateSpan() {
		return "In updates Span Service";
	}

}
