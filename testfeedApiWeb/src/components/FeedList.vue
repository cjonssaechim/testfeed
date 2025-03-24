<template>
  <div class="feed-container">
    <h2>📢 피드 목록</h2>

    <div class="button-group">
      <button @click="fetchFeeds">📄 일반 피드 조회</button>
    </div>

    <p v-if="loading">⏳ 데이터를 불러오는 중...</p>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

    <!-- 피드 리스트 -->
    <div v-if="!loading && feeds.length > 0" class="feed-list">
      <div v-for="feed in feeds" :key="feed.seq" class="feed-card">
        <div class="feed-header">
          <div class="author-info">
            <img
                v-if="feed.author.profile"
                :src="`${feed.author.profile}`"
                alt="작성자 프로필"
                class="author-profile"
            />
            <span class="author-name">{{ feed.author.mbrName }}</span>
          </div>
          <span class="category">{{ feed.content.category }} ({{ feed.content.categoryId }})</span>
        </div>

        <!-- 이미지 슬라이드 -->
        <div v-if="feed.content.images.length > 0" class="image-slider">
          <div class="image-slide" v-for="(image, index) in feed.content.images" :key="index">
            <img :src="image" alt="피드 이미지" class="feed-image" />
          </div>
        </div>

        <h3 class="feed-title">{{ feed.content.title }}</h3>
        <p class="feed-content">{{ feed.content.body }}</p>

        <div class="meta-info">
          <span>👍 좋아요: {{ feed.stats.like }}</span>
          <span>👀 조회수: {{ feed.stats.view }}</span>
          <span>📅 작성일: {{ formatDate(feed.meta.createdAt) }}</span>
        </div>

        <!-- more 버튼 처리 -->
        <div v-if="feed.content.more">
          <a :href="formattedLink(feed.content.more.link.action)" target="_blank" class="more-link">{{ feed.content.more.title }}</a>
        </div>

        <!-- 위치 정보 처리 -->
        <div v-if="feed.content.location">
          <p class="location">
            📍 <strong>위치:</strong> {{ feed.content.location.address }}<br>
            <strong>위도:</strong> {{ feed.content.location.latitude }}, <strong>경도:</strong> {{ feed.content.location.longitude }}
          </p>
        </div>
      </div>
    </div>

    <!-- 더보기 버튼 -->
    <div v-if="nextCursor" class="more-button">
      <button @click="loadMoreFeeds">더보기</button>
    </div>

    <p v-else-if="!loading && feeds.length === 0">❌ 불러온 피드가 없습니다.</p>
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
      // http:// 또는 https://가 없으면 https://를 붙여서 반환
      return /^https?:\/\//i.test(link) ? link : 'https://' + link;
    }
  },
  mounted() {
    this.fetchFeeds();
  },
};
</script>

<style>
.feed-container {
  width: 600px;
  margin: auto;
  text-align: center;
}

.button-group {
  margin-bottom: 15px;
}

.feed-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.feed-card {
  border: 1px solid #ddd;
  padding: 15px;
  border-radius: 8px;
  text-align: left;
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
}

.author-profile {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 8px;
}

.author-name {
  font-size: 14px;
  font-weight: bold;
}

.category {
  font-size: 12px;
  color: darkred;
}

.image-slider {
  display: flex;
  overflow-x: scroll;
  gap: 10px;
  margin-bottom: 10px;
}

.image-slide {
  flex: 0 0 auto;
}

.feed-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
}

.feed-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
  color: aqua;
}

.feed-content {
  font-size: 14px;
  margin-bottom: 10px;
  color: olive;
}

.meta-info {
  font-size: 14px;
  color: gray;
  display: flex;
  justify-content: space-between;
}

.error {
  color: red;
}

.more-button {
  margin-top: 20px;
}

.more-link {
  font-size: 14px;
  color: blue;
  text-decoration: underline;
}

.location {
  font-size: 14px;
  color: darkcyan;
}

strong {
  font-weight: bold;
}
</style>