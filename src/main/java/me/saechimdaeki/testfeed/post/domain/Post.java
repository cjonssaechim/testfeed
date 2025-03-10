package me.saechimdaeki.testfeed.post.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saechimdaeki.testfeed.common.domain.BaseEntity;
import me.saechimdaeki.testfeed.post.service.request.PostUpdateRequest;
import me.saechimdaeki.testfeed.postlike.domain.PostLike;
import me.saechimdaeki.testfeed.user.domain.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class Post extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	private String title;

	private String content;

	private String imageUrl; // TODO S3

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User author;

	private Long views;

	private String couponCode;

	private Long usefulCount;

	private Long disappointCount;

	@Enumerated(EnumType.STRING)
	private Category category;

	private boolean visibility = true;

	@Enumerated(EnumType.STRING)
	private PostType postType;

	@ElementCollection
	@CollectionTable(
		name = "urls",
		joinColumns = @JoinColumn(name = "post_id")
	)
	private Set<String> urls = new HashSet<>();

	// TODO redis 연계
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PostLike> likes = new HashSet<>();

	//TODO
	// 위치 첨부, 썸네일

	@Builder
	public Post(String title, String content, String imageUrl, User author, Long views, String couponCode,
		Long usefulCount,
		Long disappointCount,
		PostType postType,
		Category category,
		Set<String> urls) {
		this.title = title;
		this.content = content;
		this.imageUrl = imageUrl;
		this.author = author;
		this.views = views;
		this.couponCode = couponCode;
		this.usefulCount = usefulCount;
		this.disappointCount = disappointCount;
		this.postType = postType == null ? PostType.NORMAL : postType;
		this.category = category == null ? Category.ETC : category;
		this.urls = urls;
	}

	public static Post create(String title, String content, String imageUrl,
		User author, String couponCode, String postType, String category, Set<String> urls) {
		return Post.builder()
			.title(title)
			.content(content)
			.imageUrl(imageUrl)
			.author(author)
			.couponCode(couponCode)
			.views(0L)
			.usefulCount(0L)
			.disappointCount(0L)
			.postType(PostType.fromString(postType))
			.category(Category.fromString(category))
			.urls(urls)
			.build();
	}

	public void addLike(PostLike postLike) {
		this.likes.add(postLike);
	}

	public void removeLike(PostLike postLike) {
		this.likes.remove(postLike);
	}

	public void changeVisibility() {
		this.visibility = !this.visibility;
	}

	public void updateRequest(PostUpdateRequest postUpdateRequest) {
		if (StringUtils.hasText(postUpdateRequest.getTitle())) {
			this.title = postUpdateRequest.getTitle();
		}

		if (StringUtils.hasText(postUpdateRequest.getContent())) {
			this.content = postUpdateRequest.getContent();
		}

		if (StringUtils.hasText(postUpdateRequest.getImageUrl())) {
			this.imageUrl = postUpdateRequest.getImageUrl();
		}

		if (StringUtils.hasText(postUpdateRequest.getCouponCode())) {
			this.couponCode = postUpdateRequest.getCouponCode();
		}

		if (StringUtils.hasText(postUpdateRequest.getPostType())) {
			this.postType = PostType.fromString(postUpdateRequest.getPostType());
		}

		if (null != postUpdateRequest.getVisibility()) {
			this.visibility = postUpdateRequest.getVisibility();
		}

		if (!CollectionUtils.isEmpty(postUpdateRequest.getUrls())) {
			this.urls = postUpdateRequest.getUrls();
		}
	}

	public void registerAuthor(User author) {
		this.author = author;
		author.addPost(this);
	}
}
