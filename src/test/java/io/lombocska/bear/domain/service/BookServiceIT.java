package io.lombocska.bear.domain.service;

import io.lombocska.bear.BearApplication;
import io.lombocska.bear.common.BearDomainEvent;
import io.lombocska.bear.common.BookCreatedEvent;
import io.lombocska.bear.domain.entity.Writer;
import io.lombocska.bear.domain.repository.BookRepository;
import io.lombocska.bear.domain.repository.WriterRepository;
import io.lombocska.bear.utils.Assertions;
import io.lombocska.bear.utils.kafka.ConsumingIntegrationTestBase;
import io.lombocska.bear.utils.model.TestModelBuilder;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BearApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookServiceIT extends ConsumingIntegrationTestBase {


    @Value("${bear.kafka.domain-event-topic}")
    protected String bookDomainEventTopic;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WebTestClient testClient;

    private CreateBookCommand createBookCommand;
    private Writer writer;

    @Before
    public void setup() throws JSONException {
        //Prepare the db
        this.cleanDb();

        // Build test objects
        writer = TestModelBuilder.build(Collections.emptyList());
        var savedWriter = writerRepository.save(writer);
        createBookCommand = TestModelBuilder.build(savedWriter);

        // Setup kafka test consumer
        setUpConsumer(bookDomainEventTopic, null);
    }

    @After
    public void after() {
        this.cleanDb();
        super.tearDown();
    }

    private void cleanDb() {
        bookRepository.deleteAll();
        writerRepository.deleteAll();
    }

    @Test
    public void testBookCreation() throws InterruptedException, IOException {
        testClient.post().uri("/api")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(createBookCommand), CreateBookCommand.class)
                .exchange()
                .expectStatus()
                .isOk();

        var domainEvents = getTopicRecords(bookDomainEventTopic);
        var poll = domainEvents.poll(50, TimeUnit.SECONDS);
        assertNotNull(poll);

        BearDomainEvent<BookCreatedEvent> bookDomainEvent = objectMapper.readValue(poll.value(), BearDomainEvent.class);
        var bookCreatedEvent = bookDomainEvent.getPayload();
        var savedBook = bookRepository.findOneById(bookCreatedEvent.getAggregationId());
        Assertions.assertSavedBook(bookDomainEvent, savedBook);
    }

}
