package me.saechimdaeki.testfeed.feed.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saechimdaeki.testfeed.feed.dto.FeedEvent;
import me.saechimdaeki.testfeed.post.domain.Post;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
	private Long views;
	private int like;
	private Long share;

	public static Stats from(FeedEvent feedEvent) {
		return new Stats(feedEvent.getViews(), feedEvent.getLike(), feedEvent.getShare());
	}

	public static Stats from(Post post){
		return new Stats(post.getViews(), post.getLikes().size(), post.getShare());
	}
}
