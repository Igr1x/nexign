package ru.varnavskii.nexign;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

import ru.varnavskii.nexign.service.SubscriberService;

@ActiveProfiles("test")
@SpringBootTest
public class SubscriberServiceTest {
    @Autowired
    private SubscriberService subscriberService;

    @Test
    public void testGetAll() {
        var allUsers = subscriberService.getAllSubscribers();
        Assertions.assertEquals(10, allUsers.size());
    }

}
