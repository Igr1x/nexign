package ru.varnavskii.nexign.controller.cdr.validator;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

import ru.varnavskii.nexign.controller.cdr.dto.io.CDRIn;

@Component
public class CDRValidator {
    public void checkValidRecord(CDRIn cdrIn) throws BadRequestException {
        if (cdrIn.getCallingPhone().equals(cdrIn.getReceivingPhone())) {
            throw new BadRequestException("The phone number can't be the same");
        }
    }
}
