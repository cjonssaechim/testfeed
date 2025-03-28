package me.saechimdaeki.testfeed.post.controller;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.saechimdaeki.testfeed.annotation.IntegrationTest;
import me.saechimdaeki.testfeed.common.exception.ErrorCode;
import me.saechimdaeki.testfeed.post.domain.Category;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.domain.PostType;
import me.saechimdaeki.testfeed.post.infrastructure.PostJpaRepository;
import me.saechimdaeki.testfeed.post.service.request.PostCreateRequest;
import me.saechimdaeki.testfeed.post.service.request.PostUpdateRequest;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.domain.UserType;
import me.saechimdaeki.testfeed.user.infrastructure.UserJpaRepository;

@AutoConfigureMockMvc
@IntegrationTest
class PostControllerTest {

	@Autowired
	private UserJpaRepository userRepository;

	@Autowired
	private PostJpaRepository postRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	private final String mbrName = "testMbrName";

	private final String nickName = "testNickName";

	private final UserType userType = UserType.ADMIN;

	private final String title = "title";
	private final String body = "body";
	private final String postType = "ad";
	private User user;

	@Test
	@DisplayName("이미 회원가입 한 유저가 글을 쓴다면 정상적으로 등록되어야 한다")
	void createPostTest() throws Exception {

		// given

		PostCreateRequest postCreateRequest = new PostCreateRequest(title, body, null, postType, null, mbrName, null,
			null, null, 0, 0, null, null);

		String jsonString = objectMapper.writeValueAsString(postCreateRequest);

		MockPart postPart = new MockPart("post", jsonString.getBytes());
		postPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		// when then
		mockMvc.perform(multipart("/posts")
				.part(postPart))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.resultCode").value("001"))
			.andExpect(jsonPath("$.data.title").value(title))
			.andExpect(jsonPath("$.data.postType").value(PostType.fromString(postType).name()))
			.andExpect(jsonPath("$.data.body").value(body));
	}

	@Test
	@DisplayName("검증되지 않은 유저가 글을 작성시 에러를 발생시킨다")
	void createPostTest_With_UnAuthenticatedUser() throws Exception {

		// given
		String unAuthenticatedMbrName = "unAuthenticatedUserName";

		PostCreateRequest postCreateRequest = new PostCreateRequest(title, body, null, postType, null,
			unAuthenticatedMbrName, null,
			null, null, 0, 0, null, null);

		String jsonString = objectMapper.writeValueAsString(postCreateRequest);

		MockPart postPart = new MockPart("post", jsonString.getBytes());
		postPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		// when then
		mockMvc.perform(multipart("/posts")
				.part(postPart))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()))
			.andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
	}

	@Test
	@DisplayName("이미 저장된 게시글은 해당 게시글을 작성한 유저나 어드민이라면 게시글을 수정할 수 있다")
	void updatePostTest() throws Exception {

		// given
		String originCategory = "invest";
		String changedTitle = "changedTitle";
		String changedBody = "changedBody";
		String changedCategory = "fashion";
		Post post = saveTestPost(originCategory);

		PostUpdateRequest postUpdateRequest = new PostUpdateRequest(changedTitle, changedBody, null, null, null,
			changedCategory,
			null, null, null, null, null, null, null);
		// when
		mockMvc.perform(put("/posts/{postId}", post.getId())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(objectMapper.writeValueAsString(postUpdateRequest)));

		// then

		Post findedPost = postRepository.findById(post.getId()).orElseThrow();

		assertThat(findedPost.getTitle()).isEqualTo(changedTitle);
		assertThat(findedPost.getBody()).isEqualTo(changedBody);
		assertThat(findedPost.getPostType()).isEqualTo(PostType.fromString(postType));
		assertThat(findedPost.getCategory()).isEqualTo(Category.fromString(changedCategory));
	}

	@Test
	@DisplayName("작성된 게시글은 게시글을 작성한 유저가 삭제할 수 있다")
	void deletePostTest_With_PostUser() throws Exception {
		// given
		String category = "culture";
		Post savedPost = saveTestPost(category);

		// when

		mockMvc.perform(delete("/posts/{postId}", savedPost.getId())
				.param("username", mbrName))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.resultCode").value("003"))
			.andExpect(jsonPath("$.data").doesNotExist());

		// then

		Optional<Post> findPost = postRepository.findById(savedPost.getId());
		assertThat(findPost.isPresent()).isFalse();

	}

	@Test
	@DisplayName("존재하는 게시글의 id라면 게시글 조회시 조회수가 올라야 한다")
	void readPostTest() throws Exception {
		// given
		Post savedPost = saveTestPost("invest");

		// when

		mockMvc.perform(get("/posts/{postId}", savedPost.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.resultCode").value("001"))
			.andExpect(jsonPath("$.data.view").value(greaterThan(0)));

		// then

		Post findedPost = postRepository.findById(savedPost.getId()).orElseThrow();
		assertThat(findedPost.getId()).isEqualTo(savedPost.getId());
		assertThat(findedPost.getTitle()).isEqualTo(savedPost.getTitle());

	}

	@Test
	@DisplayName("존재하지 않는 게시글에 대한 read요청이라면 404 NOT FOUND와 커스텀 예외를 발생시킨다.")
	void readPostTest_non_exist_post() throws Exception {
		// given
		// when then
		mockMvc.perform(get("/posts/{postId}", 1L ))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value(ErrorCode.POST_NOT_FOUND.getCode()))
			.andExpect(jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));

	}

	@Test
	@DisplayName("게시글 공유하기 성공시 공유할 게시글의 url을 전달받아야 한다")
	void sharePostTest() throws Exception {
		// given
		Post savedPost = saveTestPost("fashion");
		// when then
		mockMvc.perform(get("/posts/{postId}/share", savedPost.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.resultCode").value("001"))
			.andExpect(jsonPath("$.data").value("/posts/" + savedPost.getId()));
	}

	@Test
	@DisplayName("존재하지 않는 게시글 공유시 404 not found 커스텀 예외를 발생시켜야 한다")
	void sharePostTest_non_exist_post() throws Exception {
		// given
		// when then
		mockMvc.perform(get("/posts/{postId}/share",1L))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value(ErrorCode.POST_NOT_FOUND.getCode()))
			.andExpect(jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));
	}

	private Post saveTestPost(String category) {
		Post post = new Post(title, body, null, user, null, Category.fromString(category),
			PostType.fromString(postType),
			null, null, null, 0L, 0L, null, null, null);

		return postRepository.save(post);
	}

	@BeforeEach
	void setUpUser() {
		user = new User(mbrName, userType, nickName);
		userRepository.save(user);
	}

	@AfterEach
	void deleteAllData() {
		userRepository.deleteAll();
		postRepository.deleteAll();
	}

}