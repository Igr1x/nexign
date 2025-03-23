package ru.varnavskii.nexign.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.varnavskii.nexign.common.enumeration.CallType;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class CallTypeConverter implements AttributeConverter<CallType, String> {
    @Override
    public String convertToDatabaseColumn(CallType callType) {
        if (callType == null) {
            return null;
        }
        return callType.getValue();
    }

    @Override
    public CallType convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }

        return Stream.of(CallType.values())
                .filter(c -> c.getValue().equals(s))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
