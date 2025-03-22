package ru.varnavskii.nexign.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.varnavskii.nexign.service.CDRService;
import ru.varnavskii.nexign.service.SubscriberService;

import java.util.Map;

@RestController
@RequestMapping(value = "/cdr", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CDRController {

    private final CDRService cdrService;
    private final SubscriberService subscriberService;

    @PostMapping(value = "/generate")
    public ResponseEntity<String> generateCDR(@RequestBody Map<String, String> body) {
        int countRecordsPerSubscriber = Integer.parseInt(body.get("call_count"));
        var allSubscribers = subscriberService.getAllSubscribers();
        cdrService.generateCDRRecords(allSubscribers, countRecordsPerSubscriber);
        return ResponseEntity.ok("ok");
    }
}
