package ru.varnavskii.nexign.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.varnavskii.nexign.converter.CallTypeConverter;
import ru.varnavskii.nexign.enumeration.CallType;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "cdr")
public class CDREntity {
    private static final String ID_COLUMN_NAME = "id";
    private static final String CALL_TYPE_COLUMN_NAME = "call_type";
    private static final String CALLING_COLUMN_NAME = "calling";
    private static final String RECEIVING_COLUMN_NAME = "receiving";
    private static final String START_CALL_COLUMN_NAME = "start_call";
    private static final String END_CALL_COLUMN_NAME = "end_call";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_COLUMN_NAME)
    private Long id;

    @Column(name = CALL_TYPE_COLUMN_NAME)
    private CallType callType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CALLING_COLUMN_NAME, nullable = false)
    private SubscriberEntity callingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = RECEIVING_COLUMN_NAME, nullable = false)
    private SubscriberEntity receivingNumber;

    @Column(name = START_CALL_COLUMN_NAME, nullable = false)
    private LocalDateTime startCall;

    @Column(name = END_CALL_COLUMN_NAME, nullable = false)
    private LocalDateTime endCall;
}
