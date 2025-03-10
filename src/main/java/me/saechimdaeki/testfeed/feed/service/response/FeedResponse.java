package me.saechimdaeki.testfeed.feed.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saechimdaeki.testfeed.feed.domain.Feed;
import me.saechimdaeki.testfeed.feed.dto.FeedEvent;
import me.saechimdaeki.testfeed.post.domain.Post;

@Getter
@AllArgsConstructor
public class FeedResponse {
	private Long postId;
	private String title;
	private String content;
	private String username;

	public static FeedResponse from(FeedEvent feedEvent) {
		return new FeedResponse(feedEvent.getPostId(),
			feedEvent.getTitle(), feedEvent.getContent(), feedEvent.getUsername());
	}

	public static FeedResponse from(Feed feed) {
		return new FeedResponse(feed.getPost().getId(), feed.getPost().getTitle(), feed.getPost().getTitle(),
			feed.getUser().getUsername());
	}

	public static FeedResponse from(Post post) {
		return new FeedResponse(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getUsername());
	}
}
