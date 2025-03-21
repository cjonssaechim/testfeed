package me.saechimdaeki.testfeed.post.service.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import me.saechimdaeki.testfeed.post.domain.Location;
import me.saechimdaeki.testfeed.post.domain.MoreInfo;
import me.saechimdaeki.testfeed.post.domain.Post;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {
	private String title;

	private String body;

	private List<String> images;

	private String mbrName;

	private String nickName;

	private Long view;

	private String couponCode;

	private String postType;

	private String url;

	private String category;

	private Long postId;

	private String flag;

	private LocalDateTime from;

	private LocalDateTime to;

	private Location location;

	private MoreInfo more;

	public static PostResponse from(Post post, String mbrName) {
		PostResponse postResponse = new PostResponse();
		postResponse.title = post.getTitle();
		postResponse.body = post.getBody();
		postResponse.images = post.getImages();
		postResponse.mbrName = mbrName;
		postResponse.view = post.getViews();
		postResponse.couponCode = post.getCouponCode();
		postResponse.postType = String.valueOf(post.getPostType());
		postResponse.url = post.getUrl();
		postResponse.category = String.valueOf(post.getCategory());
		postResponse.postId = post.getId();
		postResponse.flag = post.getFlag();
		postResponse.from = post.getFromDate();
		postResponse.to = post.getToDate();
		postResponse.location = post.getLocation();
		postResponse.more = post.getMore();
		return postResponse;
	}

	public void changeViews(Long views) {
		this.view = views;
	}
}
