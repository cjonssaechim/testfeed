<template>
  <div class="feed-container">
    <h2>📢 피드 목록</h2>

    <div class="button-group">
      <button @click="openModal">📄 글쓰기</button>
    </div>

    <p v-if="loading">⏳ 데이터를 불러오는 중...</p>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

    <!-- 피드 목록 -->
    <div v-if="!loading && feeds.length > 0" class="feed-list">
      <div v-for="feed in feeds" :key="feed.seq" class="feed-card" :data-feed-seq="feed.seq">
        <div class="feed-header">
          <div class="author-info">
            <img
                v-if="feed.author.profile"
                :src="feed.author.profile"
                alt="작성자 프로필"
                class="author-profile"
            />
            <span class="author-name">{{ feed.author.mbrName }}</span>
          </div>
          <span class="category">{{ feed.content.category }}</span>
        </div>

        <!-- 이미지 슬라이더 (슬라이드 애니메이션 + 도트 인디케이터 추가) -->
        <div v-if="feed.content.images.length > 0" class="image-slider"
             @touchstart="onTouchStart($event, feed.seq)"
             @touchmove="onTouchMove"
             @touchend="onTouchEnd($event, feed.seq)"
             @mousedown="onMouseDown($event, feed.seq)"
             @mousemove="onMouseMove"
             @mouseup="onMouseUp($event, feed.seq)"
             @mouseleave="onMouseUp($event, feed.seq)">
          <div class="image-wrapper" :style="{ transform: `translateX(-${(currentImageIndex[feed.seq] || 0) * 100}%)` }">
            <img
                v-for="(image, index) in feed.content.images"
                :key="index"
                :src="image"
                alt="피드 이미지"
                class="feed-image"
            />
          </div>
          <!-- 도트 인디케이터 -->
          <div class="dots-container">
            <span
                v-for="(image, index) in feed.content.images"
                :key="index"
                class="dot"
                :class="{ active: (currentImageIndex[feed.seq] || 0) === index }"
                @click="goToImage(feed.seq, index)"
            ></span>
          </div>
        </div>

        <h3 class="feed-title">{{ feed.content.title }}</h3>
        <p class="feed-content">{{ feed.content.body }}</p>

        <div class="meta-info">
          <span>👍 좋아요: {{ feed.stats.like }}</span>
          <span>👀 조회수: {{ feed.stats.view }}</span>
          <span>📅 작성일: {{ formatDate(feed.meta.createdAt) }}</span>
        </div>

        <div v-if="feed.content.location">
          <p class="location">
            📍 <strong>위치:</strong> {{ feed.content.location.address }}<br />
            <strong>위도:</strong> {{ feed.content.location.latitude }},
            <strong>경도:</strong> {{ feed.content.location.longitude }}
          </p>
        </div>

        <!-- MoreInfo 표시 -->
        <div class="more-info">
          <a
              v-if="feed.content.more"
              :href="formattedLink(feed.content.more.link.action)"
              target="_blank"
              class="more-link"
          >
            {{ feed.content.more.title }}
          </a>
          <span v-else>More Info 없음</span>
        </div>
      </div>
    </div>

    <div v-if="nextCursor" class="more-button">
      <button @click="loadMoreFeeds">더보기</button>
    </div>

    <p v-else-if="!loading && feeds.length === 0">❌ 불러온 피드가 없습니다.</p>

    <!-- 글쓰기 모달 -->
    <div v-if="isModalOpen" class="modal-overlay">
      <div class="modal-content">
        <h3>글쓰기</h3>
        <form @submit.prevent="submitPost">
          <div>
            <h4>작성자</h4>
            <input v-model="newPost.author.mbrName" placeholder="작성자 이름" required />
          </div>

          <div>
            <h4>게시글 타입</h4>
            <select v-model="newPost.content.postType" required>
              <option v-for="type in postTypes" :key="type.value" :value="type.value">
                {{ type.label }}
              </option>
            </select>
          </div>

          <div>
            <h4>카테고리</h4>
            <select v-model="newPost.content.category" required>
              <option v-for="category in categories" :key="category.value" :value="category.value">
                {{ category.label }}
              </option>
            </select>
          </div>

          <div>
            <h4>피드 내용</h4>
            <input v-model="newPost.content.title" placeholder="제목" required />
            <textarea v-model="newPost.content.body" placeholder="내용" required></textarea>
            <input v-model="newPost.content.couponCode" placeholder="쿠폰 코드" />
            <input v-model="newPost.content.url" placeholder="URL" />
            <input v-model="newPost.content.flag" placeholder="플래그" />
            <input v-model="newPost.content.from" placeholder="시작 시간 (Unix timestamp)" type="number" />
            <input v-model="newPost.content.to" placeholder="종료 시간 (Unix timestamp)" type="number" />
          </div>

          <div>
            <h4>이미지들</h4>
            <input type="file" multiple @change="handleFileUpload" />
          </div>

          <div>
            <h4>위치</h4>
            <input v-model="newPost.content.location.address" placeholder="위치" />
            <input v-model="newPost.content.location.latitude" placeholder="위도" type="number" step="any" />
            <input v-model="newPost.content.location.longitude" placeholder="경도" type="number" step="any" />
          </div>

          <div>
            <h4>More Info</h4>
            <input v-model="newPost.content.more.title" placeholder="More Info 제목" />
            <input v-model="newPost.content.more.link.type" placeholder="링크 타입" />
            <input v-model="newPost.content.more.link.action" placeholder="링크 액션 (URL)" />
            <input v-model="newPost.content.more.link.target" placeholder="링크 타겟" />
          </div>

          <div>
            <button type="submit">제출</button>
            <button @click="closeModal" type="button">취소</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      feeds: [],
      loading: false,
      errorMessage: "",
      nextCursor: null,
      isModalOpen: false,
      selectedFiles: [],
      currentImageIndex: {},
      startX: 0, // 드래그 시작 위치
      currentX: 0, // 드래그 중 현재 위치
      isDragging: false, // 드래그 중인지 여부
      currentFeedSeq: null, // 현재 드래그 중인 피드의 seq
      postTypes: [
        { value: "AD", label: "광고 게시글" },
        { value: "EVENT", label: "이벤트 게시글" },
        { value: "NORMAL", label: "일반 게시글" },
        { value: "NOTICE", label: "공지 게시글" },
      ],
      categories: [
        { value: "FASHION", label: "패션/뷰티" },
        { value: "FOOD", label: "식품/외식" },
        { value: "LIFE", label: "생활/건강" },
        { value: "TRIP", label: "여행/레저" },
        { value: "CULTURE", label: "영화/공연/전시" },
        { value: "FURNITURE", label: "가구/잡화" },
        { value: "DIGITAL", label: "디지털/가전" },
        { value: "INVEST", label: "재테크" },
        { value: "EDU", label: "교육" },
        { value: "GAME", label: "게임/앱" },
        { value: "CAR", label: "자동차" },
        { value: "ETC", label: "기타" },
      ],
      newPost: {
        author: {
          mbrName: "saechimdaeki",
          profile: "",
        },
        content: {
          title: "",
          body: "",
          category: "ETC",
          couponCode: "",
          postType: "NORMAL",
          url: "",
          flag: "",
          from: "",
          to: "",
          images: [],
          location: {
            address: "",
            latitude: "",
            longitude: "",
          },
          more: {
            title: "",
            link: {
              type: "",
              action: "",
              target: "",
            },
          },
        },
        stats: {
          like: 0,
          view: 0,
        },
        meta: {
          createdAt: "",
        },
      },
    };
  },
  methods: {
    async fetchFeeds() {
      this.loading = true;
      this.errorMessage = "";
      this.feeds = [];
      this.nextCursor = null;

      try {
        const response = await axios.get("http://13.124.159.53/feeds", { timeout: 5000 });
        if (response.data.resultCode === "001" && response.data.data) {
          this.feeds = response.data.data.feeds;
          this.nextCursor = response.data.data.nextCursor;
        } else {
          this.errorMessage = "❌ 데이터를 불러오는 데 실패했습니다.";
        }
      } catch (error) {
        this.errorMessage = "❌ 데이터를 불러오는 데 실패했습니다.";
      } finally {
        this.loading = false;
      }
    },
    async loadMoreFeeds() {
      if (!this.nextCursor) return;

      this.loading = true;
      this.errorMessage = "";

      try {
        const response = await axios.get("http://13.124.159.53/feeds", {
          params: { nextCursor: this.nextCursor },
          timeout: 5000,
        });
        if (response.data.resultCode === "001" && response.data.data) {
          this.feeds = [...this.feeds, ...response.data.data.feeds];
          this.nextCursor = response.data.data.nextCursor;
        } else {
          this.errorMessage = "❌ 데이터를 불러오는 데 실패했습니다.";
        }
      } catch (error) {
        this.errorMessage = "❌ 데이터를 불러오는 데 실패했습니다.";
      } finally {
        this.loading = false;
      }
    },
    formatDate(dateStr) {
      return new Date(dateStr).toLocaleString();
    },
    formattedLink(link) {
      return /^https?:\/\//i.test(link) ? link : "https://" + link;
    },
    openModal() {
      this.isModalOpen = true;
    },
    closeModal() {
      this.isModalOpen = false;
      this.newPost = {
        author: {
          mbrName: "saechimdaeki",
          profile: "",
        },
        content: {
          title: "",
          body: "",
          category: "ETC",
          couponCode: "",
          postType: "NORMAL",
          url: "",
          flag: "",
          from: "",
          to: "",
          images: [],
          location: {
            address: "",
            latitude: "",
            longitude: "",
          },
          more: {
            title: "",
            link: {
              type: "",
              action: "",
              target: "",
            },
          },
        },
        stats: {
          like: 0,
          view: 0,
        },
        meta: {
          createdAt: "",
        },
      };
      this.selectedFiles = [];
    },
    handleFileUpload(event) {
      this.selectedFiles = Array.from(event.target.files);
    },
    async submitPost() {
      try {
        const formData = new FormData();

        const postCreateRequest = {
          title: this.newPost.content.title,
          body: this.newPost.content.body,
          couponCode: this.newPost.content.couponCode || "",
          postType: this.newPost.content.postType || "",
          category: this.newPost.content.category,
          mbrName: this.newPost.author.mbrName,
          url: this.newPost.content.url || "",
          flag: this.newPost.content.flag || "",
          from: this.newPost.content.from ? parseInt(this.newPost.content.from) : 0,
          to: this.newPost.content.to ? parseInt(this.newPost.content.to) : 0,
          location: {
            address: this.newPost.content.location.address || "",
            latitude: this.newPost.content.location.latitude
                ? parseFloat(this.newPost.content.location.latitude)
                : null,
            longitude: this.newPost.content.location.longitude
                ? parseFloat(this.newPost.content.location.longitude)
                : null,
          },
          more: this.newPost.content.more && this.newPost.content.more.title
              ? {
                title: this.newPost.content.more.title,
                link: {
                  type: this.newPost.content.more.link.type || "",
                  action: this.newPost.content.more.link.action || "",
                  target: this.newPost.content.more.link.target || "",
                },
              }
              : null,
        };

        formData.append("post", new Blob([JSON.stringify(postCreateRequest)], { type: "application/json" }));

        this.selectedFiles.forEach((file) => {
          formData.append("image", file);
        });

        const response = await axios.post("http://13.124.159.53/posts", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
          timeout: 5000,
        });

        if (response.data != null) {
          alert("게시글이 작성되었습니다!");
          this.closeModal();
          this.fetchFeeds();
        } else {
          alert("게시글 작성에 실패했습니다.");
        }
      } catch (error) {
        console.error("게시글 작성 오류:", error);
        alert("게시글 작성 중 오류가 발생했습니다.");
      }
    },
    // 이미지 슬라이더 관련 메서드
    prevImage(feedSeq) {
      const feed = this.feeds.find((f) => f.seq === feedSeq);
      if (feed && feed.content.images.length > 0) {
        this.currentImageIndex[feedSeq] = (this.currentImageIndex[feedSeq] || 0) - 1;
        if (this.currentImageIndex[feedSeq] < 0) {
          this.currentImageIndex[feedSeq] = feed.content.images.length - 1;
        }
      }
    },
    nextImage(feedSeq) {
      const feed = this.feeds.find((f) => f.seq === feedSeq);
      if (feed && feed.content.images.length > 0) {
        this.currentImageIndex[feedSeq] =
            ((this.currentImageIndex[feedSeq] || 0) + 1) % feed.content.images.length;
      }
    },
    goToImage(feedSeq, index) {
      this.currentImageIndex[feedSeq] = index;
    },
    // 드래그/스와이프 관련 메서드
    onTouchStart(event, feedSeq) {
      this.startX = event.touches[0].clientX;
      this.isDragging = true;
      this.currentFeedSeq = feedSeq;
    },
    onTouchMove(event) {
      if (!this.isDragging) return;
      this.currentX = event.touches[0].clientX;
    },
    onTouchEnd(event, feedSeq) {
      if (!this.isDragging) return;
      this.isDragging = false;

      const deltaX = this.currentX - this.startX;
      if (deltaX > 50) {
        this.prevImage(feedSeq);
      } else if (deltaX < -50) {
        this.nextImage(feedSeq);
      }
    },
    onMouseDown(event, feedSeq) {
      this.startX = event.clientX;
      this.isDragging = true;
      this.currentFeedSeq = feedSeq;
    },
    onMouseMove(event) {
      if (!this.isDragging) return;
      this.currentX = event.clientX;
    },
    onMouseUp(event, feedSeq) {
      if (!this.isDragging) return;
      this.isDragging = false;

      const deltaX = this.currentX - this.startX;
      if (deltaX > 50) {
        this.prevImage(feedSeq);
      } else if (deltaX < -50) {
        this.nextImage(feedSeq);
      }
    },
  },
  mounted() {
    this.fetchFeeds();
  },
};
</script>

