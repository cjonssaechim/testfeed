package me.saechimdaeki.testfeed.post.domain;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostType {
	AD("광고 게시글"),
	EVENT("이벤트 게시글"),
	NORMAL("일반 게시글"),
	NOTICE("공지 게시글")
	;

	private final String postType;

	public static PostType fromString(String type) {
		return Arrays.stream(values())
			.filter(posttype -> posttype.name().equalsIgnoreCase(type))
			.findFirst()
			.orElse(NORMAL);
	}
}
