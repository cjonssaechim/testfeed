package me.saechimdaeki.testfeed.post.service;

import java.io.IOException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.saechimdaeki.testfeed.common.exception.ErrorCode;
import me.saechimdaeki.testfeed.common.file.FileStorageService;
import me.saechimdaeki.testfeed.common.util.RedisKeyConstants;
import me.saechimdaeki.testfeed.feed.dto.FeedEvent;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.exception.PostException;
import me.saechimdaeki.testfeed.post.service.port.PostRepository;
import me.saechimdaeki.testfeed.post.service.request.PostCreateRequest;
import me.saechimdaeki.testfeed.post.service.request.PostUpdateRequest;
import me.saechimdaeki.testfeed.post.service.response.PostResponse;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.domain.UserType;
import me.saechimdaeki.testfeed.user.exception.UserException;
import me.saechimdaeki.testfeed.user.service.port.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository; // TODO 시스템 구성이 어떻게 될지 아직 미지수.
	private final KafkaTemplate<String, FeedEvent> kafkaTemplate;
	private final RedisTemplate<String, Long> redisTemplate;
	private final FileStorageService fileStorageService;

	// TODO 일단은 postcreate시에만 kafka event 발송.
	@Transactional
	public PostResponse createPost(PostCreateRequest postCreateRequest, MultipartFile image) {
		String username = postCreateRequest.username();
		User user = userRepository.findByUserName(username)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

		String imageUrl = "";
		try {
			imageUrl = fileStorageService.saveFile(image);
		} catch (IOException e) {
			log.error("파일 저장 실패 ", e.getMessage());
		}
		postCreateRequest = postCreateRequest.withImageUrl(imageUrl);
		Post post = PostCreateRequest.create(postCreateRequest, user);
		postRepository.savePost(post);
		FeedEvent feedEvent = new FeedEvent(post.getId(), user.getId(), post.getTitle(), post.getContent(),
			user.getUsername(), post.getCategory().name());
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				FeedEvent feedEvent = new FeedEvent(
					post.getId(),
					user.getId(),
					post.getTitle(),
					post.getContent(),
					user.getUsername(),
					post.getCategory().name()
				);
				kafkaTemplate.send("feed", feedEvent);
			}
		});
		return PostResponse.from(post, username);
	}

	@Transactional
	public void updatePost(PostUpdateRequest postUpdateRequest, Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
		post.updateRequest(postUpdateRequest);
	}

	@Transactional
	public void deletePost(Long postId, String username) {
		Post post = postRepository.findPostByPostId(postId)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
		if (post.getAuthor().getUsername().equals(username) || post.getAuthor().getUserType().equals(UserType.ADMIN)) {
			postRepository.deletePost(post);
			redisTemplate.delete(RedisKeyConstants.generatePostViewsKey(postId));
		}
	}

	public PostResponse readPost(Long postId) {
		Post post = postRepository.findPostByPostId(postId)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

		Long increment = redisTemplate.opsForValue().increment(RedisKeyConstants.generatePostViewsKey(postId));

		// TODO refactoring (현재 조회수는 redis로)
		PostResponse postResponse = PostResponse.from(post, post.getAuthor().getUsername());
		postResponse.changeViews(increment);
		return postResponse;
	}
}
