package me.saechimdaeki.testfeed.post.event.listener;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.feed.event.dto.FeedEvent;
import me.saechimdaeki.testfeed.post.domain.Post;
import me.saechimdaeki.testfeed.post.event.dto.PostCreatedEvent;
import me.saechimdaeki.testfeed.user.domain.User;

@Component
@RequiredArgsConstructor
public class PostCreatedEventListener {

    private final KafkaTemplate<String, FeedEvent> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreated(PostCreatedEvent event) {
        Post post = event.post();
        User user = event.user();

        FeedEvent feedEvent = FeedEvent.from(post, user);
        kafkaTemplate.send("feed", feedEvent);
    }
}