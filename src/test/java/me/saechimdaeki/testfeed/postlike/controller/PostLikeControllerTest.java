package me.saechimdaeki.testfeed.postlike.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import me.saechimdaeki.testfeed.annotation.IntegrationTest;
import me.saechimdaeki.testfeed.common.exception.ErrorCode;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.infrastructure.PostJpaRepository;
import me.saechimdaeki.testfeed.postlike.domain.PostLike;
import me.saechimdaeki.testfeed.postlike.infrastructure.PostLikeJpaRepository;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.domain.UserType;
import me.saechimdaeki.testfeed.user.infrastructure.UserJpaRepository;

@IntegrationTest
@AutoConfigureMockMvc
@Transactional
class PostLikeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PostJpaRepository postJpaRepository;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Autowired
	private PostLikeJpaRepository postLikeJpaRepository;

	private final String mbrName = "testMbrName";
	private final String userType = "member";
	private final String nickName = "testNickname";
	private final String title = "Test title";
	private final String body = "Test body";


	@Test
	@DisplayName("좋아요 API 테스트 성공시 해당 post에 좋아요가 추가된다")
	void postLikeApiTest() throws Exception {
		// given
		User user = new User(mbrName, UserType.fromString(userType), nickName);
		userJpaRepository.save(user);
		Post post = Post.builder()
			.author(user)
			.title(title)
			.body(body)
			.build();
		postJpaRepository.save(post);

		// when

		mockMvc.perform(post("/post-likes/{postId}",post.getId())
			.param("mbrName", mbrName))
			.andExpect(status().isOk())
			.andExpect(jsonPath("resultCode").value("001"));
		// then

		Post findedPost = postJpaRepository.findPostByPostId(post.getId()).orElseThrow();
		assertThat(findedPost.getTitle()).isEqualTo(title);
		assertThat(findedPost.getBody()).isEqualTo(body);
		assertThat(findedPost.getLikes()).isNotEmpty();
		assertThat(findedPost.getLikes().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("좋아요 API 테스트시 존재하지 않는 postId를 요청하면 예외가 발생한다")
	void likePostApiTest_with_non_existPostId() throws Exception {
		// given
		Long postId = 31L;
		// when  then

		mockMvc.perform(post("/post-likes/{postId}", postId)
				.param("mbrName", mbrName))
			.andExpect(status().isNotFound())
			.andDo(print())
			.andExpect(jsonPath("status").value(404))
			.andExpect(jsonPath("name").value(ErrorCode.POST_NOT_FOUND.name()))
			.andExpect(jsonPath("message").value(ErrorCode.POST_NOT_FOUND.getMessage()));
	}

	@Test
	@DisplayName("좋아요취소 API를 누를 시 좋아요가 취소된다")
	void postLikeCancelApiTest() throws Exception {
		// given
		User user = new User(mbrName, UserType.fromString(userType), nickName);
		userJpaRepository.save(user);
		Post post = Post.builder()
			.author(user)
			.title(title)
			.body(body)
			.build();
		postJpaRepository.save(post);

		PostLike postLike = new PostLike(user, post);
		postLikeJpaRepository.save(postLike);

		// when

		mockMvc.perform(delete("/post-likes/{postId}", post.getId())
				.param("mbrName", mbrName))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("resultCode").value("001"));

		// then

		Optional<PostLike> findedPostLike = postLikeJpaRepository.findByUserAndPost(user, post);
		assertThat(findedPostLike).isEmpty();
		Post findedPost = postJpaRepository.findPostByPostId(post.getId()).orElseThrow();
		assertThat(findedPost.getTitle()).isEqualTo(title);
		assertThat(findedPost.getBody()).isEqualTo(body);
		assertThat(findedPost.getLikes()).isEmpty();
	}




	@AfterEach
	void clearAllData() {
		postLikeJpaRepository.deleteAll();
		postJpaRepository.deleteAll();
		userJpaRepository.deleteAll();
	}
}