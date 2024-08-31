package com.app.exception;

import com.app.domain.model.Users;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class UserValidationException extends JsonDeserializer<Users> {

    private static final String ERROR_MESSAGE = "";

    @Override
    public Users deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        try {
            String username = node.get("username").asText();
            String email = node.get("email").asText();
            return new Users(username, email);
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException(ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
        }
    }

}