package me.saechimdaeki.testfeed.feed.service.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saechimdaeki.testfeed.feed.dto.FeedEvent;
import me.saechimdaeki.testfeed.post.domain.Post;

@Getter
@NoArgsConstructor
public class FeedData {
	private String title;
	private String content;
	private String category;
	private String imageUrl;
	private String flag;
	private LocalDateTime to;
	private LocalDateTime from;

	@Builder
	public FeedData(String title, String content, String category,
		String imageUrl, String flag, LocalDateTime to, LocalDateTime from) {
		this.title = title;
		this.content = content;
		this.category = category;
		this.imageUrl = imageUrl;
		this.flag = flag;
		this.to = to;
		this.from = from;
	}

	public static FeedData from(FeedEvent feedEvent) {
		return FeedData.builder()
			.title(feedEvent.getTitle())
			.content(feedEvent.getContent())
			.category(feedEvent.getCategory())
			.imageUrl(feedEvent.getImageUrl())
			.build();
	}

	public static FeedData from(Post post) {
		return FeedData.builder()
			.content(post.getContent())
			.title(post.getTitle())
			.category(post.getCategory().name())
			.imageUrl(post.getImageUrl())
			.flag(post.getFlag())
			.to(post.getToDate())
			.from(post.getFromDate())
			.build();
	}
}
