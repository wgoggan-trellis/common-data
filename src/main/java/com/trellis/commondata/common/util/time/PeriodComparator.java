package com.trellis.commondata.common.util.time;

import java.time.Period;
import java.util.Comparator;

public class PeriodComparator <T extends Period> implements Comparator<T> {
   @Override
   public int compare(T p1, T p2) {
      if (p1 == null && p2 == null) {
         return 0;
      }
      final int daysInYear = 365;
      final int daysInMonth = 30;
      long p1DaySum = (p1.getYears() * daysInYear) + (p1.getMonths() * daysInMonth) + p1.getDays();
      long p2DaySum = (p2.getYears() * daysInYear) + (p2.getMonths() * daysInMonth) + p2.getDays();
      if (p1DaySum > p2DaySum) {
         return 1;
      } else if (p1DaySum < p2DaySum) {
         return 2;
      }
      return 0;
   }
}
