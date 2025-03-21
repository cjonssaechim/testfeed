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
public class FeedVo {
	private Long seq;
	private UserResponse author;
	private FeedData content;
	private Stats stats;
	private Meta meta;

	public static FeedVo from(FeedEvent feedEvent, long view, long like, long share) {
		return FeedVo.builder()
			.seq(feedEvent.getPostId())
			.author(feedEvent.getAuthor())
			.content(FeedData.from(feedEvent))
			.stats(Stats.from(view, like, share))
			.build();
	}

	public static FeedVo from(Feed feed, long view, long like, long share) {
		Post post = feed.getPost();
		User user = feed.getUser();
		return FeedVo.builder()
			.seq(post.getId())
			.author(UserResponse.from(user))
			.content(FeedData.from(post))
			.stats(Stats.from(view, like, share))
			.meta(Meta.from(post))
			.build();
	}

	public static FeedVo from(Post post, long view, long like, long share) {
		return FeedVo.builder()
			.seq(post.getId())
			.author(UserResponse.from(post.getAuthor()))
			.content(FeedData.from(post))
			.stats(Stats.from(view, like, share))
			.meta(Meta.from(post))
			.build();
	}
}
