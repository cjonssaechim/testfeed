package me.saechimdaeki.testfeed.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.common.domain.response.CommonResponse;
import me.saechimdaeki.testfeed.common.exception.ErrorResponseEntity;
import me.saechimdaeki.testfeed.post.service.PostService;
import me.saechimdaeki.testfeed.post.service.request.PostCreateRequest;
import me.saechimdaeki.testfeed.post.service.request.PostUpdateRequest;
import me.saechimdaeki.testfeed.post.service.response.PostResponse;

@Tag(name = "게시글 관리", description = "게시글 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	@Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "게시글 생성 성공",
			content = @Content(schema = @Schema(implementation = PostResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(schema = @Schema(implementation = ErrorResponseEntity.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류",
		content = @Content(schema = @Schema(implementation = Void.class))),
	})
	@PostMapping(consumes = "multipart/form-data")
	public CommonResponse<PostResponse> createPost(
		@Parameter(description = "게시글 정보", required = true)
		@RequestPart(value = "post") @Valid PostCreateRequest postCreateRequest,

		@Parameter(description = "이미지 파일 (선택 사항)")
		@RequestPart(value = "image", required = false) MultipartFile image) {
		return CommonResponse.of(HttpStatus.OK.value(), postService.createPost(postCreateRequest, image));
	}

	@Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponseEntity.class))),
	})
	@PutMapping("/{postId}")
	public CommonResponse<Void> updatePost(
		@Parameter(description = "수정할 게시글 ID") @PathVariable Long postId,
		@Parameter(description = "수정할 내용") @RequestBody @Valid PostUpdateRequest postUpdateRequest) {
		postService.updatePost(postUpdateRequest, postId);
		return CommonResponse.of(HttpStatus.OK.value(), null);
	}

	@Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponseEntity.class))),
	})
	@DeleteMapping("/{postId}")
	public CommonResponse<?> deletePost(
		@Parameter(description = "삭제할 게시글 ID") @PathVariable Long postId,
		@Parameter(description = "작성자 이름") @RequestParam String username) {
		postService.deletePost(postId, username);
		return CommonResponse.of(HttpStatus.NO_CONTENT.value(), null);
	}

	@Operation(summary = "게시글 조회", description = "게시글을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "게시글 조회 성공",
			content = @Content(schema = @Schema(implementation = PostResponse.class))),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponseEntity.class))),
	})
	@GetMapping("/{postId}")
	public CommonResponse<PostResponse> readPost(
		@Parameter(description = "조회할 게시글 ID") @PathVariable Long postId) {
		return CommonResponse.of(HttpStatus.OK.value(), postService.readPost(postId));
	}
}