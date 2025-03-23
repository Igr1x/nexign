package ru.varnavskii.nexign.controller.cdr.dto.io;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CDROut {
    private Long id;
    private String callType;
    private String callingPhone;
    private String receivingPhone;
    private LocalDateTime startCall;
    private LocalDateTime endCall;
}
