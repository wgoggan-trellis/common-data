package com.trellis.commondata;

import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer;

import java.time.format.DateTimeFormatter;

public class CustomYearMonthDeserializer extends YearMonthDeserializer {
    public CustomYearMonthDeserializer() {
        super(DateTimeFormatter.ofPattern("MM/yyyy"));
    }
}
