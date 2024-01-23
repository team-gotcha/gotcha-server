package com.gotcha.server.stomp;

import com.gotcha.server.question.dto.message.QuestionUpdateMessage;
import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

@Slf4j
public class TestSessionHandler implements StompSessionHandler {
    private final BlockingQueue<QuestionUpdateMessage> blockingQueue;
    private final String jwtToken;

    public TestSessionHandler(BlockingQueue<QuestionUpdateMessage> blockingQueue, String jwtToken) {
        this.blockingQueue = blockingQueue;
        this.jwtToken = jwtToken;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        StompHeaders subHeaders = new StompHeaders();
        subHeaders.setLogin("Bearer " + jwtToken);
        subHeaders.setDestination("/sub/question/1");
        session.subscribe(subHeaders, this);
    }

    @Override
    public void handleException(StompSession session, @Nullable StompCommand command,
            StompHeaders headers, byte[] payload, Throwable exception) {
        log.info("Exception in handleException of StompSessionHandler: {}", exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.info("Exception in handleTransportError of StompSessionHandler: {}", exception.getMessage());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return QuestionUpdateMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        blockingQueue.add((QuestionUpdateMessage) payload);
    }
}
