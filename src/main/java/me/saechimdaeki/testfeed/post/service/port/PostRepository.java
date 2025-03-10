package me.saechimdaeki.testfeed.post.service.port;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import me.saechimdaeki.testfeed.post.domain.Post;

public interface PostRepository {

	Post savePost(Post post);

	Optional<Post> findById(Long postId);

	Optional<Post> findPostByPostId(Long postId);

	void deletePost(Post post);

	List<Post> findAllPostByPostIds(Collection<Long> postIds);
}
