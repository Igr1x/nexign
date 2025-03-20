package ru.varnavskii.nexign.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
    @Column(name = ID_COLUMN_NAME)
    private Long id;

    @Column(name = PHONE_NUMBER_COLUMN_NAME)
    private String phoneNumber;

}
