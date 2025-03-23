package ru.varnavskii.nexign.IT.cdr;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import org.springframework.test.web.servlet.MockMvc;

import ru.varnavskii.nexign.controller.cdr.dto.io.CDRIn;
import ru.varnavskii.nexign.controller.cdr.dto.io.CDROut;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class CDRTestUtil {

    public static final String URL = "/cdr";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    public CDROut createCDR(String callType, String incomingPhone,
                             String outgoingPhone, LocalDateTime start,
                             LocalDateTime end) {
        var cdrIn = new CDRIn(callType, incomingPhone, outgoingPhone, start, end);

        var createdCdr = mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cdrIn)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
        return objectMapper.readValue(createdCdr.getContentAsString(), CDROut.class);
    }
}
