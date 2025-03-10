package me.saechimdaeki.testfeed.post.infrastructure;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.service.port.PostRepository;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepository {

	private final PostJpaRepository postJpaRepository;

	@Override
	public Post savePost(Post post) {
		return postJpaRepository.save(post);
	}

	@Override
	public Optional<Post> findById(Long postId) {
		return postJpaRepository.findById(postId);
	}

	@Override
	public Optional<Post> findPostByPostId(Long postId) {
		return postJpaRepository.findPostByPostId(postId);
	}

	@Override
	public void deletePost(Post post) {
		postJpaRepository.delete(post);
	}

	@Override
	public List<Post> findAllPostByPostIds(Collection<Long> postIds) {
		return postJpaRepository.findAllByPostIds(postIds);
	}
}
