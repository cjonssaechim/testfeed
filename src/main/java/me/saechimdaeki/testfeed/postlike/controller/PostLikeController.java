package me.saechimdaeki.testfeed.postlike.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.postlike.service.PostLikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post-likes")
public class PostLikeController {

	private final PostLikeService postLikeService;

	// 테스트용이며 실제로는 cjone에서 넘어오지 않을까 추측, authServer에서 jwt식으로 넘어오는 것 같음.
	private final String testUsername = "saechimdaeki";

	@PostMapping("/{postId}")
	public void postLike(@PathVariable Long postId) {
		postLikeService.likePost(postId, testUsername);
	}

	@DeleteMapping("/{postId}")
	public void deletePostLike(@PathVariable Long postId) {
		postLikeService.unlikePost(postId, testUsername);
	}
}
