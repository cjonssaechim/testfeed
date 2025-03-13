package me.saechimdaeki.testfeed.post.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.BatchSize;
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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saechimdaeki.testfeed.common.domain.BaseEntity;
import me.saechimdaeki.testfeed.post.service.request.PostUpdateRequest;
import me.saechimdaeki.testfeed.postlike.domain.PostLike;
import me.saechimdaeki.testfeed.user.domain.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
public class Post extends BaseEntity {

	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String content;

	private String imageUrl;

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
	@Column(name = "post_url")
	private Set<String> urls = new HashSet<>();

	// TODO redis 연계
	@BatchSize(size = 1000)
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PostLike> likes = new HashSet<>();

	// 추가해달라 하여 추가한 필드.
	private String flag;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private Long share;


	@Builder
	public Post(String title, String content, String imageUrl, User author, Long views, String couponCode,
		Long usefulCount, Long disappointCount, PostType postType, Category category,
		Set<String> urls, String flag, LocalDateTime fromDate, LocalDateTime toDate, Long share) {
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
		this.flag = flag;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.share = share;
	}

	public static Post create(String title, String content, String imageUrl, User author,
		String couponCode, String postType, String category,
		Set<String> urls, String flag, long from, long to) {
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
			.flag(flag)
			.fromDate(from == 0 ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(from), ZoneId.systemDefault()))
			.toDate(to == 0 ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(to), ZoneId.systemDefault()))
			.share(0L)
			.build();
	}

	public void addLike(PostLike postLike) {
		this.likes.add(postLike);
		postLike.setPost(this);
	}

	public void removeLike(PostLike postLike) {
		this.likes.remove(postLike);
		postLike.setPost(null);
		postLike.setUser(null);
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

		if (StringUtils.hasText(postUpdateRequest.getFlag())) {
			this.flag = postUpdateRequest.getFlag();
		}

		if (null != postUpdateRequest.getFrom()) {
			this.fromDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(postUpdateRequest.getFrom()),
				ZoneId.systemDefault());
		}

		if (null != postUpdateRequest.getTo()) {
			this.toDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(postUpdateRequest.getTo()), ZoneId.systemDefault());
		}
	}

	public void registerAuthor(User author) {
		this.author = author;
		author.addPost(this);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || getClass() != object.getClass())
			return false;
		Post post = (Post)object;
		return visibility == post.visibility && Objects.equals(id, post.id) && Objects.equals(title,
			post.title) && Objects.equals(content, post.content) && Objects.equals(imageUrl,
			post.imageUrl) && Objects.equals(author, post.author) && Objects.equals(views, post.views)
			&& Objects.equals(couponCode, post.couponCode) && Objects.equals(usefulCount,
			post.usefulCount) && Objects.equals(disappointCount, post.disappointCount)
			&& category == post.category && postType == post.postType && Objects.equals(urls, post.urls)
			&& Objects.equals(likes, post.likes) && Objects.equals(flag, post.flag)
			&& Objects.equals(fromDate, post.fromDate) && Objects.equals(toDate, post.toDate)
			&& Objects.equals(share, post.share);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, content, imageUrl, author, views, couponCode, usefulCount, disappointCount,
			category,
			visibility, postType, urls, likes, flag, fromDate, toDate, share);
	}
}
