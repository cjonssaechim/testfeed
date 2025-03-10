package me.saechimdaeki.testfeed.post.domain;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostType {
	AD("광고 게시글"),
	BIZ("비즈니스 파트너 게시글"),
	NORMAL("일반 게시글"),
	;

	private final String postType;

	public static PostType fromString(String type) {
		return Arrays.stream(values())
			.filter(posttype -> posttype.name().equalsIgnoreCase(type))
			.findFirst()
			.orElse(null);
	}
}
