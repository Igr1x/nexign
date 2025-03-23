package ru.varnavskii.nexign.IT.subscriber;

import lombok.SneakyThrows;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import ru.varnavskii.nexign.controller.subscriber.dto.io.SubscriberIn;
import ru.varnavskii.nexign.controller.subscriber.dto.io.SubscriberOut;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class SubscriberTestUtil {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    public SubscriberOut createSubscriber(String phoneNumber) {
        var subscriberIn = SubscriberIn.builder()
            .phoneNumber(phoneNumber)
            .build();
        String requestBody = objectMapper.writeValueAsString(subscriberIn);

        var res = mockMvc.perform(post(SubscriberIT.URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
        return objectMapper.readValue(res.getContentAsString(), SubscriberOut.class);
    }
}
