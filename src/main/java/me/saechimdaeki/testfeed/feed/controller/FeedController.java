package me.saechimdaeki.testfeed.feed.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.feed.controller.response.FeedData;
import me.saechimdaeki.testfeed.feed.service.FeedService;
import me.saechimdaeki.testfeed.feed.service.response.FeedResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

	private final FeedService feedService;

	/**
	 * 카테고리별 피드 조회 API
	 * GET /feeds?category={category}&page=0&size=10
	 */
	@GetMapping
	public FeedData<List<FeedResponse>> getFeedsByCategory(
		@RequestParam String category,
		Pageable pageable) {
		List<FeedResponse> feeds = feedService.getUsersFeeds(category, pageable);
		return new FeedData<>(HttpStatus.OK.value(), feeds);
	}

	@GetMapping("/hot")
	public FeedData<List<FeedResponse>> getHotFeeds(
		@RequestParam int start,
		@RequestParam int end) {
		List<FeedResponse> hotFeeds = feedService.getHotFeeds(start, end);
		return new FeedData<>(HttpStatus.OK.value(), hotFeeds);
	}
}
