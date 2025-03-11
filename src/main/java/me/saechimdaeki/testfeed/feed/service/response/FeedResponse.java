package me.saechimdaeki.testfeed.feed.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.saechimdaeki.testfeed.feed.domain.Feed;
import me.saechimdaeki.testfeed.feed.dto.FeedEvent;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.service.response.UserResponse;

@Getter
@AllArgsConstructor
@Builder
public class FeedResponse {
	private Long postId;
	private UserResponse author;
	private FeedContent content;


	public static FeedResponse from(FeedEvent feedEvent) {
		return FeedResponse.builder()
			.postId(feedEvent.getPostId())
			.author(feedEvent.getAuthor())
			.content(FeedContent.from(feedEvent))
			.build();
	}

	public static FeedResponse from(Feed feed) {
		Post post = feed.getPost();
		User user = feed.getUser();
		return FeedResponse.builder()
			.postId(post.getId())
			.author(UserResponse.from(user))
			.content(FeedContent.from(post))
			.build();
	}

	public static FeedResponse from(Post post) {
		return FeedResponse.builder()
			.postId(post.getId())
			.author(UserResponse.from(post.getAuthor()))
			.content(FeedContent.from(post))
			.build();
	}
}
