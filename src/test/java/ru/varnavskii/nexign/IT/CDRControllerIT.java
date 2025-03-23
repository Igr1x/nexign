package ru.varnavskii.nexign.IT;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.varnavskii.nexign.IT.annotation.IT;
import ru.varnavskii.nexign.repository.cdr.entity.CDREntity;
import ru.varnavskii.nexign.service.cdr.CDRService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
public class CDRControllerIT {

    @Autowired
    private CDRService cdrService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGenerateCDRRecords() throws Exception {
        var url = "/cdr/generate";
        var countRecords = 10000;

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("call_count", String.valueOf(countRecords));
        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

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


}
