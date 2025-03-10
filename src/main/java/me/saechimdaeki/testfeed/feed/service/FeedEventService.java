package me.saechimdaeki.testfeed.feed.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.common.exception.ErrorCode;
import me.saechimdaeki.testfeed.common.util.RedisKeyConstants;
import me.saechimdaeki.testfeed.feed.dto.FeedEvent;
import me.saechimdaeki.testfeed.feed.service.response.FeedResponse;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.exception.PostException;
import me.saechimdaeki.testfeed.post.service.port.PostRepository;

@Service
@RequiredArgsConstructor
public class FeedEventService {

	private final FeedService feedService;
	private final RedisTemplate<String, FeedResponse> redisTemplate;
	private final PostRepository postRepository; // TODO REFACTOR

	@KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.consumer-group}", containerFactory = "kafkaListenerContainerFactory")
	public void handleFeedEvent(@Payload FeedEvent event) {
		String redisKey = RedisKeyConstants.generateCategoryKey(event.getCategory());

		FeedResponse feedResponse = FeedResponse.from(event);
		redisTemplate.opsForList().leftPush(redisKey, feedResponse);

		Post post = postRepository.findPostByPostId(event.getPostId())
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

		feedService.saveFeed(post);

	}

	// 나중에는 개인화 추천으로 userId를 받을 예정, 지금은 카테고리별 피드.
	public List<FeedResponse> retrieveFeedsByCategory(String category, int start, int end) {
		String redisKey = RedisKeyConstants.generateCategoryKey(category);
		List<FeedResponse> categoryFeed = redisTemplate.opsForList().range(redisKey, start, end);
		if (CollectionUtils.isEmpty(categoryFeed)) {
			Pageable pageable = PageRequest.of(start, end);
			return feedService.getUsersFeeds(category, pageable);
		}

		return categoryFeed;
	}

}
