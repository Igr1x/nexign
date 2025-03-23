package ru.varnavskii.nexign.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.varnavskii.nexign.dto.UDRIn;
import ru.varnavskii.nexign.dto.UDROut;
import ru.varnavskii.nexign.service.UDRService;

@RestController
@RequestMapping(value = "/udr", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UDRController {

    private final UDRService udrService;

    @PostMapping(value = "/getReport")
    public UDROut getReport(@RequestBody UDRIn udrIn) {
        return udrService.getUDRReportForSubscriber(udrIn.getSubscriberId(), udrIn.getMonth());
    }
}
