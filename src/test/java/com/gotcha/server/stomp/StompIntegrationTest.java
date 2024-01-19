package com.gotcha.server.stomp;

import static com.gotcha.server.common.TestFixture.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import com.gotcha.server.common.TestRepository;
import com.gotcha.server.question.dto.message.QuestionUpdateMessage;
import com.gotcha.server.question.repository.IndividualQuestionRepository;
import com.gotcha.server.question.service.QuestionUpdateType;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StompIntegrationTest {
    @LocalServerPort
    private int port;

    @MockBean
    private TestRepository testRepository;

    @MockBean
    private IndividualQuestionRepository individualQuestionRepository;

    private final WebSocketStompClient stompClient;

    public StompIntegrationTest() {
        this.stompClient = getStompClient();
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
        String url = String.format("ws://localhost:%d/ws", port);

        // when & then
        assertDoesNotThrow(() -> {
            stompClient.connectAsync(url,new StompSessionHandlerAdapter(){}).get(2, TimeUnit.SECONDS);
        });
    }

    @Test
    void 질문_수정하기() throws Exception {
        // given
        String URL = String.format("ws://localhost:%d/ws", port);
        given(individualQuestionRepository.findById(1L)).willReturn(
                Optional.of(테스트개별질문(null, "자기소개해주세요.", 0, true, 5)));

        BlockingQueue<QuestionUpdateMessage> blockingQueue = new LinkedBlockingQueue<>();
        StompSession session = getStompClient().connectAsync(URL, new TestSessionHandler(blockingQueue)).get(2, TimeUnit.SECONDS);

        // when
        session.send("/pub/question/1", new QuestionUpdateMessage("바뀐내용", QuestionUpdateType.CONTENT));
        QuestionUpdateMessage 발행된_메세지 = blockingQueue.poll(2, TimeUnit.SECONDS);

        // then
        log.info("수정 값: {}, 수정 타입: {}", 발행된_메세지.value(), 발행된_메세지.type());
        assertNotNull(발행된_메세지);
    }

}
