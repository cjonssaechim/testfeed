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
	private static final String POST_LIKES_REDIS_KEY = "post-likes::%s";
	private static final String POST_SHARES_REDIS_KEY = "post-shares::%s";
	private static final String COMMON_FEEDS_REDIS_KEY = "common-feeds::%s";

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	public static String generateCommonFeedKey() {
		return String.format(COMMON_FEEDS_REDIS_KEY, LocalDateTime.now().format(TIME_FORMATTER));
	}

	public static String generateCategoryKey(String category) {
		return String.format(CATEGORY_REDIS_KEY, category.toLowerCase());
	}

	public static String generateHotArticleKey() {
		return String.format(HOT_POST_REDIS_KEY, TIME_FORMATTER.format(LocalDateTime.now()));
	}

	public static String generatePostViewsKey(Long postId) {
		return String.format(POST_VIEWS_REDIS_KEY, postId);
	}

	public static String generatePostLikesKey(Long postId) {
		return String.format(POST_LIKES_REDIS_KEY, postId);
	}

	public static String generatePostSharesKey(Long postId) {
		return String.format(POST_SHARES_REDIS_KEY, postId);
	}

}
