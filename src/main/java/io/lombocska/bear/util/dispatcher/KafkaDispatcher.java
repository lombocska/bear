package io.lombocska.bear.util.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lombocska.bear.common.BearDomainEvent;
import io.lombocska.bear.common.DomainEvent;
import io.lombocska.bear.util.exception.EventSerializationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional("kafkaTransactionManager")
public class KafkaDispatcher {

    @Value("${bear.kafka.domain-event-topic}")
    private String domainEventTopic;

    private KafkaTemplate kafkaTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public KafkaDispatcher(KafkaTemplate kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(BearDomainEvent event) {
        send(domainEventTopic, event);
    }

    private void send(String topicToSend, BearDomainEvent event) {
        kafkaTemplate.send(topicToSend, event.getAggregationId(), serializeDomainEvent(event));
        log.debug("DomainEvent sent: {} to the topic: {}", event, topicToSend);
    }

    private byte[] serializeDomainEvent(final DomainEvent<?> event) {
        try {
            return objectMapper.writeValueAsBytes(event);
        } catch (JsonProcessingException e) {
            throw new EventSerializationException("Error during domain event serialization! DomainEvent aggregateId: " + event.getAggregationId(), e);
        }
    }

}
