package me.saechimdaeki.testfeed.feed.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.common.domain.response.CommonResponse;
import me.saechimdaeki.testfeed.feed.service.FeedService;
import me.saechimdaeki.testfeed.feed.service.response.FeedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "피드 관리", description = "피드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

	private final FeedService feedService;

	/**
	 * 카테고리별 피드 조회 API
	 * GET /feeds?category={category}&page=0&size=10
	 */
	@Operation(summary = "카테고리별 피드 조회", description = "지정된 카테고리에 해당하는 피드를 페이징하여 조회합니다.")
	@GetMapping
	public CommonResponse<List<FeedResponse>> getFeedsByCategory(
		@Parameter(description = "조회할 피드 카테고리") @RequestParam String category,
		@Parameter Pageable pageable) {
		List<FeedResponse> feeds = feedService.getUsersFeeds(category, pageable);
		return CommonResponse.of(HttpStatus.OK.value(), feeds);
	}

	/**
	 * 인기 피드 조회 API
	 * GET /feeds/hot?start=0&end=10
	 */
	@Operation(summary = "인기 피드 조회", description = "지정된 범위 내에서 인기 피드를 조회합니다.")
	@GetMapping("/hot")
	public CommonResponse<List<FeedResponse>> getHotFeeds(
		@Parameter(description = "조회 시작 인덱스") @RequestParam int start,
		@Parameter(description = "조회 종료 인덱스") @RequestParam int end) {
		List<FeedResponse> hotFeeds = feedService.getHotFeeds(start, end);
		return CommonResponse.of(HttpStatus.OK.value(), hotFeeds);
	}
}
