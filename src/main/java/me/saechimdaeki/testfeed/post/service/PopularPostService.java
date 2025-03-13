package me.saechimdaeki.testfeed.post.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

	public List<Post> getPopularPosts(LocalDateTime cursorTime) {
		// 시간 기준으로 인기글 조회
		Set<Long> postIds = redisTemplate.opsForZSet()
			.reverseRangeByScore(RedisKeyConstants.generateHotArticleKey(),
				cursorTime.atZone(ZoneId.systemDefault()).toEpochSecond(),
				Long.MAX_VALUE);

		// 게시글 데이터를 조회하고 피드를 반환
		List<Post> popularPosts = postRepository.findAllPostByPostIds(postIds);
		return popularPosts;
	}
}