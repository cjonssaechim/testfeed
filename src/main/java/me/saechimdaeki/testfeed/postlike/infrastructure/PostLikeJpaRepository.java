package me.saechimdaeki.testfeed.postlike.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.postlike.domain.PostLike;
import me.saechimdaeki.testfeed.user.domain.User;

public interface PostLikeJpaRepository extends JpaRepository<PostLike, Long> {
	Optional<PostLike> findByUserAndPost(User user, Post post);
}
