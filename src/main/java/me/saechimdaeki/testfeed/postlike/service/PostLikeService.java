package me.saechimdaeki.testfeed.postlike.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.saechimdaeki.testfeed.common.exception.ErrorCode;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.exception.PostException;
import me.saechimdaeki.testfeed.post.service.PopularPostService;
import me.saechimdaeki.testfeed.post.service.port.PostRepository;
import me.saechimdaeki.testfeed.postlike.domain.PostLike;
import me.saechimdaeki.testfeed.postlike.exception.PostLikeException;
import me.saechimdaeki.testfeed.postlike.service.port.PostLikeRepository;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.exception.UserException;
import me.saechimdaeki.testfeed.user.service.port.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

	// TODO 시스템 정립되면 .
	private final PostLikeRepository postLikeRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final PopularPostService popularPostService;

	@Transactional
	public void likePost(Long postId, String username) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
		User user = userRepository.findByUserName(username)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

		Optional<PostLike> existingLike = postLikeRepository.findByUserAndPost(user, post);
		if (existingLike.isPresent()) {
			throw new PostLikeException(ErrorCode.POST_LIKE_DUPLICATED);
		}

		PostLike postLike = PostLike.builder()
			.user(user)
			.post(post)
			.build();

		post.addLike(postLike);

		postLikeRepository.save(postLike);

		popularPostService.updatePopularScore(postId, 1.0);
	}

	@Transactional
	public void unlikePost(Long postId, String username) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

		User user = userRepository.findByUserName(username)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

		PostLike postLike = postLikeRepository.findByUserAndPost(user, post)
			.orElseThrow(() -> new PostLikeException(ErrorCode.POST_LIKE_DUPLICATED));

		post.removeLike(postLike);

		postLikeRepository.delete(postLike);
		popularPostService.updatePopularScore(postId, -1.0);

	}
}
