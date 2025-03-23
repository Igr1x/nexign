package ru.varnavskii.nexign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CDROut {
    private Long id;
    private String callType;
    private String callingPhone;
    private String receivingPhone;
    private LocalDateTime startCall;
    private LocalDateTime endCall;
}
