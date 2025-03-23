package ru.varnavskii.nexign.IT;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;

import ru.varnavskii.nexign.IT.annotation.IT;
import ru.varnavskii.nexign.controller.subscriber.dto.io.SubscriberIn;
import ru.varnavskii.nexign.controller.subscriber.dto.io.SubscriberOut;
import ru.varnavskii.nexign.service.subscriber.SubscriberService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
public class SubscriberServiceIT {
    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAll() {
        var allUsers = subscriberService.getAllSubscribers();
        assertEquals(10, allUsers.size());
    }

    @Test
    public void testCreateSubscriber() throws Exception {
        var url = "/subscriber";
        var phoneNumber = "+79122345678";
        var subscriberIn = SubscriberIn.builder()
            .phoneNumber(phoneNumber)
            .build();
        String requestBody = objectMapper.writeValueAsString(subscriberIn);

        var res = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
        SubscriberOut responseBody = objectMapper.readValue(res.getContentAsString(), SubscriberOut.class);

        assertNotNull(responseBody.getId());
        assertEquals(phoneNumber.replaceAll("\\+", ""), responseBody.getPhoneNumber());
    }

}
