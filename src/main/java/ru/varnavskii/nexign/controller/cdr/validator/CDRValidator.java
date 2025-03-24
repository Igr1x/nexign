package ru.varnavskii.nexign.controller.cdr.validator;

import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

import ru.varnavskii.nexign.common.util.Range;
import ru.varnavskii.nexign.controller.cdr.dto.io.CDRIn;
import ru.varnavskii.nexign.controller.cdr.dto.io.CDRReportIn;
import ru.varnavskii.nexign.service.cdr.CDRService;
import ru.varnavskii.nexign.service.subscriber.SubscriberService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CDRValidator {

    private final CDRService cdrService;
    private final SubscriberService subscriberService;

    public void checkValidRecord(CDRIn cdrIn) throws BadRequestException {
        if (cdrIn.getCallingPhone().equals(cdrIn.getReceivingPhone())) {
            throw new BadRequestException("The phone number can't be the same");
        }
        checkCallRange(cdrIn.getStartCall(), cdrIn.getEndCall());
        var timeCallRange = new Range(cdrIn.getStartCall(), cdrIn.getEndCall());
        checkTimeCallOverlappingWithExistedRecords(cdrIn.getCallingPhone(), cdrIn.getReceivingPhone(), timeCallRange);
    }

    public void checkValidReportReq(CDRReportIn cdrReportIn) throws BadRequestException {
        checkCallRange(cdrReportIn.getStartPeriod(), cdrReportIn.getEndPeriod());
    }

    private void checkTimeCallOverlappingWithExistedRecords(String callingPhone,
                                                            String receivingPhone,
                                                            Range timeCallRange) throws BadRequestException {
        var sub1 = subscriberService.getSubscriberByPhoneOrThrowException(callingPhone);
        var sub2 = subscriberService.getSubscriberByPhoneOrThrowException(receivingPhone);
        if (cdrService.checkHasOverlappingCDRRecords(sub1.getId(), sub2.getId(), timeCallRange)) {
            throw new BadRequestException("Call time range overlaps with an existing record");
        }
    }

    private void checkCallRange(LocalDateTime startCall, LocalDateTime endCall) throws BadRequestException {
        if (startCall.isAfter(endCall)) {
            throw new BadRequestException("Incorrect range set");
        }
    }
}
