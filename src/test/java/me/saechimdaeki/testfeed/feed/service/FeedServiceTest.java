package me.saechimdaeki.testfeed.feed.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import me.saechimdaeki.testfeed.feed.domain.Feed;
import me.saechimdaeki.testfeed.feed.service.port.FeedRepository;
import me.saechimdaeki.testfeed.feed.service.response.FeedResponse;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.service.PopularPostService;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.domain.UserType;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

	@Mock
	private FeedRepository feedRepository;

	@Mock
	private PopularPostService popularPostService;

	@InjectMocks
	private FeedService feedService;

	final String testUsername = "testUser";
	final UserType testUserType = UserType.USER;

	User testUser = User.builder()
		.username(testUsername)
		.userType(testUserType)
		.build();

	Post testPost = Post.builder()
		.content("content")
		.author(testUser)
		.build();

	@Test
	@DisplayName("피드가 정상적으로 저장되었다면 저장된 피드의 id를 반환해야 한다")
	void saveFeedTest_success() {

		// given

		Feed feed = Feed.builder()
			.user(testUser)
			.post(testPost)
			.build();

		BDDMockito.given(feedRepository.saveFeed(feed)).willReturn(feed);

		// when

		Feed savedFeed = feedService.saveFeed(testPost);

		// then
		assertThat(savedFeed.getUser()).isNotNull();
		assertThat(savedFeed.getUser()).isEqualTo(testUser);
		assertThat(savedFeed.getPost()).isNotNull();
		assertThat(savedFeed.getPost()).isEqualTo(testPost);
	}

	@Test
	@DisplayName("피드 컨텐츠를 조회 했을 때 데이터가 있다면 정의한 피드형식의 리스트로 데이터를 응답받아야 한다")
	void getUsersFeeds_has_content() {
		// given

		Feed feed = Feed.builder()
			.user(testUser)
			.post(testPost)
			.build();

		Pageable pageable = PageRequest.of(0, 10);

		BDDMockito.given(feedRepository.findFeedsByCommonFeed(pageable)).willReturn(
			List.of(feed)
		);

		// when

		List<FeedResponse> usersFeeds = feedService.getUsersFeeds(pageable);

		// then
		assertThat(usersFeeds).isNotEmpty();
		assertThat(usersFeeds.size()).isEqualTo(1);
		FeedResponse feedResponse = usersFeeds.get(0);
		assertThat(feedResponse).isNotNull();
		assertThat(feedResponse.getContent()).isNotNull();
		assertThat(feedResponse.getContent().getContent()).isEqualTo(testPost.getContent());
	}

	@Test
	@DisplayName("피드 컨텐츠를 조회 했을 때 데이터가 존재하지 않는다면 빈 리스트를 반환해야한다")
	void getUsersFeeds_no_content() {
		// given
		Pageable pageable = PageRequest.of(0, 10);

		BDDMockito.given(feedRepository.findFeedsByCommonFeed(pageable)).willReturn(List.of());
		// when
		List<FeedResponse> usersFeeds = feedService.getUsersFeeds(pageable);
		// then
		assertThat(usersFeeds).isEmpty();
	}

}