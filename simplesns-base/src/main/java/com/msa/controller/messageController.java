package com.msa.controller;


import com.msa.SimpleSNSApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class messageController {

    private final StreamBridge streamBridge;

    public messageController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @GetMapping(value = "/direct/{message}")
    public Mono<Void> directMessage(@PathVariable String message) {

        return Mono.just(message)
                .doOnNext(s -> streamBridge.send(SimpleSNSApplication.ProducerChannel.DIRECT, message))
                .then();
    }

    @GetMapping(value = "/broadcast/{message}")
    public Mono<Void> broadcastMessage(@PathVariable String message) {

        return Mono.just(message)
                .doOnNext(s -> streamBridge.send(SimpleSNSApplication.ProducerChannel.BROADCAST, message))
                .then();
    }
}
