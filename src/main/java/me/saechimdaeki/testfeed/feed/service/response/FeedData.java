package me.saechimdaeki.testfeed.feed.service.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saechimdaeki.testfeed.feed.dto.FeedEvent;
import me.saechimdaeki.testfeed.post.domain.Category;
import me.saechimdaeki.testfeed.post.domain.Location;
import me.saechimdaeki.testfeed.post.domain.MoreInfo;
import me.saechimdaeki.testfeed.post.domain.Post;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
	private Location location;
	private MoreInfo more;

	@Builder
	public FeedData(String title, String body, String category, String categoryId, List<String> images, String flag,
		String type, LocalDateTime to, LocalDateTime from, Location location, MoreInfo more) {
		this.title = title;
		this.body = body;
		this.category = category;
		this.categoryId = categoryId;
		this.images = images;
		this.flag = flag;
		this.type = type;
		this.to = to;
		this.from = from;
		this.location = location;
		this.more = more;
	}

	public static FeedData from(FeedEvent feedEvent) {
		return FeedData.builder()
			.title(feedEvent.getTitle())
			.body(feedEvent.getContent())
			.category(Category.fromString(feedEvent.getCategory()).getCategoryName())
			.categoryId(Category.fromString(feedEvent.getCategory()).getCategoryId())
			.images(feedEvent.getImages())
			.type(feedEvent.getType())
			.location(feedEvent.getLocation())
			.more(feedEvent.getMore())
			.build();
	}

	public static FeedData from(Post post) {
		return FeedData.builder()
			.body(post.getBody())
			.title(post.getTitle())
			.category(post.getCategory().getCategoryName())
			.categoryId(post.getCategory().getCategoryId())
			.images(post.getImages())
			.flag(post.getFlag())
			.to(post.getToDate())
			.from(post.getFromDate())
			.type(post.getPostType().name())
			.location(post.getLocation())
			.more(post.getMore())
			.build();
	}
}
