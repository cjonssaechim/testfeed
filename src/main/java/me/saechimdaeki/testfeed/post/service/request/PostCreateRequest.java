package me.saechimdaeki.testfeed.post.service.request;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.user.domain.User;

public record PostCreateRequest(
	@NotEmpty(message = "title은 필수 사항입니다")
	String title,
	String content,
	String couponCode,
	String postType,
	String category,
	@NotEmpty(message = "username은 필수 사항입니다")
	String username,
	String imageUrl,
	Set<String> urls
) {

	public static Post create(PostCreateRequest request, User user) {
		return Post.create(request.title,
			request.content,
			request.imageUrl,
			user,
			request.couponCode,
			request.postType,
			request.category,
			request.urls);
	}

	public PostCreateRequest withImageUrl(String newImageUrl) {
		return new PostCreateRequest(
			this.title,
			this.content,
			this.couponCode,
			this.postType,
			this.category,
			this.username,
			newImageUrl,
			this.urls
		);
	}
}
