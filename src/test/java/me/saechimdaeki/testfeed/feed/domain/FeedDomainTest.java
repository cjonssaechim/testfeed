package me.saechimdaeki.testfeed.feed.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import me.saechimdaeki.testfeed.feed.infrastructure.FeedJpaRepository;

@DataJpaTest
class FeedDomainTest {

	@Autowired
	private FeedJpaRepository feedJpaRepository;

	@Test
	@DisplayName("id, post, user가 모두 동일해야 feed는 같은 객체라고 판단한다")
	void equalsTest() {
		Feed feed1 = Feed.builder()
			.post(null)
			.user(null)
			.build();

		Feed feed2 = Feed.builder()
			.post(null)
			.user(null)
			.build();

		feedJpaRepository.save(feed1);
		feedJpaRepository.save(feed2);

		assertThat(feed1).isNotEqualTo(feed2);
	}

	@Test
	@DisplayName("id, post, user가 전부 비어있다면 feed는 같은 객체라고 판단한다")
	void equalsTest2() {
		Feed feed1 = Feed.builder()
			.post(null)
			.user(null)
			.build();

		Feed feed2 = Feed.builder()
			.post(null)
			.user(null)
			.build();

		assertThat(feed1).isEqualTo(feed2);

	}

}