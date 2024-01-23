package com.gotcha.server.stomp;

import static com.gotcha.server.common.TestFixture.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import com.gotcha.server.common.DatabaseCleaner;
import com.gotcha.server.common.TestRepository;
import com.gotcha.server.common.TestTokenCreator;
import com.gotcha.server.question.dto.message.QuestionUpdateMessage;
import com.gotcha.server.question.repository.IndividualQuestionRepository;
import com.gotcha.server.question.service.QuestionUpdateType;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@Slf4j
@ActiveProfiles(profiles = "dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StompIntegrationTest {
    @LocalServerPort
    private int port;

    @MockBean
    private TestRepository testRepository;

    @MockBean
    private IndividualQuestionRepository individualQuestionRepository;

    @Autowired
    private TestTokenCreator testTokenCreator;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private final WebSocketStompClient stompClient;

    public StompIntegrationTest() {
        this.stompClient = getStompClient();
    }

    @BeforeEach
    void setUp() {
        databaseCleaner.clear();
    }

    private WebSocketStompClient getStompClient() {
        WebSocketStompClient stompClient = new WebSocketStompClient(
                new SockJsClient(List.of(
                        new WebSocketTransport(
                                new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
    }
    
    @Test
    void 웹소켓_연결하기() {
        // given
        String URL = String.format("ws://localhost:%d/ws", port);
        String jwtToken = testTokenCreator.create();

        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.setLogin("Bearer " + jwtToken);

        // when & then
        assertDoesNotThrow(() -> {
            stompClient.connectAsync(
                    URL,
                    new WebSocketHttpHeaders(),
                    stompHeaders,
                    new StompSessionHandlerAdapter(){}).get(2, TimeUnit.SECONDS);
        });
    }

    @Test
    void 질문_수정하기() throws Exception {
        // given
        given(individualQuestionRepository.findById(1L)).willReturn(
                Optional.of(테스트개별질문(null, "자기소개해주세요.", 0, true, 5, null)));

        String URL = String.format("ws://localhost:%d/ws", port);
        String jwtToken = testTokenCreator.create();

        StompHeaders connectionHeaders = new StompHeaders();
        connectionHeaders.setLogin("Bearer " + jwtToken);
        BlockingQueue<QuestionUpdateMessage> blockingQueue = new LinkedBlockingQueue<>();
        StompSession session = getStompClient().connectAsync(
                URL,
                new WebSocketHttpHeaders(),
                connectionHeaders,
                new TestSessionHandler(blockingQueue, jwtToken)).get(2, TimeUnit.SECONDS);

        StompHeaders pubHeaders = new StompHeaders();
        pubHeaders.setLogin("Bearer " + jwtToken);
        pubHeaders.setDestination("/pub/question/1");

        // when
        session.send(pubHeaders, new QuestionUpdateMessage("바뀐내용", QuestionUpdateType.CONTENT));
        QuestionUpdateMessage 발행된_메세지 = blockingQueue.poll(2, TimeUnit.SECONDS);

        // then
        log.info("수정 값: {}, 수정 타입: {}", 발행된_메세지.value(), 발행된_메세지.type());
        assertNotNull(발행된_메세지);
    }

}
