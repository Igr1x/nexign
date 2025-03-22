package ru.varnavskii.nexign.IT;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.varnavskii.nexign.IT.annotation.IT;
import ru.varnavskii.nexign.service.CDRService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        assertEquals(countRecords, cdrService.getAllCDRecords().size());
    }
}
