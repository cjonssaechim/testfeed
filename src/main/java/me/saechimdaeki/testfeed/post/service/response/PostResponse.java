package me.saechimdaeki.testfeed.post.service.response;

import java.util.Set;

import lombok.Getter;
import me.saechimdaeki.testfeed.post.domain.Post;

@Getter
public class PostResponse {
	private String title;

	private String content;

	private String imageUrl;

	private String username;

	private Long views;

	private String couponCode;

	private Long usefulCount;

	private Long disappointCount;

	private String postType;

	private Set<String> urls;

	private String category;

	private Long postId;

	public static PostResponse from(Post post, String username) {
		PostResponse postResponse = new PostResponse();
		postResponse.title = post.getTitle();
		postResponse.content = post.getContent();
		postResponse.imageUrl = post.getImageUrl();
		postResponse.username = username;
		postResponse.views = post.getViews();
		postResponse.couponCode = post.getCouponCode();
		postResponse.usefulCount = post.getUsefulCount();
		postResponse.disappointCount = post.getDisappointCount();
		postResponse.postType = String.valueOf(post.getPostType());
		postResponse.urls = post.getUrls();
		postResponse.category = String.valueOf(post.getCategory());
		postResponse.postId = post.getId();
		return postResponse;
	}

	public void changeViews(Long views) {
		this.views = views;
	}
}
