package me.saechimdaeki.testfeed.postlike.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import me.saechimdaeki.testfeed.common.util.RedisKeyConstants;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.exception.PostException;
import me.saechimdaeki.testfeed.post.service.port.PostRepository;
import me.saechimdaeki.testfeed.postlike.domain.PostLike;
import me.saechimdaeki.testfeed.postlike.exception.PostLikeException;
import me.saechimdaeki.testfeed.postlike.service.port.PostLikeRepository;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.exception.UserException;
import me.saechimdaeki.testfeed.user.service.port.UserRepository;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceTest {

	@Mock
	private PostLikeRepository postLikeRepository;
	@Mock
	private PostRepository postRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private RedisTemplate<String, Long> redisTemplate;

	@Mock
	private ValueOperations<String, Long> valueOperations;

	@InjectMocks
	private PostLikeService postLikeService;

	@Nested
	@DisplayName("likePost 테스트")
	class LikePostTest {

		@Test
		@DisplayName("성공적으로 좋아요를 누를 수 있다")
		void likePostSuccess() {
			// given
			Long postId = 100L;
			String mbrName = "testUser";

			Post mockPost = Post.builder().build();
			User mockUser = User.builder().build();

			given(redisTemplate.opsForValue()).willReturn(valueOperations);
			given(postRepository.findById(postId)).willReturn(Optional.of(mockPost));
			given(userRepository.findByMbrName(mbrName)).willReturn(Optional.of(mockUser));
			given(postLikeRepository.findByUserAndPost(mockUser, mockPost))
				.willReturn(Optional.empty());

			given(valueOperations.increment(RedisKeyConstants.generatePostLikesKey(postId)))
				.willReturn(1L);

			// when
			postLikeService.likePost(postId, mbrName);

			// then
			verify(postLikeRepository, times(1)).save(any(PostLike.class));
			verify(valueOperations, times(1))
				.increment(RedisKeyConstants.generatePostLikesKey(postId));
		}

		@Test
		@DisplayName("존재하지 않는 포스트일 경우 예외가 발생한다")
		void likePost_whenPostNotFound_throwsException() {
			// given
			Long postId = 999L;
			String mbrName = "testUser";
			given(postRepository.findById(postId)).willReturn(Optional.empty());

			// when & then
			assertThrows(PostException.class,
				() -> postLikeService.likePost(postId, mbrName));
		}

		@Test
		@DisplayName("존재하지 않는 유저일 경우 예외가 발생한다")
		void likePost_whenUserNotFound_throwsException() {
			// given
			Long postId = 100L;
			String mbrName = "nonExistentUser";

			Post mockPost = Post.builder().build();
			given(postRepository.findById(postId)).willReturn(Optional.of(mockPost));
			given(userRepository.findByMbrName(mbrName)).willReturn(Optional.empty());

			// when & then
			assertThrows(UserException.class,
				() -> postLikeService.likePost(postId, mbrName));
		}

		@Test
		@DisplayName("이미 좋아요 누른 상태면 예외가 발생한다")
		void likePost_whenAlreadyLiked_throwsException() {
			// given
			Long postId = 100L;
			String mbrName = "testUser";

			Post mockPost = Post.builder().build();
			User mockUser = User.builder().build();

			PostLike existingPostLike = PostLike.builder()
				.post(mockPost)
				.user(mockUser)
				.build();

			given(postRepository.findById(postId)).willReturn(Optional.of(mockPost));
			given(userRepository.findByMbrName(mbrName)).willReturn(Optional.of(mockUser));
			given(postLikeRepository.findByUserAndPost(mockUser, mockPost))
				.willReturn(Optional.of(existingPostLike));

			// when & then
			assertThrows(PostLikeException.class,
				() -> postLikeService.likePost(postId, mbrName));
		}
	}

	@Nested
	@DisplayName("unlikePost 테스트")
	class UnlikePostTest {

		@Test
		@DisplayName("좋아요를 취소한다")
		void unlikePostSuccess() {
			// given
			Long postId = 200L;
			String mbrName = "anotherUser";

			Post mockPost = Post.builder().build();
			User mockUser = User.builder().build();
			PostLike mockPostLike = PostLike.builder()
				.user(mockUser)
				.post(mockPost)
				.build();
			given(redisTemplate.opsForValue()).willReturn(valueOperations);
			given(postRepository.findById(postId)).willReturn(Optional.of(mockPost));
			given(userRepository.findByMbrName(mbrName)).willReturn(Optional.of(mockUser));
			given(postLikeRepository.findByUserAndPost(mockUser, mockPost))
				.willReturn(Optional.of(mockPostLike));

			// Stub decrement(...) on the valueOperations mock
			given(valueOperations.decrement(RedisKeyConstants.generatePostLikesKey(postId)))
				.willReturn(0L);

			// when
			postLikeService.unlikePost(postId, mbrName);

			// then
			verify(postLikeRepository, times(1)).delete(mockPostLike);
			verify(valueOperations, times(1))
				.decrement(RedisKeyConstants.generatePostLikesKey(postId));
		}

		@Test
		@DisplayName("포스트가 존재하지 않으면 예외가 발생한다")
		void unlikePost_whenPostNotFound_throwsException() {
			// given
			Long postId = 999L;
			String mbrName = "testUser";
			given(postRepository.findById(postId)).willReturn(Optional.empty());

			// when & then
			assertThrows(PostException.class,
				() -> postLikeService.unlikePost(postId, mbrName));
		}

		@Test
		@DisplayName("유저가 존재하지 않으면 예외가 발생한다")
		void unlikePost_whenUserNotFound_throwsException() {
			// given
			Long postId = 100L;
			String mbrName = "invalidUser";

			Post mockPost = Post.builder().build();
			given(postRepository.findById(postId)).willReturn(Optional.of(mockPost));
			given(userRepository.findByMbrName(mbrName)).willReturn(Optional.empty());

			// when & then
			assertThrows(UserException.class,
				() -> postLikeService.unlikePost(postId, mbrName));
		}

		@Test
		@DisplayName("좋아요를 누르지 않았다면 (PostLike가 없다면) 예외가 발생한다")
		void unlikePost_whenPostLikeNotFound_throwsException() {
			// given
			Long postId = 100L;
			String mbrName = "testUser";

			Post mockPost = Post.builder().build();
			User mockUser = User.builder().build();

			given(postRepository.findById(postId)).willReturn(Optional.of(mockPost));
			given(userRepository.findByMbrName(mbrName)).willReturn(Optional.of(mockUser));
			given(postLikeRepository.findByUserAndPost(mockUser, mockPost))
				.willReturn(Optional.empty());

			// when & then
			assertThrows(PostLikeException.class,
				() -> postLikeService.unlikePost(postId, mbrName));
		}
	}
}