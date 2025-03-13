package me.saechimdaeki.testfeed.feed.service.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saechimdaeki.testfeed.feed.dto.FeedEvent;
import me.saechimdaeki.testfeed.post.domain.Post;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Meta {

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static Meta from(FeedEvent feedEvent) {
		return new Meta(feedEvent.getCreatedAt(), feedEvent.getUpdatedAt());
	}

	public static Meta from(Post post) {
		return new Meta(post.getCreatedAt(), post.getUpdatedAt());
	}
}
