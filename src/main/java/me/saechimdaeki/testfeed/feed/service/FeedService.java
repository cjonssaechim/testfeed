package me.saechimdaeki.testfeed.feed.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.saechimdaeki.testfeed.common.util.RedisKeyConstants;
import me.saechimdaeki.testfeed.feed.domain.Feed;
import me.saechimdaeki.testfeed.feed.service.port.FeedRepository;
import me.saechimdaeki.testfeed.feed.service.response.FeedVo;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FeedService {

	private final FeedRepository feedRepository;
	private final RedisTemplate<String, Long> redisTemplate;

	@Transactional
	public Feed saveFeed(Post post) {
		User author = post.getAuthor();

		Feed feed = Feed.builder()
			.post(post)
			.user(author)
			.build();

		return feedRepository.saveFeed(feed);
	}

	public List<FeedVo> getUsersFeeds(String nextCursor) {
		Pageable pageable = PageRequest.of(0, 5);
		LocalDateTime cursorDateTime = parseNextCursor(nextCursor);
		List<Feed> feedsByCursor = feedRepository.findFeedsByCursor(cursorDateTime, pageable);

		List<Long> postIds = feedsByCursor.stream()
			.map(feed -> feed.getPost().getId())
			.toList();

		List<String> viewKeys = postIds.stream()
			.map(RedisKeyConstants::generatePostViewsKey)
			.toList();
		List<String> shareKeys = postIds.stream()
			.map(RedisKeyConstants::generatePostSharesKey)
			.toList();
		List<String> likeKeys = postIds.stream()
			.map(RedisKeyConstants::generatePostLikesKey)
			.toList();

		List<Long> views = redisTemplate.opsForValue().multiGet(viewKeys);
		List<Long> shares = redisTemplate.opsForValue().multiGet(shareKeys);
		List<Long> likes = redisTemplate.opsForValue().multiGet(likeKeys);

		List<FeedVo> feedVos = new ArrayList<>();
		for (int i = 0; i < feedsByCursor.size(); i++) {
			Feed feed = feedsByCursor.get(i);
			Long view = views.get(i) != null ? views.get(i) : 0L;
			Long share = shares.get(i) != null ? shares.get(i) : 0L;
			Long like = likes.get(i) != null ? likes.get(i) : 0L;
			feedVos.add(FeedVo.from(feed, view, like, share));
		}

		return feedVos;
	}

	private LocalDateTime parseNextCursor(String nextCursor) {
		if (!StringUtils.hasText(nextCursor)) {
			return LocalDateTime.now();
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

		return LocalDateTime.parse(nextCursor, formatter);
	}
}
