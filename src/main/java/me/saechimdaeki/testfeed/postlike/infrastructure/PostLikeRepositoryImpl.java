package me.saechimdaeki.testfeed.postlike.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.postlike.domain.PostLike;
import me.saechimdaeki.testfeed.postlike.service.port.PostLikeRepository;
import me.saechimdaeki.testfeed.user.domain.User;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepository {
	private final PostLikeJpaRepository postLikeJpaRepository;

	@Override
	public Optional<PostLike> findByUserAndPost(User user, Post post) {
		return postLikeJpaRepository.findByUserAndPost(user, post);
	}

	@Override
	public void save(PostLike postLike) {
		postLikeJpaRepository.save(postLike);
	}

	@Override
	public void delete(PostLike postLike) {
		postLikeJpaRepository.delete(postLike);
	}
}
