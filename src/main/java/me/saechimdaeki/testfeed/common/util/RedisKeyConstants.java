package me.saechimdaeki.testfeed.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisKeyConstants {
	private static final String CATEGORY_REDIS_KEY = "feed::%s";
	private static final String HOT_POST_REDIS_KEY = "hot-article::list::%s";
	private static final String POST_VIEWS_REDIS_KEY = "post-views::%s";

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	public static String generateCategoryKey(String category) {
		return String.format(CATEGORY_REDIS_KEY, category.toLowerCase());
	}

	public static String generateHotArticleKey() {
		return String.format(HOT_POST_REDIS_KEY, TIME_FORMATTER.format(LocalDateTime.now()));
	}

	public static String generatePostViewsKey(Long postId) {
		return String.format(POST_VIEWS_REDIS_KEY, postId);
	}
}
