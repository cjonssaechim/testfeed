package me.saechimdaeki.testfeed.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedEvent {
	private Long postId;
	private Long authorId;
	private String title;
	private String content;
	private String username;
	private String category;
}
