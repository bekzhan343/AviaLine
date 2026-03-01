package com.example.avialine.security.util;



import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateDeserializer extends StdDeserializer<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    protected DateDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        String value = p.getText().trim();

        try {
            return LocalDate.parse(value, formatter);
        }catch (DateTimeParseException ex){
            throw new InvalidFormatException(p,"Invalid date format, expected dd.MM.yyyy, got: " + value, value, LocalDate.class);
        }
    }
}