package com.example.Order.producer;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducer {
    private static final Logger log = LoggerFactory.getLogger(MessageProducer.class);
    private final StreamBridge streamBridge;

    public void send(Object event){
        log.info("Sending communication request for the details {}", event);
        var result = streamBridge.send("orderCommunication-out-0",event);
        log.info("Is the communication successfully triggered?: {}", result);
    }

}
