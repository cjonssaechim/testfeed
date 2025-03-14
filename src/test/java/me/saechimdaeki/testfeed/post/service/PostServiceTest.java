package me.saechimdaeki.testfeed.post.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import me.saechimdaeki.testfeed.feed.dto.FeedEvent;
import me.saechimdaeki.testfeed.post.domain.Category;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.domain.PostType;
import me.saechimdaeki.testfeed.post.service.port.PostRepository;
import me.saechimdaeki.testfeed.post.service.request.PostUpdateRequest;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.domain.UserType;
import me.saechimdaeki.testfeed.user.service.port.UserRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	String testTitle = "title";
	String testContent = "content";
	String testPostType = "biz";
	String testUsername = "username";
	String testCategory = "etc";
	Set<String> testUrls = Set.of("www.google.com", "www.youtube.com");
	@Mock
	private PostRepository postRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private KafkaTemplate<String, FeedEvent> kafkaTemplate;
	@InjectMocks
	private PostService postService;

	@Test
	@DisplayName("포스트 업데이트 요청시 기존 포스트가 존재한다면 수정되어야 한다")
	void updatePost_Success() {
		// given
		User user = new User(testUsername, UserType.MEMBER);
		Post post = new Post(testTitle, testContent, null, user, 0L, null, 0L, 0L, PostType.EVENT, Category.ETC,
			testUrls,null, null, null, null);

		String updateTitle = "updateTitle";
		String updateContent = "updateContent";
		String updatePostType = "ad";
		String updateImageUrl = "updated.jpg";
		Set<String> updateUrls = Set.of("www.updatedGoogle.com", "www.updatedYoutube.com");

		PostUpdateRequest postUpdateRequest = new PostUpdateRequest(updateTitle, updateContent, updateImageUrl, null,
			updatePostType, null, updateUrls,null, null, null);

		BDDMockito.given(postRepository.findById(1L)).willReturn(Optional.of(post));
		// when
		postService.updatePost(postUpdateRequest, 1L);
		// then
		assertThat(post.getTitle()).isEqualTo(updateTitle);
		assertThat(post.getContent()).isEqualTo(updateContent);
		assertThat(post.getImageUrl()).isEqualTo(updateImageUrl);
		assertThat(post.getPostType().name()).isEqualToIgnoringCase(updatePostType);
		assertThat(post.getUrls().size()).isEqualTo(updateUrls.size());
		assertThat(post.getUrls()).containsAll(updateUrls);
	}

	@Test
	@DisplayName("존재하지 않는 포스트를 삭제하려고 할 시 예외를 발생시켜야 한다")
	void deletePost_NotFound() {
		// given
		Long postId = 1L;
		BDDMockito.given(postRepository.findPostByPostId(postId)).willThrow(new RuntimeException("post not found"));
		// when then
		assertThatThrownBy(() -> postService.deletePost(postId, testUsername))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("post not found");
	}

	@BeforeEach
	void setUp() {
		TransactionSynchronizationManager.initSynchronization();
	}

	@AfterEach
	void tearDown() {
		TransactionSynchronizationManager.clearSynchronization();
	}
}