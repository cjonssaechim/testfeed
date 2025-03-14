<template>
  <div class="feed-container">
    <h2>📢 피드 목록</h2>

    <div class="button-group">
      <button @click="fetchFeeds">📄 일반 피드 조회</button>
      <button @click="fetchHotFeeds">🔥 인기 피드 조회</button>
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

        <!-- 이미지와 flag -->
        <div class="image-container">
          <img
              v-if="feed.content.images.length > 0"
              :src="`${feed.content.images[0]}`"
              alt="피드 이미지"
              class="feed-image"
          />
          <div v-if="feed.content.flag" class="flag">{{ feed.content.flag }}</div>
        </div>

        <h3 class="feed-title">{{ feed.content.title }}</h3>
        <p class="feed-content">{{ feed.content.body }}</p>

        <div class="meta-info">
          <span>👍 좋아요: {{ feed.stats.like }}</span>
          <span>👀 조회수: {{ feed.stats.view }}</span>
          <span>📅 작성일: {{ formatDate(feed.meta.createdAt) }}</span>
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

    async fetchHotFeeds() {
      this.loading = true;
      this.errorMessage = "";
      this.feeds = [];
      this.nextCursor = null;

      try {
        const response = await axios.get("http://13.124.159.53/feeds/hot", { timeout: 5000 });
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

.image-container {
  position: relative;
}

.feed-image {
  width: 100%;
  height: auto;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 10px;
}

.flag {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 12px;
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
</style>