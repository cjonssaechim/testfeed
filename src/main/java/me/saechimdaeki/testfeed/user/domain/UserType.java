package me.saechimdaeki.testfeed.user.domain;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserType {
	ADMIN("관리자"),
	MEMBER("일반 회원"),
	BUSINESS("비즈니스 회원"),
	;

	private final String type;

	public static UserType fromString(String type) {
		return Arrays.stream(values())
			.filter(userType -> userType.name().equalsIgnoreCase(type))
			.findFirst()
			.orElse(MEMBER);
	}

}
