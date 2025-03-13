package me.saechimdaeki.testfeed.feed.infrastructure;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import me.saechimdaeki.testfeed.feed.domain.Feed;

public interface FeedJpaRepository extends JpaRepository<Feed, Long> {
	@Query("""
			SELECT f FROM Feed f 
			JOIN FETCH f.post p 
			JOIN FETCH f.user u
			ORDER BY f.createdAt DESC
		""")
	List<Feed> findFeedsByCommonFeed(Pageable pageable);

	@Query("""
        SELECT f FROM Feed f 
        WHERE  f.createdAt < :nextCursor
        ORDER BY f.createdAt DESC
    """)
	List<Feed> findFeedsByCursor(@Param("nextCursor") LocalDateTime nextCursor, Pageable pageable);
}