<style scoped>
.feed-container {
  padding: 20px;
  background-color: #1a1a1a;
  color: #fff;
  min-height: 100vh;
}

.feed-container h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.button-group {
  margin-bottom: 20px;
}

.button-group button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.button-group button:hover {
  background-color: #0056b3;
}

.feed-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feed-card {
  background-color: #fff;
  color: #333;
  border-radius: 10px;
  padding: 15px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.feed-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.author-profile {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.author-name {
  font-weight: bold;
}

.category {
  font-size: 14px;
  color: #666;
}

.image-slider {
  position: relative;
  width: 100%;
  overflow: hidden;
  user-select: none; /* 드래그 중 텍스트 선택 방지 */
}

.image-wrapper {
  display: flex;
  transition: transform 0.3s ease; /* 슬라이드 애니메이션 */
}

.feed-image {
  width: 100%;
  flex: 0 0 100%; /* 각 이미지가 전체 너비를 차지 */
  height: auto;
  object-fit: cover;
  pointer-events: auto; /* 이미지에서 이벤트 감지 가능 */
}

/* 도트 인디케이터 스타일 */
.dots-container {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.dot {
  width: 8px;
  height: 8px;
  background-color: #bbb;
  border-radius: 50%;
  margin: 0 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.dot.active {
  background-color: #007bff;
}

.feed-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
}

.feed-content {
  font-size: 14px;
  margin-bottom: 10px;
}

.meta-info {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #666;
}

.location {
  font-size: 12px;
  color: #666;
  margin-top: 10px;
}

.more-info {
  margin-top: 10px;
  font-size: 14px;
  color: #007bff;
}

.more-link {
  text-decoration: none;
  color: #007bff;
}

.more-link:hover {
  text-decoration: underline;
}

.more-button {
  text-align: center;
  margin-top: 20px;
}

.more-button button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.more-button button:hover {
  background-color: #0056b3;
}

.error {
  color: #dc3545;
  text-align: center;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 10px;
  width: 450px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  max-height: 80vh;
  overflow-y: auto;
}

.modal-content h3 {
  font-size: 22px;
  margin-bottom: 20px;
  text-align: center;
  color: #333;
}

form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

form div {
  display: flex;
  flex-direction: column;
}

form h4 {
  font-size: 16px;
  color: #444;
  margin-bottom: 8px;
}

input,
textarea,
select {
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #ddd;
  margin-bottom: 10px;
  font-size: 14px;
}

textarea {
  resize: vertical;
  min-height: 100px;
}

button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  width: 100%;
  margin-top: 10px;
}

button:hover {
  background-color: #0056b3;
}

button[type="button"] {
  background-color: #dc3545;
}

button[type="button"]:hover {
  background-color: #c82333;
}
</style>