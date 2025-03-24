package me.saechimdaeki.testfeed.feed.event.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saechimdaeki.testfeed.post.domain.Location;
import me.saechimdaeki.testfeed.post.domain.MoreInfo;
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
	private List<String> images;
	private String type;
	private LocalDateTime to;
	private LocalDateTime from;
	private Location location;
	private MoreInfo more;


	public static FeedEvent from(Post post, User user ) {
		return FeedEvent
			.builder()
			.images(post.getImages())
			.author(UserResponse.from(user))
			.postId(post.getId())
			.title(post.getTitle())
			.content(post.getBody())
			.category(post.getCategory().name())
			.createdAt(post.getCreatedAt())
			.updatedAt(post.getUpdatedAt())
			.type(post.getPostType().name())
			.to(post.getToDate())
			.from(post.getFromDate())
			.location(post.getLocation())
			.more(post.getMore())
			.build();
	}
}
