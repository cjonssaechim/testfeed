package me.saechimdaeki.testfeed.user.domain;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserType {
	ADMIN("관리자"),
	USER("일반 회원"),
	BIZUSER("비즈니스 회원"),
	;

	private final String type;

	public static UserType fromString(String type) {
		return Arrays.stream(values())
			.filter(userType -> userType.name().equalsIgnoreCase(type))
			.findFirst()
			.orElse(USER);
	}

}
