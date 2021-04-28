package com.trellis.commondata;

import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;

import java.time.format.DateTimeFormatter;

public class CustomYearMonthSerializer extends YearMonthSerializer {
    public CustomYearMonthSerializer() {
        super(DateTimeFormatter.ofPattern("MM/yyyy"));
    }
}
