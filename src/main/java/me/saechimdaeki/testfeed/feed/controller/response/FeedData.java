package me.saechimdaeki.testfeed.feed.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FeedData<T> {
	int statusCode;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	String message;
	T feeds;

	public FeedData(int statusCode, T feeds) {
		this.statusCode = statusCode;
		this.feeds = feeds;
	}
}
