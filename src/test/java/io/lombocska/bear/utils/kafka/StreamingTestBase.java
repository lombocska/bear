package io.lombocska.bear.utils.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class StreamingTestBase {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private KafkaListenerEndpointRegistry appKafkaListenerRegistry;

    @Autowired
    protected ObjectMapper objectMapper;

    protected ConsumingIntegrationTestHelper consumingHelper;

    protected ProducingIntegrationTestHelper producingHelper;

    @Before
    public final void initialize() {
        consumingHelper = new ConsumingIntegrationTestHelper(kafkaProperties);
        producingHelper = new ProducingIntegrationTestHelper(kafkaProperties, appKafkaListenerRegistry, objectMapper);
        producingHelper.waitForTestedConsumers();
    }

    @After
    public void tearDown() {
        consumingHelper.tearDown();
    }

    protected void setUpConsumer(String topicToListen, String dlqTopicName) {
        consumingHelper.setUpConsumer(topicToListen, dlqTopicName);
    }

    protected LinkedBlockingQueue<ConsumerRecord<String, byte[]>> getTopicRecords(final String topic) {
        return consumingHelper.getTopicRecords(topic);
    }

    protected ConsumerRecord<String, byte[]> pollRecord(final String topic, long timeout, TimeUnit timeUnit) throws InterruptedException {
        return getTopicRecords(topic).poll(timeout, timeUnit);
    }

    protected ConsumerRecord<String, byte[]> pollRecord(final String topic) throws InterruptedException {
        return pollRecord(topic, defaultTimeout(), defaultTimeoutTimeUnit());
    }

    protected <T> T pollValue(Class<T> valueType, final String topic, long timeout, TimeUnit timeUnit) throws InterruptedException, IOException {
        ConsumerRecord<String, byte[]> consumerRecord = getTopicRecords(topic).poll(timeout, timeUnit);
        assertNotNull(String.format("No record received on topic: %s", topic), consumerRecord);
        return objectMapper.readValue(consumerRecord.value(), valueType);
    }

    protected <T> T pollValue(Class<T> valueType, final String topic) throws IOException, InterruptedException {
        return pollValue(valueType, topic, defaultTimeout(), defaultTimeoutTimeUnit());
    }

    protected void sendBytes(String topicToSend, byte[] jsonInBytes) {
        String messageKey = UUID.randomUUID().toString();
        sendBytes(topicToSend, messageKey, jsonInBytes);
    }

    protected void sendBytes(String topicToSend, String messageKey, byte[] jsonInBytes) {
        producingHelper.sendBytes(topicToSend, messageKey, jsonInBytes);
    }

    protected long defaultTimeout() {
        return 1000L;
    }

    protected TimeUnit defaultTimeoutTimeUnit() {
        return TimeUnit.SECONDS;
    }

}
