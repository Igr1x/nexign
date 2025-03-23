package ru.varnavskii.nexign.IT.cdr;

import lombok.SneakyThrows;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.varnavskii.nexign.IT.annotation.IT;
import ru.varnavskii.nexign.IT.subscriber.SubscriberTestUtil;
import ru.varnavskii.nexign.common.util.Range;
import ru.varnavskii.nexign.controller.cdr.dto.io.CDRGenerate;
import ru.varnavskii.nexign.controller.cdr.dto.io.CDRIn;
import ru.varnavskii.nexign.controller.cdr.dto.io.CDRReportIn;
import ru.varnavskii.nexign.repository.cdr.entity.CDREntity;
import ru.varnavskii.nexign.service.cdr.CDRService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
public class CDRIT {
    public static final String REPORTS_PATH = "reports";

    @Autowired
    private CDRService cdrService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubscriberTestUtil subscriberUtil;

    @Autowired
    private CDRTestUtil cdrUtil;

    @Test
    @SneakyThrows
    public void testGenerateCDRRecords() {
        var url = CDRTestUtil.URL + "/generate";
        var countRecords = 10000;

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("call_count", String.valueOf(countRecords));
        var req = new CDRGenerate(countRecords);
        String requestBody = objectMapper.writeValueAsString(req);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());

        var allCDRRecords = cdrService.getAllCDRecords();
        assertEquals(countRecords, allCDRRecords.size());

        Map<Long, List<CDREntity>> callsByUser = new HashMap<>();
        for (CDREntity cdr : allCDRRecords) {
            callsByUser.computeIfAbsent(cdr.getCalling().getId(), k -> new ArrayList<>()).add(cdr);
            callsByUser.computeIfAbsent(cdr.getReceiving().getId(), k -> new ArrayList<>()).add(cdr);
        }

        for (Map.Entry<Long, List<CDREntity>> entry : callsByUser.entrySet()) {
            List<CDREntity> calls = entry.getValue();
            calls.sort(Comparator.comparing(CDREntity::getStartCall));
            for (int i = 1; i < calls.size(); i++) {
                CDREntity prev = calls.get(i - 1);
                CDREntity curr = calls.get(i);

                assertTrue(prev.getEndCall().isBefore(curr.getStartCall()));
            }
        }
    }

    @Test
    @SneakyThrows
    public void testCreateSubscribersAndAddCDR() {
        var phone1 = "+79122345678";
        var sub1 = subscriberUtil.createSubscriber(phone1);
        var phone2 = "+71345345678";
        var sub2 = subscriberUtil.createSubscriber(phone2);
        var callType = "01";
        var start = LocalDateTime.now();
        var end = LocalDateTime.now().plusMinutes(10);

        var cdrOut = cdrUtil.createCDR(callType, sub1.getPhoneNumber(),
            sub2.getPhoneNumber(), start, end);

        assertEquals(phone1.replaceAll("\\+", "").replaceFirst("7", "8"),
            cdrOut.getCallingPhone());
        assertEquals(phone2.replaceAll("\\+", "").replaceFirst("7", "8"),
            cdrOut.getReceivingPhone());
        assertEquals(callType, cdrOut.getCallType());
        assertEquals(start, cdrOut.getStartCall());
        assertEquals(end, cdrOut.getEndCall());
    }

    @Test
    @SneakyThrows
    public void testCreateCDRWithInvalidSamePhoneNumber() {
        var phone1 = "+79122345678";
        var sub1 = subscriberUtil.createSubscriber(phone1);
        var callType = "01";
        var start = LocalDateTime.now();
        var end = LocalDateTime.now().plusMinutes(10);

        var cdrIn = new CDRIn(callType, sub1.getPhoneNumber(),
            sub1.getPhoneNumber(), start, end);

        mockMvc.perform(post(CDRTestUtil.URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cdrIn)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("The phone number can't be the same"));
    }

    @Test
    @SneakyThrows
    public void testCreateCDRWithInvalidCallRange() {
        var sub1 = subscriberUtil.createSubscriber("+79122345678");
        var sub2 = subscriberUtil.createSubscriber("+79452345678");
        var callType = "01";
        var start = LocalDateTime.now();
        var end = LocalDateTime.now().plusMinutes(10);

        var cdrIn = new CDRIn(callType, sub1.getPhoneNumber(),
            sub2.getPhoneNumber(), end, start);

        mockMvc.perform(post(CDRTestUtil.URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cdrIn)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Incorrect range set"));
    }

    @Test
    @SneakyThrows
    public void testGenerateReportsForTwoSubscribers() {
        var phone1 = "+79122345678";
        var sub1 = subscriberUtil.createSubscriber(phone1);
        var phone2 = "+71345345678";
        var sub2 = subscriberUtil.createSubscriber(phone2);
        var callType = "01";
        var start = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        var end = LocalDateTime.of(2025, 1, 1, 0, 10, 0);

        var startCycle = start;
        var endCycle = end;
        for (int i = 0; i < 3; i++) {
            startCycle = startCycle.plusMinutes(10 * i);
            endCycle = endCycle.plusMinutes(10 * i);
            cdrUtil.createCDR(callType, sub1.getPhoneNumber(), sub2.getPhoneNumber(),
                startCycle, endCycle);
        }
        var period1 = new Range(start.plusMinutes(10), end.plusMinutes(30));
        var period2 = new Range(start, start.plusMinutes(20));

        var requestId1 = generateRepAndReturnId(sub1.getId(), period1);
        var requestId2 = generateRepAndReturnId(sub2.getId(), period2);

        var createdFiles = new File(REPORTS_PATH).listFiles((d, name) -> name.contains(requestId1) || name.contains(requestId2));

        assertNotNull(createdFiles);
        assertEquals(2, createdFiles.length);

        for (File file : createdFiles) {
            List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
            assertEquals(3, lines.size());
        }

        for (File file : createdFiles) {
            if (file.exists()) {
                boolean deleted = file.delete();
                assertTrue(deleted);
            }
        }
    }

    @SneakyThrows
    private String generateRepAndReturnId(Long subId, Range period) {
        var url = CDRTestUtil.URL + "/generateReport";
        var res1 = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CDRReportIn(subId, period.start(), period.end()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Report generated"))
            .andExpect(jsonPath("$.requestId").exists())
            .andReturn()
            .getResponse()
            .getContentAsString();
        return objectMapper.readTree(res1).get("requestId").asText();
    }

}
