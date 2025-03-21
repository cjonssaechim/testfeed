package me.saechimdaeki.testfeed.post.service.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saechimdaeki.testfeed.post.domain.Location;
import me.saechimdaeki.testfeed.post.domain.MoreInfo;

@Getter
@AllArgsConstructor
public class PostUpdateRequest {
	private String title;
	private String body;
	private List<String> images;
	private String couponCode;
	private String postType;
	private Boolean visibility;
	private String url;
	private String flag;
	private Long from;
	private Long to;
	private Location location;
	private MoreInfo more;
}
