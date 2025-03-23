package ru.varnavskii.nexign.controller.udr;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.varnavskii.nexign.controller.udr.dto.UDRIn;
import ru.varnavskii.nexign.controller.udr.dto.UDROut;
import ru.varnavskii.nexign.service.udr.UDRService;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/udr", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UDRController {

    private final UDRService udrService;

    @PostMapping("/user")
    public UDROut getReport(@Valid @RequestBody UDRIn udrIn) {
        return udrService.getUDRReportForSubscriber(udrIn.getSubscriberId(), udrIn.getMonth());
    }

    @PostMapping("/allUsers")
    public List<UDROut> getReportForAllSubscriberByMonth(@Valid @RequestBody Map<String, String> body) {
        var month = Integer.parseInt(body.get("month"));
        return udrService.getUDRReportForAllSubscriberByMonth(month);
    }
}
