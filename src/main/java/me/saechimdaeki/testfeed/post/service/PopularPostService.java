package me.saechimdaeki.testfeed.post.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.saechimdaeki.testfeed.common.util.RedisKeyConstants;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.service.port.PostRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PopularPostService {

	private final RedisTemplate<String, Long> redisTemplate;
	private final PostRepository postRepository;

	public void updatePopularScore(Long postId, double likeCountDelta) {
		redisTemplate.opsForZSet()
			.incrementScore(RedisKeyConstants.generateHotArticleKey(), postId, likeCountDelta);
	}

	public List<Post> getPopularPosts(int start, int end) {
		Set<Long> postIds = redisTemplate.opsForZSet()
			.reverseRange(RedisKeyConstants.generateHotArticleKey(), start, end - 1);

		if (CollectionUtils.isEmpty(postIds)) {
			return Collections.emptyList();
		}

		return postRepository.findAllPostByPostIds(postIds);
	}
}