package me.saechimdaeki.testfeed.post.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.saechimdaeki.testfeed.common.exception.ErrorCode;
import me.saechimdaeki.testfeed.common.file.FileStorageService;
import me.saechimdaeki.testfeed.common.util.RedisKeyConstants;
import me.saechimdaeki.testfeed.feed.service.port.FeedRepository;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.event.dto.PostCreatedEvent;
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
	private final UserRepository userRepository; // TODO 시스템 구성이 어떻게 될지 아직 미지수. 결국엔 이 기능은 빼야 정상.
	private final RedisTemplate<String, Long> redisTemplate;
	private final FileStorageService fileStorageService;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final FeedRepository feedRepository;

	// TODO 일단은 postcreate시에만 kafka event 발송.
	@Transactional
	public PostResponse createPost(PostCreateRequest postCreateRequest, List<MultipartFile> images) {
		String mbrName = postCreateRequest.mbrName();

		User user = userRepository.findByMbrName(mbrName)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

		List<String> imageUrls = new ArrayList<>();
		if (!CollectionUtils.isEmpty(images)) {
			try {
				imageUrls = fileStorageService.saveFiles(images);
			} catch (IOException e) {
				log.error("Error while saving images", e);
			}
		}

		PostCreateRequest postCreate = postCreateRequest.withImageUrls(imageUrls);

		Post post = postRepository.savePost(Post.create(
			postCreate.title(),
			postCreate.body(),
			postCreate.images(),
			user,
			postCreate.couponCode(),
			postCreate.category(),
			postCreate.postType(),
			postCreate.location(),
			postCreate.more(),
			postCreate.url(),
			postCreate.flag(),
			postCreate.from(),
			postCreate.to()
		));

		applicationEventPublisher.publishEvent(new PostCreatedEvent(post, user));

		return PostResponse.from(post, mbrName);
	}

	@Transactional
	public void updatePost(PostUpdateRequest postUpdateRequest, Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
		post.updateRequest(postUpdateRequest);
	}

	@Transactional
	public void deletePost(Long postId, String mbrName) {
		Post post = postRepository.findPostByPostId(postId)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
		if (post.getAuthor().getMbrName().equals(mbrName) || post.getAuthor().getUserType().equals(UserType.ADMIN)) {
			feedRepository.deleteByPostId(postId);
			postRepository.deletePost(post);
			redisTemplate.delete(RedisKeyConstants.generatePostViewsKey(postId));
		}
	}

	public PostResponse readPost(Long postId) {
		Post post = postRepository.findPostByPostId(postId)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

		Long increment = redisTemplate.opsForValue().increment(RedisKeyConstants.generatePostViewsKey(postId));

		// TODO refactoring (현재 조회수는 redis로)
		PostResponse postResponse = PostResponse.from(post, post.getAuthor().getMbrName());
		postResponse.changeViews(increment);
		return postResponse;
	}

	public String sharePost(Long postId) {
		Post post = postRepository.findPostByPostId(postId)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
		Long increment = redisTemplate.opsForValue().increment(RedisKeyConstants.generatePostSharesKey(postId));
		return "공유할 게시글의 shortUrl?";
	}
}
