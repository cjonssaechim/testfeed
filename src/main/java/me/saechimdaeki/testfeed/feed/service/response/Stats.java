package me.saechimdaeki.testfeed.feed.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
	private Long view;
	private Long like;
	private Long share;

	public static Stats from(long view, long like, long share) {
		return new Stats(view, like, share);
	}
}
