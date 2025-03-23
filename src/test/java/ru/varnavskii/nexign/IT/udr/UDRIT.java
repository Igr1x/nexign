package ru.varnavskii.nexign.IT.udr;

import lombok.SneakyThrows;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.varnavskii.nexign.IT.annotation.IT;
import ru.varnavskii.nexign.IT.cdr.CDRTestUtil;
import ru.varnavskii.nexign.IT.subscriber.SubscriberTestUtil;
import ru.varnavskii.nexign.controller.udr.dto.UDRIn;
import ru.varnavskii.nexign.controller.udr.dto.UDROut;
import ru.varnavskii.nexign.service.udr.UDRService;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
public class UDRIT {

    public static final String URL = "/udr";
    public static final String USER_URL = "/user";
    public static final String ALL_USER_URL = "/allUsers";

    @Autowired
    private UDRService udrService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubscriberTestUtil subscriberTestUtil;

    @Autowired
    private CDRTestUtil cdrTestUtil;

    @Test
    @SneakyThrows
    public void testGetUDRWithIncorrectBody() {
        var udrIn = new UDRIn(null, 1);
        var errors = mockMvc.perform(post(URL + USER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(udrIn)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Map<String, String> errorMap = objectMapper.readValue(errors, new TypeReference<>() {
        });
        var errorMsg = errorMap.get("subscriberId");
        assertEquals(UDRIn.SUBSCRIBER_ID_NULL_MESSAGES, errorMsg);
    }

    @Test
    @SneakyThrows
    public void testCreateSubscriberCDRAndGetUDRReport() {
        var phoneNumber = "+79122345678";
        var sub1 = subscriberTestUtil.createSubscriber(phoneNumber);
        var sub2 = subscriberTestUtil.createSubscriber("+79332345678");

        var start = LocalDateTime.now();
        var end = LocalDateTime.now().plusMinutes(10);

        var cdrOut1 = cdrTestUtil.createCDR("01", sub1.getPhoneNumber(),
            sub2.getPhoneNumber(), start, end);
        var cdrOut2 = cdrTestUtil.createCDR("02", sub2.getPhoneNumber(),
            sub1.getPhoneNumber(), start.plusMinutes(10), end.plusMinutes(20));

        var udrIn = new UDRIn(sub1.getId(), LocalDateTime.now().getMonthValue());
        var response = mockMvc.perform(post(URL + USER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(udrIn)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        UDROut udr = objectMapper.readValue(response, new TypeReference<>() {
        });

        assertEquals(sub1.getPhoneNumber(), udr.getMsisdn());

    }

}
