package ru.varnavskii.nexign.IT.subscriber;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.varnavskii.nexign.IT.annotation.IT;
import ru.varnavskii.nexign.controller.subscriber.dto.io.SubscriberIn;
import ru.varnavskii.nexign.controller.subscriber.dto.io.SubscriberOut;
import ru.varnavskii.nexign.service.subscriber.SubscriberService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
public class SubscriberIT {

    public static final String URL = "/subscriber";

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubscriberTestUtil subscriberUtil;

    @Test
    public void testGetAll() {
        var allUsers = subscriberService.getAllSubscribers();
        assertEquals(10, allUsers.size());
    }

    @Test
    public void testCreateSubscriber() {
        var phoneNumber = "+79122345678";
        SubscriberOut responseBody = subscriberUtil.createSubscriber(phoneNumber);

        assertNotNull(responseBody.getId());
        assertEquals(phoneNumber.replaceAll("\\+", "").replaceFirst("7", "8"),
            responseBody.getPhoneNumber());
    }

    @Test
    @SneakyThrows
    public void testCreateSubscriberWithIncorrectPhoneNumber() {
        var phoneNumber = "12423432429122345678";
        var subscriberIn = SubscriberIn.builder()
            .phoneNumber(phoneNumber)
            .build();
        String requestBody = objectMapper.writeValueAsString(subscriberIn);
        var errors = mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
        Map<String, String> errorMap = objectMapper.readValue(errors, new TypeReference<>() {
        });
        var errorMsg = errorMap.get("phoneNumber");
        assertEquals(SubscriberIn.INVALID_PHONE_NUMBER, errorMsg);
    }
}
