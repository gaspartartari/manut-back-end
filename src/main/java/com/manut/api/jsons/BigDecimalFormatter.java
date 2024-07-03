package com.manut.api.jsons;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class BigDecimalFormatter extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String value = p.getText();
        if (value != null) {
            // Trim the string to remove leading and trailing whitespace
            value = value.trim();

            // Check if the string is empty or only contains whitespace
            if (value.isEmpty()) {
                // Return null or BigDecimal.ZERO based on your requirement
                return null; // or BigDecimal.ZERO
            }

            // Replace comma with dot to handle decimal separator
            value = value.replace(",", ".");

            // Handle potential grouping separators, if needed, before this line

            try {
                return new BigDecimal(value);
            } catch (NumberFormatException e) {
                // Log the error or handle it as per your requirements
                // Optionally, return null or a default value
                return null; // or BigDecimal.ZERO
            }
        }
        return null; // Or BigDecimal.ZERO depending on your requirements
    }

}
