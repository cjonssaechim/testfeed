package me.saechimdaeki.testfeed.feed.service.port;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;

import me.saechimdaeki.testfeed.feed.domain.Feed;

public interface FeedRepository {
	Feed saveFeed(Feed feed);

	List<Feed> findFeedsByCommonFeed(Pageable pageable);

	List<Feed> findFeedsByCursor(LocalDateTime nextCursor, Pageable pageable);
}
