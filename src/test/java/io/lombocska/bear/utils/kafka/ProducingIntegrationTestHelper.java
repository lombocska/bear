package io.lombocska.bear.utils.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lombocska.bear.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;

@Slf4j
public final class ProducingIntegrationTestHelper {

    KafkaListenerEndpointRegistry appKafkaListenerRegistry;

    private final ObjectMapper objectMapper;

    private KafkaTemplate<String, byte[]> template;

    protected ProducingIntegrationTestHelper(final KafkaProperties kafkaProperties, final KafkaListenerEndpointRegistry appKafkaListenerRegistry,
                                             ObjectMapper objectMapper) {
        this.appKafkaListenerRegistry = appKafkaListenerRegistry;
        this.objectMapper = objectMapper;
        initTestKafkaTemplate(kafkaProperties);
    }

    private void initTestKafkaTemplate(final KafkaProperties kafkaProperties) {
        DefaultKafkaProducerFactory<String, byte[]> producerFactory
                = new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
        producerFactory.setTransactionIdPrefix("tx-test-");
        producerFactory.setValueSerializer(new ByteArraySerializer());
        template = new KafkaTemplate<>(producerFactory);
    }

    /**
     * This helper method returns when all the consumers of the tested application are considered set up and ready for message retrieval (partitions
     * are assigned).
     */
    protected void waitForTestedConsumers() {
        log.info("Waiting partition assignment of tested consumers.");
        for (MessageListenerContainer appListenerContainer : appKafkaListenerRegistry.getListenerContainers()) {
            TestUtils.waitForKafkaListenerPartitionAssignment(appListenerContainer);
        }
        log.info("Partition assignment of tested consumers finished.");
    }

    protected void sendBytes(String topicToSend, String messageKey, byte[] jsonInBytes) {
        template.executeInTransaction(t -> {
            t.send(topicToSend, messageKey, jsonInBytes);
            return null;
        });
    }

    protected void sendDto(String topicToSend, String messageKey, Object dto) throws JsonProcessingException {
        byte[] valueAsBytes = objectMapper.writeValueAsBytes(dto);
        sendBytes(topicToSend, messageKey, valueAsBytes);
    }

}
