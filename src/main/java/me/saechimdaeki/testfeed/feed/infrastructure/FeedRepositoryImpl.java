package me.saechimdaeki.testfeed.feed.infrastructure;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.feed.domain.Feed;
import me.saechimdaeki.testfeed.feed.service.port.FeedRepository;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepository {

	private final FeedJpaRepository feedJpaRepository;

	@Override
	public Feed saveFeed(Feed feed) {
		return feedJpaRepository.save(feed);
	}

	@Override
	public List<Feed> findFeedsByCommonFeed(Pageable pageable) {
		return feedJpaRepository.findFeedsByCommonFeed(pageable);
	}
}
