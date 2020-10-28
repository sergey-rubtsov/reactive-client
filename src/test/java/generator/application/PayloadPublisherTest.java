package generator.application;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PayloadPublisherTest {

    public static MockWebServer mockBackEnd;
    @Value("${data}")
    private String[] data;
    @Autowired
    private PayloadPublisher payloadPublisher;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());
        ReflectionTestUtils.setField(payloadPublisher, "webClient", WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build());
    }

    @Test
    public void dataNotNull() {
        assertNotNull(data);
        assertThat(data, arrayWithSize(4));
    }

    @Test
    void generatePayload() throws InterruptedException {
        mockBackEnd.enqueue(new MockResponse().setResponseCode(200)
                .setBody("{\"requestId\":\"id\", \"responseNumber\":10.10}")
                .addHeader("Content-Type", "application/json")
        );
        payloadPublisher.generatePayload();
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        recordedRequest.getBody();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/add", recordedRequest.getPath());
    }
}