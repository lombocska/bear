package io.lombocska.bear.utils.kafka;

import io.lombocska.bear.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class ConsumingIntegrationTestHelper {

    protected DefaultKafkaConsumerFactory<String, byte[]> consumerFactory;

    protected List<KafkaMessageListenerContainer<String, byte[]>> testListenerContainers = new ArrayList<>();

    protected Map<String, LinkedBlockingQueue<ConsumerRecord<String, byte[]>>> records = new ConcurrentHashMap<>();

    protected ConsumingIntegrationTestHelper(KafkaProperties properties) {
        initTestConsumerFactory(properties);
    }

    private void initTestConsumerFactory(final KafkaProperties properties) {
        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties = properties.buildConsumerProperties();
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        consumerProperties.putIfAbsent(ConsumerConfig.CLIENT_ID_CONFIG, "it-test-consumer");
        // the command producers should be transactional so we test this behaviour with the following consumer settings
        consumerProperties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        // create a Kafka consumer factory
        consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);
    }

    protected void setUpConsumer(String topicToListen, String dlqTopicName) {

        // set the topic that needs to be consumed
        ContainerProperties containerProperties;
        if (dlqTopicName == null) {
            containerProperties = new ContainerProperties(topicToListen);
        } else {
            containerProperties = new ContainerProperties(topicToListen, dlqTopicName);
        }

        // create a Kafka MessageListenerContainer
        var testListenerContainer = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        testListenerContainer.setClientIdSuffix(topicToListen);

        // setup a Kafka message listener
        testListenerContainer.setupMessageListener((MessageListener<String, byte[]>) record -> {
            log.info("test-listener received message='{}'", record.toString());

            LinkedBlockingQueue<ConsumerRecord<String, byte[]>> results = getTopicRecords(record.topic());
            results.add(record);

            records.put(record.topic(), results);
        });

        // start the testListenerContainer and underlying message listener
        testListenerContainer.start();
        testListenerContainers.add(testListenerContainer);
        // Wait for partition assignment for test KafkaMessageListenerContainer before executing tests
        log.info("Waiting for test consumer partition assignment. [topic={}]", topicToListen);
        TestUtils.waitForKafkaListenerPartitionAssignment(testListenerContainer);
        log.info("Test consumer partition assignment finished. [topic={}]", topicToListen);
    }

    protected LinkedBlockingQueue<ConsumerRecord<String, byte[]>> getTopicRecords(final String topic) {
        LinkedBlockingQueue<ConsumerRecord<String, byte[]>> consumerRecords = records.get(topic);
        if (null == consumerRecords) {
            consumerRecords = new LinkedBlockingQueue<>();
            records.put(topic, consumerRecords);
        }

        return consumerRecords;
    }

    protected void tearDown() {
        // stop the testListenerContainers
        testListenerContainers.forEach(container -> {
            container.stop();
            Awaitility.await().atMost(3, TimeUnit.SECONDS).until(() -> !container.isRunning());
        });
    }

}
