package com.api.instaclone.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String TOPIC_EXCHANGE = "topic.exchange";
    public static final String ACTION_QUEUE_NAME = "actions";
    public static final String COMMENT_QUEUE_NAME = "comments";
    public static final String FOLLOWER_QUEUE_NAME = "subscriptions";
    public static final String LOGS_QUEUE_NAME = "logs";
    public static final String NOTIFICATION_QUEUE_NAME = "notifications";
    public static final String NOTIFICATION_ACTION_QUEUE_NAME = "notification-action";
    public static final String NOTIFICATON_ACTION_ROUTNG_KEY = "clone.apricot.notifications";
    public static final String ACTION_ROUTING_KEY = "clone.apricot.actions.#";
    public static final String COMMENT_ROUTING_KEY = "clone.apricot.comments.#";
    public static final String LOGS_ROUTING_KEY = "clone.apricot.*";
    public static final String FOLLOWER_ROUTING_KEY = "clone.apricot.subscriptions.#";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Queue actionQueue() {
        return new Queue(ACTION_QUEUE_NAME);
    }

    @Bean
    public Queue commentsQueue() {
        return new Queue(COMMENT_QUEUE_NAME);
    }

    @Bean
    public Queue logsQueue() {
        return new Queue(LOGS_QUEUE_NAME);
    }

    @Bean
    public Queue followQueue() {
        return new Queue(FOLLOWER_QUEUE_NAME);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE_NAME);
    }

    @Bean
    public Queue notificationActionQueue() {
        // this queue receives and processes read write action on notification Queue.
        return new Queue(NOTIFICATION_ACTION_QUEUE_NAME);
    }

    @Bean
    public Binding bindingActions() {
        return BindingBuilder.bind(actionQueue()).to(topicExchange()).with(ACTION_ROUTING_KEY);
    }

    @Bean
    public Binding bindComments() {
        return BindingBuilder.bind(commentsQueue()).to(topicExchange()).with(COMMENT_ROUTING_KEY);
    }

    @Bean
    public Binding bindLogs() {
        return BindingBuilder.bind(logsQueue()).to(topicExchange()).with(LOGS_ROUTING_KEY);
    }

    @Bean
    public Binding bindFollows() {
        return BindingBuilder.bind(followQueue()).to(topicExchange()).with(FOLLOWER_ROUTING_KEY);
    }

    @Bean
    public Binding bindNotificationToActions() {
        return BindingBuilder.bind(notificationQueue()).to(topicExchange()).with(ACTION_ROUTING_KEY);
    }

    @Bean
    public Binding bindNotificationToComments() {
        return BindingBuilder.bind(notificationQueue()).to(topicExchange()).with(COMMENT_ROUTING_KEY);
    }

    @Bean
    public Binding bindNotificationToFollower() {
        return BindingBuilder.bind(notificationQueue()).to(topicExchange()).with(FOLLOWER_ROUTING_KEY);
    }

    @Bean
    public Binding bindNotificationActionsToNotificationActionQueue() {
        return BindingBuilder.bind(notificationActionQueue()).to(topicExchange()).with(NOTIFICATON_ACTION_ROUTNG_KEY);
    }
}
