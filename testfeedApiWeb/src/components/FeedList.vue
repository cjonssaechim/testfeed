<template>
  <div class="feed-container">
    <h2>📢 피드 목록</h2>

    <!-- 사용자 입력 필드 추가 -->
    <div class="input-group">
      <label>시작 인덱스: </label>
      <input v-model="startInput" type="number" min="0" placeholder="0" />
      <label>가져올 피드 수: </label>
      <input v-model="sizeInput" type="number" min="1" placeholder="10" />
    </div>

    <div class="button-group">
      <button @click="fetchFeeds">📄 일반 피드 조회</button>
      <button @click="fetchHotFeeds">🔥 인기 피드 조회</button>
    </div>

    <p v-if="loading">⏳ 데이터를 불러오는 중...</p>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

    <!-- 피드 데이터가 있을 경우 -->
    <div v-if="!loading && feeds.length > 0" class="feed-list">
      <div v-for="feed in feeds" :key="feed.postId" class="feed-card">
        <div class="feed-header">
          <span class="post-type">{{ feed.author.userType }}</span>
          <span class="category">{{ feed.content.category }}</span>
        </div>
        <img
            v-if="feed.content.imageUrl"
            :src="feed.content.imageUrl"
            alt="피드 이미지"
            class="feed-image"
        />
        <h3 class="feed-title">{{ feed.content.title }}</h3>
        <p class="feed-content">{{ feed.content.content }}</p>
        <div class="meta-info">
          <span>👤 작성자: {{ feed.author?.username || "익명" }}</span>
          <span>👍 좋아요: {{ feed.content.like }}</span>
          <span>👀 조회수: {{ feed.content.views }}</span>
        </div>
      </div>
    </div>

    <!-- 데이터가 없을 경우 -->
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
      startInput: 0, // 사용자 입력값 저장 (기본값 0)
      sizeInput: 10, // 사용자 입력값 저장 (기본값 10)
    };
  },
  methods: {
    async fetchFeeds() {
      // 입력값 유효성 검사
      const start = parseInt(this.startInput);
      const size = parseInt(this.sizeInput);
      if (isNaN(start) || start < 0 || isNaN(size) || size < 1) {
        this.errorMessage = "❌ 유효한 start와 size를 입력해주세요.";
        return;
      }

      this.loading = true;
      this.errorMessage = "";
      this.feeds = [];
      try {
        const response = await axios.get("http://13.124.159.53/feeds", {
          params: { start: start, size: size }, // 동적 params 사용
          timeout: 5000,
        });
        if (response.data.resultCode === "001" && response.data.data) {
          this.feeds = response.data.data.map((feed) => ({
            ...feed,
            content: {
              ...feed.content,
              imageUrl: feed.content.imageUrl
                  ? `http://13.124.159.53${feed.content.imageUrl}`
                  : "",
            },
          }));
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
      // 입력값 유효성 검사
      const start = parseInt(this.startInput);
      const size = parseInt(this.sizeInput);
      if (isNaN(start) || start < 0 || isNaN(size) || size < 1) {
        this.errorMessage = "❌ 유효한 start와 size를 입력해주세요.";
        return;
      }

      this.loading = true;
      this.errorMessage = "";
      this.feeds = [];
      try {
        const response = await axios.get("http://13.124.159.53/feeds/hot", {
          params: { start: start, size: size }, // 동적 params 사용
          timeout: 5000,
        });
        if (response.data.resultCode === "001" && response.data.data) {
          this.feeds = response.data.data.map((feed) => ({
            ...feed,
            content: {
              ...feed.content,
              imageUrl: feed.content.imageUrl
                  ? `http://13.124.159.53${feed.content.imageUrl}`
                  : "",
            },
          }));
        } else {
          this.errorMessage = "❌ 인기 피드를 불러오는 데 실패했습니다. 인기 피드는 매일 업데이트 되며 좋아요 순입니다";
        }
      } catch (error) {
        this.errorMessage = "❌ 인기 피드를 불러오는 데 실패했습니다. 인기 피드는 매일 업데이트 되며 좋아요 순입니다";
      } finally {
        this.loading = false;
      }
    },
  },
  mounted() {
    this.fetchFeeds(); // 초기 로드 시 기본값으로 조회
  },
};
</script>

<style>

.feed-container {
  width: 600px;
  margin: auto;
  text-align: center;
}

.input-group {
  margin-bottom: 15px;
}

.input-group label {
  margin-right: 5px;
}

.input-group input {
  width: 50px;
  margin-right: 10px;
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
  margin-bottom: 10px;
}

.post-type {
  font-size: 12px;
  color: olivedrab;
}

.category {
  font-size: 12px;
  color: darkred;
}

.feed-image {
  width: 100%;
  height: auto;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 10px;
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
</style>