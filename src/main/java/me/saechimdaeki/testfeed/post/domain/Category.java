package me.saechimdaeki.testfeed.post.domain;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
	FASHION("패션/뷰티","00001"),
	FOOD("식품/외식","00002"),
	LIFE("생활/건강","00003"),
	TRIP("여행/레저","00004"),
	CULTURE("영화/공연/전시","00005"),
	FURNITURE("가구/잡화","00006"),
	DIGITAL("디지털/가전","00007"),
	INVEST("재테크","00008"),
	EDU("교육","00009"),
	GAME("게임/앱","00010"),
	CAR("자동차","00011"),
	ETC("기타","00012");

	private final String categoryName;
	private final String categoryId;

	public static Category fromString(String name) {
		return Arrays.stream(values())
			.filter(category -> category.name().equalsIgnoreCase(name))
			.findFirst()
			.orElse(ETC);
	}
}
