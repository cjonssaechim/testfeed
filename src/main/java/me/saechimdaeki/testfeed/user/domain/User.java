package me.saechimdaeki.testfeed.user.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saechimdaeki.testfeed.common.domain.BaseEntity;
import me.saechimdaeki.testfeed.post.domain.Post;

/**
 * // TODO 전부 다 미지수임 어찌 해야 할 까
 * 여기는 사실 cj one oauth로 넘어올듯함 jwt라던지 뭐 정보가 넘어올 듯
 *
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	private String username; // unique(?)

	@Enumerated(EnumType.STRING)
	private UserType userType;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts = new ArrayList<>();

	@Builder
	public User(String username, UserType userType) {
		this.username = username;
		this.userType = userType;
	}

	public void addPost(Post post) {
		this.posts.add(post);
	}

	public void changeUserType(String userType) {
		this.userType = UserType.fromString(userType);
	}

}
