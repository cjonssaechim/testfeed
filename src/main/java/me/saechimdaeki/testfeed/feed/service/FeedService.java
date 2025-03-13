package me.saechimdaeki.testfeed.feed.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.saechimdaeki.testfeed.feed.domain.Feed;
import me.saechimdaeki.testfeed.feed.service.port.FeedRepository;
import me.saechimdaeki.testfeed.feed.service.response.FeedResponse;
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

	public List<FeedResponse> getUsersFeeds(Pageable pageable) {
		return feedRepository.findFeedsByCommonFeed(pageable)
			.stream()
			.map(FeedResponse::from)
			.toList();
	}

	public List<FeedResponse> getHotFeeds(int start, int end) {
		List<Post> popularPosts = popularPostService.getPopularPosts(start, end);
		popularPosts.forEach(popularPost -> log.info("Popular post: {}", popularPost.getTitle()));
		return popularPosts
			.stream()
			.map(FeedResponse::from)
			.toList();
	}
}
