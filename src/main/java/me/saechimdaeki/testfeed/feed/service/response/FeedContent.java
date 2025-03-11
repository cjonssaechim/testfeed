package me.saechimdaeki.testfeed.feed.service.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saechimdaeki.testfeed.feed.dto.FeedEvent;
import me.saechimdaeki.testfeed.post.domain.Post;

@Getter
@NoArgsConstructor
public class FeedContent {
	private String title;
	private String content;
	private String category;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Long views;
	private int like;
	private String imageUrl;

	@Builder
	public FeedContent(String title, String content, String category, LocalDateTime createdAt, LocalDateTime updatedAt,
		Long views, int like, String imageUrl) {
		this.title = title;
		this.content = content;
		this.category = category;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.views = views;
		this.like = like;
		this.imageUrl = imageUrl;
	}

	public static FeedContent from(FeedEvent feedEvent) {
		return FeedContent.builder()
			.title(feedEvent.getTitle())
			.content(feedEvent.getContent())
			.category(feedEvent.getCategory())
			.createdAt(feedEvent.getCreatedAt())
			.updatedAt(feedEvent.getUpdatedAt())
			.views(feedEvent.getViews())
			.like(feedEvent.getLike())
			.imageUrl(feedEvent.getImageUrl())
			.build();
	}

	public static FeedContent from(Post post) {
		return FeedContent.builder()
			.content(post.getContent())
			.title(post.getTitle())
			.category(post.getCategory().name())
			.createdAt(post.getCreatedAt())
			.updatedAt(post.getUpdatedAt())
			.views(post.getViews())
			.like(post.getLikes().size())
			.imageUrl(post.getImageUrl())
			.build();
	}
}
