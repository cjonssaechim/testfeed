package me.saechimdaeki.testfeed.feed.service.response;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedResponse {
	private List<FeedVo> feeds;
	private String nextCursor;

	public FeedResponse(List<FeedVo> feeds) {
		this.feeds = feeds;
		if (!CollectionUtils.isEmpty(feeds)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
			this.nextCursor = feeds.get(feeds.size() - 1).getMeta().getCreatedAt().format(formatter);
		}
	}
}
