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
import ru.varnavskii.nexign.service.subscriber.SubscriberService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubscriberTestUtil subscriberTestUtil;

    @Autowired
    private CDRTestUtil cdrTestUtil;

    @Autowired
    private SubscriberService subscriberService;

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
    public void testCreateSubscriberAndGetEmptyUDR() {
        var phoneNumber = "+79122345678";
        var sub1 = subscriberTestUtil.createSubscriber(phoneNumber);
        var udrIn = new UDRIn(sub1.getId(), null);
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
        assertEquals("00:00:00", udr.getIncomingCall().totalTime());
        assertEquals("00:00:00", udr.getOutgoingCall().totalTime());
    }

    @Test
    @SneakyThrows
    public void testCreateSubscriberCDRAndGetUDRReport() {
        var phoneNumber = "+79122345678";
        var sub1 = subscriberTestUtil.createSubscriber(phoneNumber);
        var sub2 = subscriberTestUtil.createSubscriber("+79332345678");

        var start = LocalDateTime.now();
        var end = LocalDateTime.now().plusMinutes(10);
        var currentMonth = LocalDateTime.now().getMonthValue();

        cdrTestUtil.createCDR("01", sub1.getPhoneNumber(),
            sub2.getPhoneNumber(), start, end);
        cdrTestUtil.createCDR("02", sub2.getPhoneNumber(),
            sub1.getPhoneNumber(), start.plusMinutes(11), end.plusMinutes(11));

        var udrIn = new UDRIn(sub1.getId(), currentMonth);
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

        var expectedIncomingTime = Duration.between(start, end).getSeconds();
        var expectedOutgoingTime = Duration.between(start.plusMinutes(10), end.plusMinutes(10)).getSeconds();
        var expectedIncomingFormattedTime = formatDuration(expectedIncomingTime);
        var expectedOutgoingFormattedTime = formatDuration(expectedOutgoingTime);

        assertEquals(expectedIncomingFormattedTime, udr.getIncomingCall().totalTime());
        assertEquals(expectedOutgoingFormattedTime, udr.getOutgoingCall().totalTime());
    }

    @Test
    @SneakyThrows
    public void testGetUDRRecordsForAllUsers() {
        var currentMonth = LocalDateTime.now().getMonthValue();
        var body = new HashMap<String, String>();
        body.put("month", String.valueOf(currentMonth));
        var response = mockMvc.perform(post(URL + ALL_USER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        List<UDROut> udrList = objectMapper.readValue(response, new TypeReference<>() {
        });

        var countSubscribers = subscriberService.getAllSubscribers().size();
        assertEquals(countSubscribers, udrList.size());
    }

    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

}
