package ru.varnavskii.nexign.controller.cdr;

import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.varnavskii.nexign.common.util.Range;
import ru.varnavskii.nexign.controller.cdr.dto.io.CDRIn;
import ru.varnavskii.nexign.controller.cdr.dto.io.CDROut;
import ru.varnavskii.nexign.controller.cdr.dto.io.CDRReportIn;
import ru.varnavskii.nexign.controller.cdr.dto.mapper.CDRMapper;
import ru.varnavskii.nexign.controller.cdr.validator.CDRValidator;
import ru.varnavskii.nexign.service.cdr.CDRService;
import ru.varnavskii.nexign.service.subscriber.SubscriberService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/cdr", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CDRController {

    private final CDRService cdrService;
    private final SubscriberService subscriberService;
    private final CDRMapper cdrMapper;
    private final CDRValidator cdrValidator;

    @PostMapping(value = "/generate")
    public ResponseEntity<String> generateCDR(@RequestBody Map<String, String> body) {
        int countRecordsPerSubscriber = Integer.parseInt(body.get("call_count"));
        var allSubscribers = subscriberService.getAllSubscribers();
        cdrService.generateCDRRecords(allSubscribers, countRecordsPerSubscriber);
        return ResponseEntity.ok("ok");
    }

    @PostMapping
    public CDROut createCDR(@RequestBody CDRIn cdrIn) throws BadRequestException {
        cdrValidator.checkValidRecord(cdrIn);
        var cdr = cdrService.createCDREntity(cdrMapper.toEntity(cdrIn));
        return cdrMapper.toOut(cdr);
    }

    @GetMapping("/{id}")
    public List<CDROut> getCDRForSubscriber(@PathVariable Long id) {
        var subscriber = subscriberService.getSubscriberByIdOrThrowException(id);
        return cdrService.getAllCDRecordsBySubscriber(subscriber)
            .stream()
            .map(cdrMapper::toOut)
            .toList();
    }

    @PostMapping("/generateReport")
    public ResponseEntity<Map<String, Object>> generateCDRReport(@RequestBody CDRReportIn cdrReportIn) throws BadRequestException {
        cdrValidator.checkValidReportReq(cdrReportIn);
        UUID requestId = UUID.randomUUID();
        var subscriber = subscriberService.getSubscriberByIdOrThrowException(cdrReportIn.getSubscriberId());
        var range = new Range(cdrReportIn.getStartPeriod(), cdrReportIn.getEndPeriod());
        cdrService.generateReport(requestId, subscriber, range);

        Map<String, Object> response = new HashMap<>();
        response.put("requestId", requestId.toString());
        response.put("message", "Report generated");
        return ResponseEntity.ok(response);
    }
}
