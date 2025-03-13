package me.saechimdaeki.testfeed.feed.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public Feed saveFeed(Post post) {
		User author = post.getAuthor();

		Feed feed = Feed.builder()
			.post(post)
			.user(author)
			.build();

		return feedRepository.saveFeed(feed);
	}

	public List<FeedVo> getUsersFeeds(Long nextCursor) {
		Pageable pageable = PageRequest.of(0, 5);

		return feedRepository.findFeedsByCursor(nextCursor, pageable)
			.stream()
			.map(FeedVo::from)
			.toList();
	}

	public List<FeedVo> getHotFeeds(Long nextCursor) {
		List<Post> popularPosts = popularPostService.getPopularPosts(nextCursor, nextCursor+5);
		popularPosts.forEach(popularPost -> log.info("Popular post: {}", popularPost.getTitle()));
		return popularPosts
			.stream()
			.map(FeedVo::from)
			.toList();
	}
}
