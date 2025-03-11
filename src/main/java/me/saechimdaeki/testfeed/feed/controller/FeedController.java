package me.saechimdaeki.testfeed.feed.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.common.domain.response.CommonResponse;
import me.saechimdaeki.testfeed.feed.service.FeedService;
import me.saechimdaeki.testfeed.feed.service.response.FeedResponse;

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
	@Operation(summary = "게시글이 생긴 순서대로 피드를 페이징하여 조회합니다(현재는 데이터 없기에)", description = "가장 최근에 작성된 글을 내림차순으로 해당하는 피드를 페이징하여 조회합니다.")
	@GetMapping
	public CommonResponse<List<FeedResponse>> getFeedsByCategory(
		@Parameter(description = "조회 시작 인덱스(선택사항 기본 값 0)") @RequestParam(required = false, defaultValue = "0") int start,
		@Parameter(description = "조회 시작할 게시글 갯수 (선택사항 기본 값 10)")@RequestParam(required = false, defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(start, size);
		List<FeedResponse> feeds = feedService.getUsersFeeds(pageable);
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
