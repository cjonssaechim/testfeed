package me.saechimdaeki.testfeed.postlike.service.port;

import java.util.Optional;

import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.postlike.domain.PostLike;
import me.saechimdaeki.testfeed.user.domain.User;

public interface PostLikeRepository {
	Optional<PostLike> findByUserAndPost(User user, Post post);

	void save(PostLike postLike);

	void delete(PostLike postLike);
}
