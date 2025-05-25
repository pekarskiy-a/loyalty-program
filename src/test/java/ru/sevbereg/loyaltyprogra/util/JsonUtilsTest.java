package ru.sevbereg.loyaltyprogra.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sevbereg.loyaltyprogra.domain.Client;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class JsonUtilsTest {

    @Test
    void toJson() {
        Client client = new Client();
        client.setClientCategory("Alex");
        client.setComment("Comment");

        String expectedJson = "{\n" +
                "  \"id\" : 0,\n" +
                "  \"creatDate\" : null,\n" +
                "  \"version\" : null,\n" +
                "  \"updateDate\" : null,\n" +
                "  \"phoneNumber\" : null,\n" +
                "  \"telegram\" : null,\n" +
                "  \"email\" : null,\n" +
                "  \"sex\" : null,\n" +
                "  \"surname\" : null,\n" +
                "  \"name\" : null,\n" +
                "  \"patronymic\" : null,\n" +
                "  \"birthdate\" : null,\n" +
                "  \"botState\" : null,\n" +
                "  \"cards\" : [ ],\n" +
                "  \"clientCategory\" : \"Alex\",\n" +
                "  \"comment\" : \"Comment\",\n" +
                "  \"active\" : true\n" +
                "}";

        String actualJson = JsonUtils.toJson(client);

        assertEquals(expectedJson, actualJson);
    }

}