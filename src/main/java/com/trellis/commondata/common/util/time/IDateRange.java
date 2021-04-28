package com.trellis.commondata.common.util.time;

import java.time.LocalDate;

public interface IDateRange {
   LocalDate getStartDate();
   void setStartDate(LocalDate startDate);
   LocalDate getEndDate();
   void setEndDate(LocalDate endDate);
}
