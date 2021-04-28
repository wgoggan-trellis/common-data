package com.trellis.commondata;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.format.DateTimeFormatter;

public class CustomLocalDateDeserializer extends LocalDateDeserializer {
    public CustomLocalDateDeserializer() {
        super(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
}
