package me.saechimdaeki.testfeed.feed.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.service.response.UserResponse;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedEvent {
	private Long postId;
	private UserResponse author;
	private String title;
	private String content;
	private String category;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String imageUrl;
	private Long views;
	private int like;
	private Long share;

	public static FeedEvent from(Post post, User user ) {
		return FeedEvent
			.builder()
			.imageUrl(post.getImageUrl())
			.author(UserResponse.from(user))
			.postId(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.category(post.getCategory().name())
			.createdAt(post.getCreatedAt())
			.updatedAt(post.getUpdatedAt())
			.views(post.getViews())
			.like(post.getLikes().size())
			.share(post.getShare())
			.build();
	}
}
