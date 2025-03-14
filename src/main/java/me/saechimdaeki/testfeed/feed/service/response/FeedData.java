package me.saechimdaeki.testfeed.feed.service.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saechimdaeki.testfeed.feed.dto.FeedEvent;
import me.saechimdaeki.testfeed.post.domain.Category;
import me.saechimdaeki.testfeed.post.domain.Post;

@Getter
@NoArgsConstructor
public class FeedData {
	private String title;
	private String body;
	private String category;
	private String categoryId;
	private List<String> images;
	private String flag;
	private String type;
	private LocalDateTime to;
	private LocalDateTime from;

	@Builder
	public FeedData(String title, String body, String category,
		String categoryId,
		String imageUrl, String flag, LocalDateTime to, LocalDateTime from, String type) {
		this.title = title;
		this.body = body;
		this.category = category;
		this.images = List.of(imageUrl);
		this.categoryId = categoryId;
		this.flag = flag;
		this.to = to;
		this.from = from;
		this.type = type;
	}

	public static FeedData from(FeedEvent feedEvent) {
		return FeedData.builder()
			.title(feedEvent.getTitle())
			.body(feedEvent.getContent())
			.category(feedEvent.getCategory())
			.categoryId(Category.fromString(feedEvent.getCategory()).getCategoryId())
			.imageUrl(feedEvent.getImageUrl())
			.type(feedEvent.getPostType())
			.build();
	}

	public static FeedData from(Post post) {
		return FeedData.builder()
			.body(post.getContent())
			.title(post.getTitle())
			.category(post.getCategory().name())
			.categoryId(post.getCategory().getCategoryId())
			.imageUrl(post.getImageUrl())
			.flag(post.getFlag())
			.to(post.getToDate())
			.from(post.getFromDate())
			.type(post.getPostType().name())
			.build();
	}
}
