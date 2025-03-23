package ru.varnavskii.nexign.controller.subscriber.dto.io;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubscriberOut {
    private Long id;
    private String phoneNumber;
}
