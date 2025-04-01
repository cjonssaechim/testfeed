package me.saechimdaeki.testfeed.postlike.infrastructure;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.infrastructure.PostJpaRepository;
import me.saechimdaeki.testfeed.postlike.domain.PostLike;
import me.saechimdaeki.testfeed.postlike.service.port.PostLikeRepository;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.domain.UserType;
import me.saechimdaeki.testfeed.user.infrastructure.UserJpaRepository;

@SpringBootTest
class PostLikeRepositoryImplTest {

	@Autowired
	private PostLikeJpaRepository postLikeJpaRepository;

	@Autowired
	private PostLikeRepository postLikeRepository;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Autowired
	private PostJpaRepository postJpaRepository;

	private String nickName = "testNickName";

	private String mbrName = "testMbrName";

	@Test
	@DisplayName("회원가입 된 유저가 게시글을 좋아요를 누르면 해당 데이터는 저장된다")
	void savePostLikeTest() {

		// given
		User user = User.builder()
			.mbrName(mbrName)
			.nickName(nickName)
			.userType(UserType.ADMIN)
			.build();

		userJpaRepository.save(user);

		Post post = Post.builder()
			.author(user)
			.build();
		postJpaRepository.save(post);

		PostLike postLike = PostLike.builder()
			.post(post)
			.user(user)
			.build();

		// when
		postLikeRepository.save(postLike);

		// then

		assertThat(postLike.getId()).isNotNull();
	}


	@AfterEach
	void afterEach() {
		postLikeJpaRepository.deleteAll();
		userJpaRepository.deleteAll();
		postJpaRepository.deleteAll();
	}


}