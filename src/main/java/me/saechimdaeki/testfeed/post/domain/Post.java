package me.saechimdaeki.testfeed.post.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.BatchSize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
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

	@Column(name = "post_title")
	private String title;

	private String body;

	@ElementCollection
	@CollectionTable(
		name = "images",
		joinColumns = @JoinColumn(name = "post_id")
	)
	private List<String> images;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User author;

	private String couponCode;

	@Enumerated(EnumType.STRING)
	private Category category;

	private boolean visibility = true;

	@Enumerated(EnumType.STRING)
	private PostType postType;

	@Embedded
	private Location location;

	@Embedded
	private MoreInfo more;

	private String url;

	// TODO like count를 계산 할 때에는 redis
	@BatchSize(size = 1000)
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PostLike> likes = new HashSet<>();
	private Long share;
	private Long views;

	private String flag;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;


	@Builder
	public Post(String title, String body, List<String> images, User author, String couponCode, Category category,
		PostType postType, Location location, MoreInfo more, String url,
		Long share, Long views, String flag, LocalDateTime fromDate, LocalDateTime toDate) {
		this.title = title;
		this.body = body;
		this.images = images;
		this.author = author;
		this.couponCode = couponCode;
		this.category = category == null ? Category.ETC : category;
		this.postType = postType == null ? PostType.NORMAL : postType;
		this.location = location;
		this.more = more;
		this.url = url;
		this.share = share;
		this.views = views;
		this.flag = flag;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public static Post create(
		String title, String body, List<String> images, User author, String couponCode, String category,
		String postType, Location location, MoreInfo more, String url,
		String flag, long from, long to
	) {
		return Post.builder()
			.author(author)
			.title(title)
			.body(body)
			.images(images)
			.couponCode(couponCode)
			.category(Category.fromString(category))
			.postType(PostType.fromString(postType))
			.location(location)
			.more(more)
			.url(url)
			.share(0L)
			.views(0L)
			.flag(flag)
			.fromDate(from == 0 ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(from), ZoneId.of("Asia/Seoul")))
			.toDate(to == 0 ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(to), ZoneId.of("Asia/Seoul")))
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

		if (StringUtils.hasText(postUpdateRequest.getBody())) {
			this.body = postUpdateRequest.getBody();
		}

		if (!CollectionUtils.isEmpty(postUpdateRequest.getImages()))
			this.images = postUpdateRequest.getImages();


		if (StringUtils.hasText(postUpdateRequest.getCouponCode())) {
			this.couponCode = postUpdateRequest.getCouponCode();
		}

		if (StringUtils.hasText(postUpdateRequest.getPostType())) {
			this.postType = PostType.fromString(postUpdateRequest.getPostType());
		}

		if (null != postUpdateRequest.getVisibility()) {
			this.visibility = postUpdateRequest.getVisibility();
		}

		if (StringUtils.hasText(postUpdateRequest.getUrl())) {
			this.url = postUpdateRequest.getUrl();
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

		if (null != postUpdateRequest.getLocation()) {
			this.location = postUpdateRequest.getLocation();
		}

		if (null != postUpdateRequest.getMore()) {
			this.more = postUpdateRequest.getMore();
		}
	}

	public void registerAuthor(User author) {
		this.author = author;
		author.addPost(this);
	}

}
