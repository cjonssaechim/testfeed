package me.saechimdaeki.testfeed.feed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
	 * GET /feeds?nextCursor=1
	 */
	@Operation(summary = "게시글 피드 조회", description = "가장 최근에 작성된 글을 내림차순으로 \n 해당하는 피드를 페이징하여 조회합니다.")
	@GetMapping
	@ApiResponse(
		responseCode = "200",
		description = "성공적으로 피드를 조회함",
		content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = FeedResponse.class))
		)
	)
	public CommonResponse<FeedResponse> getFeedsByCategory(
		@Parameter(description = "이 다음 읽을 id값. (선택사항, 기본값은 0)")
		@RequestParam(required = false, defaultValue = "0") Long nextCursor) {
		FeedResponse feedResponse = new FeedResponse(feedService.getUsersFeeds(nextCursor));
		return CommonResponse.of(HttpStatus.OK.value(), feedResponse);
	}

	/**
	 * 인기 피드 조회 API
	 * GET /feeds/hot?nextCursor=1
	 */
	@Operation(summary = "인기 피드 조회", description = "지정된 범위 내에서 인기 피드를 조회합니다.")
	@GetMapping("/hot")
	@ApiResponse(
		responseCode = "200",
		description = "성공적으로 인기 피드를 조회함",
		content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = FeedResponse.class))
		)
	)
	public CommonResponse<FeedResponse> getHotFeeds(
		@Parameter(description = "이 다음 읽을 id값. (선택사항, 기본값은 0)")
		@RequestParam(required = false, defaultValue = "0") Long nextCursor) {
		FeedResponse feedResponse = new FeedResponse(feedService.getHotFeeds(nextCursor));
		return CommonResponse.of(HttpStatus.OK.value(), feedResponse);
	}
}
