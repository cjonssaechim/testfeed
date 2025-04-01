package me.saechimdaeki.testfeed.feed.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import me.saechimdaeki.testfeed.feed.service.FeedService;
import me.saechimdaeki.testfeed.feed.service.response.FeedVo;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.domain.UserType;

@WebMvcTest(FeedController.class)
class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeedService feedService;

    @MockitoBean
    JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    @DisplayName("피드 조회 API 테스트 - 성공")
    void getFeedsByCategory_Success() throws Exception {
        // given

        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
            .mbrName("testMbrName")
            .nickName("testNickName")
            .userType(UserType.MEMBER)
            .build();

        user.setCreatedAt(now);

        Post post = Post.builder()
            .author(user)
            .title("Test Title")
            .body("Test Content")
            .build();

        post.setCreatedAt(now);

        FeedVo feedVo = FeedVo.from(post, 10L, 5L, 3L);
        List<FeedVo> feedVos = List.of(feedVo);

        when(feedService.getUsersFeeds(any())).thenReturn(feedVos);

        // when & then
        mockMvc.perform(get("/feeds"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultCode").value("001"))
            .andExpect(jsonPath("$.data.feeds[0].author.nickName").value("testNickName"))
            .andExpect(jsonPath("$.data.feeds[0].stats.view").value(10))
            .andExpect(jsonPath("$.data.feeds[0].stats.like").value(5))
            .andExpect(jsonPath("$.data.feeds[0].stats.share").value(3));
    }

    @Test
    @DisplayName("피드 조회 API 테스트 - nextCursor 포함")
    void getFeedsByCategory_WithNextCursor() throws Exception {
        // given

        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
            .mbrName("testMbrName")
            .nickName("testNickName")
            .userType(UserType.MEMBER)
            .build();

        user.setCreatedAt(now);

        Post post = Post.builder()
            .author(user)
            .title("Test Title")
            .body("Test Content")
            .build();

        post.setCreatedAt(now);

        FeedVo feedVo = FeedVo.from(post, 10L, 5L, 3L);
        List<FeedVo> feedVos = List.of(feedVo);

        when(feedService.getUsersFeeds("202503281058")).thenReturn(feedVos);

        // when & then
        mockMvc.perform(get("/feeds")
                .param("nextCursor", "202503281058"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultCode").value("001"))
            .andExpect(jsonPath("$.data.feeds[0].author.nickName").value("testNickName"));
    }
}
