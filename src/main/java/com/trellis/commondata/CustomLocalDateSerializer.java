package com.trellis.commondata;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.format.DateTimeFormatter;

public class CustomLocalDateSerializer extends LocalDateSerializer {
    public CustomLocalDateSerializer() {
        super(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
}
