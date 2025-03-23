package ru.varnavskii.nexign.controller.subscriber;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.varnavskii.nexign.controller.subscriber.dto.io.SubscriberIn;
import ru.varnavskii.nexign.controller.subscriber.dto.io.SubscriberOut;
import ru.varnavskii.nexign.controller.subscriber.mapper.SubscriberMapper;
import ru.varnavskii.nexign.service.subscriber.SubscriberService;

@RestController
@RequestMapping(value = "/subscriber", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SubscriberController {

    private final SubscriberService subscriberService;
    private final SubscriberMapper subscriberMapper;

    @PostMapping
    public SubscriberOut createSubscriber(@Valid @RequestBody SubscriberIn subscriberIn) {
        var entity = subscriberMapper.toEntity(subscriberIn);
        entity = subscriberService.createSubscriber(entity);
        return subscriberMapper.toOut(entity);
    }
}
