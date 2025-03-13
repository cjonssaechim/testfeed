package me.saechimdaeki.testfeed.feed.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.saechimdaeki.testfeed.feed.domain.Feed;
import me.saechimdaeki.testfeed.feed.service.port.FeedRepository;
import me.saechimdaeki.testfeed.feed.service.response.FeedVo;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.service.PopularPostService;
import me.saechimdaeki.testfeed.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FeedService {

	private final FeedRepository feedRepository;
	private final PopularPostService popularPostService;
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

		return feedRepository.findFeedsByCursor(cursorDateTime, pageable)
			.stream()
			.map(FeedVo::from)
			.toList();
	}

	public List<FeedVo> getHotFeeds(String nextCursor) {
		LocalDateTime cursorTime = parseNextCursor(nextCursor);

		List<Post> popularPosts = popularPostService.getPopularPosts(cursorTime);

		return popularPosts.stream()
			.map(FeedVo::from)
			.collect(Collectors.toList());
	}

	private LocalDateTime parseNextCursor(String nextCursor) {
		if (!StringUtils.hasText(nextCursor)) {
			return LocalDateTime.now();
		}
		return LocalDateTime.parse(nextCursor);
	}
}
