package me.saechimdaeki.testfeed.post.service.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUpdateRequest {
	private String title;
	private String content;
	private String imageUrl;
	private String couponCode;
	private String postType;
	private Boolean visibility;
	private Set<String> urls;
	private String flag;
	private Long from;
	private Long to;
}
