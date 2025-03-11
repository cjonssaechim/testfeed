package me.saechimdaeki.testfeed.postlike.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.common.domain.response.CommonResponse;
import me.saechimdaeki.testfeed.common.exception.ErrorResponseEntity;
import me.saechimdaeki.testfeed.postlike.service.PostLikeService;

@Tag(name = "게시글 좋아요", description = "게시글 좋아요 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post-likes")
public class PostLikeController {

	private final PostLikeService postLikeService;

	@Operation(summary = "게시글 좋아요", description = "특정 게시글에 좋아요를 추가합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "좋아요 추가 성공"),
		@ApiResponse(responseCode = "400", description = "이미 좋아요 처리한 게시글 (중복 요청)",
			content = @Content(schema = @Schema(implementation = ErrorResponseEntity.class))),
		@ApiResponse(responseCode = "404", description = "게시글 또는 유저를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponseEntity.class))
		)
	})
	@PostMapping("/{postId}")
	public CommonResponse<Void> postLike(
		@Parameter(description = "좋아요할 게시글 ID", example = "1") @PathVariable Long postId,
		@Parameter(description = "좋아요를 누르는 사용자 이름", example = "saechimdaeki") @RequestParam("username") String username) {
		postLikeService.likePost(postId, username);
		return CommonResponse.of(HttpStatus.OK.value(), null);
	}

	@Operation(summary = "게시글 좋아요 취소", description = "특정 게시글의 좋아요를 취소합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
		@ApiResponse(responseCode = "400", description = "해당 유저가 좋아요를 누른 적 없는 게시글",
			content = @Content(schema = @Schema(implementation = ErrorResponseEntity.class))),
		@ApiResponse(responseCode = "404", description = "게시글 또는 유저를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponseEntity.class))),
	})
	@DeleteMapping("/{postId}")
	public CommonResponse<Void> deletePostLike(
		@Parameter(description = "좋아요를 취소할 게시글 ID", example = "1") @PathVariable Long postId,
		@Parameter(description = "좋아요를 취소하는 사용자 이름", example = "saechimdaeki") @RequestParam("username") String username) {
		postLikeService.unlikePost(postId, username);
		return CommonResponse.of(HttpStatus.OK.value(), null);
	}
}
