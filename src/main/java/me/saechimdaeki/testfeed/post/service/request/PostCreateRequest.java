package me.saechimdaeki.testfeed.post.service.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import me.saechimdaeki.testfeed.post.domain.Location;
import me.saechimdaeki.testfeed.post.domain.MoreInfo;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.user.domain.User;

public record PostCreateRequest(
	@NotEmpty(message = "title은 필수 사항입니다")
	String title,
	String body,
	String couponCode,
	String postType,
	String category,
	@NotEmpty(message = "mbrName은 필수 사항입니다")
	String mbrName,
	List<String> images,
	String url,
	String flag,
	long from,
	long to,
	Location location,
	MoreInfo more
) {

	public static Post createPost(PostCreateRequest postCreateRequest, User author) {
		return Post.create(
			postCreateRequest.title,
			postCreateRequest.body,
			postCreateRequest.images,
			author,
			postCreateRequest.couponCode,
			postCreateRequest.category,
			postCreateRequest.postType,
			postCreateRequest.location,
			postCreateRequest.more,
			postCreateRequest.url,
			postCreateRequest.flag,
			postCreateRequest.from,
			postCreateRequest.to
		);
	}


	public PostCreateRequest withImageUrls(List<String> newImageUrl) {
		return new PostCreateRequest(
			this.title,
			this.body,
			this.couponCode,
			this.postType,
			this.category,
			this.mbrName,
			newImageUrl,
			this.url,
			this.flag,
			this.from,
			this.to,
			this.location,
			this.more
		);
	}
}
