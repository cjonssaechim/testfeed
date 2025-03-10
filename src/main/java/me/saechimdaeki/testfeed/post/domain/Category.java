package me.saechimdaeki.testfeed.post.domain;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
	FASHION("패션뷰티"),
	FOOD("식품외식"),
	LIFE("생활건강"),
	TRIP("여행레저"),
	CULTURE("영화공연전시"),
	FURNITURE("가구잡화"),
	DIGITAL("디지털가전"),
	INVEST("재테크"),
	EDU("교육"),
	GAME("게임앱"),
	CAR("자동차"),
	ETC("기타");

	private final String categoryName;

	public static Category fromString(String name) {
		return Arrays.stream(values())
			.filter(category -> category.name().equalsIgnoreCase(name))
			.findFirst()
			.orElse(null);
	}
}
