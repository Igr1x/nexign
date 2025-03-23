package ru.varnavskii.nexign.controller.subscriber.dto.io;

import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Represents a request for creating a subscriber.
 * <p>
 * This class is used to store the phone number of a subscriber, ensuring that it follows
 * the required format.
 * </p>
 */
@Getter
@Builder
public class SubscriberIn {
    private static final String PHONE_NUMBER_PATTERN = "^(?:\\+7|8)\\d{10}$";

    public static final String INVALID_PHONE_NUMBER = "Invalid phone number";

    @NotNull
    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = INVALID_PHONE_NUMBER)
    private String phoneNumber;
}
