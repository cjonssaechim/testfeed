package me.saechimdaeki.testfeed.feed.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.feed.domain.Feed;
import me.saechimdaeki.testfeed.feed.service.port.FeedRepository;
import me.saechimdaeki.testfeed.feed.service.response.FeedResponse;
import me.saechimdaeki.testfeed.post.domain.Category;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.service.PopularPostService;
import me.saechimdaeki.testfeed.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

	private final FeedRepository feedRepository;
	private final PopularPostService popularPostService;

	@Transactional
	public void saveFeed(Post post) {
		User author = post.getAuthor();

		Feed feed = Feed.builder()
			.post(post)
			.user(author)
			.build();

		Feed feed1 = feedRepository.saveFeed(feed);

	}

	public List<FeedResponse> getUsersFeeds(String category, Pageable pageable) {
		return feedRepository.findFeedsByCategory(Category.fromString(category), pageable)
			.stream()
			.map(FeedResponse::from)
			.toList();
	}

	public List<FeedResponse> getHotFeeds(int start, int end) {
		return popularPostService.getPopularPosts(start, end)
			.stream()
			.map(FeedResponse::from)
			.toList();
	}
}
