package com.example.uslugicykliczne;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;

@Component
public class ValidationUtility {
    public String validationMessagesToJSON(BindingResult bindingResult){
        ArrayList<String> errorMessages = new ArrayList<>();
        bindingResult.getAllErrors().forEach(objectError ->
                errorMessages.add(objectError.getDefaultMessage())
        );

        ObjectMapper mapper = new ObjectMapper();
        String errorJsonString;
        try {
            errorJsonString = mapper.writeValueAsString(errorMessages);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return  errorJsonString;
    }
}

