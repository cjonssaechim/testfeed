package me.saechimdaeki.testfeed.post.infrastructure;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import me.saechimdaeki.testfeed.post.domain.Post;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

	@Query("select p from Post p "
		+ "join fetch p.author "
		+ "left join fetch p.url "
		+ "where p.id = :postId")
	Optional<Post> findPostByPostId(@Param("postId") Long postId);

	@Query("select p from Post p "
		+ "join fetch p.author "
		+ "where p.id in :postIds")
	List<Post> findAllByPostIds(@Param("postIds") Collection<Long> postIds);
}
