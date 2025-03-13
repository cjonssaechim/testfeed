package me.saechimdaeki.testfeed.feed.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

class FeedServiceTest2 {
	@Test
	void abcd() {
		String abd= "202503130752";
		LocalDateTime nextCursor = parseNextCursor(abd);
		System.out.println(nextCursor);
	}

	private LocalDateTime parseNextCursor(String nextCursor) {
		if (!StringUtils.hasText(nextCursor)) {
			return LocalDateTime.now();
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

		return LocalDateTime.parse(nextCursor, formatter);
	}
}