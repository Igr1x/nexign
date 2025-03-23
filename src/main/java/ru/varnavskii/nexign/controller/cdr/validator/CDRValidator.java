package ru.varnavskii.nexign.controller.cdr.validator;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;
import ru.varnavskii.nexign.controller.cdr.dto.io.CDRIn;
import ru.varnavskii.nexign.controller.cdr.dto.io.CDRReportIn;

import java.time.LocalDateTime;

@Component
public class CDRValidator {
    public void checkValidRecord(CDRIn cdrIn) throws BadRequestException {
        if (cdrIn.getCallingPhone().equals(cdrIn.getReceivingPhone())) {
            throw new BadRequestException("The phone number can't be the same");
        }
        checkCallRange(cdrIn.getStartCall(), cdrIn.getEndCall());
    }

    public void checkValidReportReq(CDRReportIn cdrReportIn) throws BadRequestException {
        checkCallRange(cdrReportIn.getStartPeriod(), cdrReportIn.getEndPeriod());
    }

    private void checkCallRange(LocalDateTime startCall, LocalDateTime endCall) throws BadRequestException {
        if (startCall.isAfter(endCall)) {
            throw new BadRequestException("Incorrect range set");
        }
    }
}
