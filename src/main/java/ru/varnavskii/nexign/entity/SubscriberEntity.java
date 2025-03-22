package ru.varnavskii.nexign.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "subscriber")
public class SubscriberEntity {
    private static final String ID_COLUMN_NAME = "id";
    private static final String PHONE_NUMBER_COLUMN_NAME = "phone_number";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_COLUMN_NAME)
    private Long id;

    @Column(name = PHONE_NUMBER_COLUMN_NAME)
    private String phoneNumber;

}
