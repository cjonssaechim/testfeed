package me.saechimdaeki.testfeed.post.event.dto;

import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.user.domain.User;

public record PostCreatedEvent(
    Post post, User user
) {}