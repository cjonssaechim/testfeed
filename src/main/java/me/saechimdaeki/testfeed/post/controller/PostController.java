package me.saechimdaeki.testfeed.post.controller;

import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.post.service.PostService;
import me.saechimdaeki.testfeed.post.service.request.PostCreateRequest;
import me.saechimdaeki.testfeed.post.service.request.PostUpdateRequest;
import me.saechimdaeki.testfeed.post.service.response.PostResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	//응답값도 통일해야 할 수도
	@PostMapping
	public ResponseEntity<PostResponse> createPost(
		@RequestPart(value = "post") @Valid PostCreateRequest postCreateRequest,
		@RequestPart(value = "image", required = false) MultipartFile image) {
		return ResponseEntity.ok(postService.createPost(postCreateRequest, image));
	}

	@PutMapping("/{postId}")
	public ResponseEntity<Void> updatePost(
		@PathVariable Long postId,
		@RequestBody @Valid PostUpdateRequest postUpdateRequest) {
		postService.updatePost(postUpdateRequest, postId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable Long postId, @RequestParam String username) {
		postService.deletePost(postId, username);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostResponse> readPost(@PathVariable Long postId) {
		return ResponseEntity.ok(postService.readPost(postId));
	}
}
