package com.trellis.commondata.common.util.time;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
   public final static DateTimeFormatter DFLT_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
   public final static long SECONDS_IN_MINUTE = 60;
   public final static long SECONDS_IN_HOUR = 60 * SECONDS_IN_MINUTE;
   public final static long SECONDS_IN_DAY = 24 * SECONDS_IN_HOUR;
   public final static long SECONDS_IN_WEEK = 7 * SECONDS_IN_DAY;
   public final static long SECONDS_IN_YEAR = 52 * SECONDS_IN_WEEK;

   /**
    * Add N days to the current day to obtain the Period object.
    * @param startDate Optional start date to add the days against. If set to null, it will use the current day as a reference.
    * @param daysToAdd
    * @return
    */
   public static Period daysToPeriod(LocalDate startDate, long daysToAdd) {
      LocalDate beginDate = startDate == null ? LocalDate.now() : startDate;
      LocalDate addedDate = LocalDate.of(beginDate.getYear(), beginDate.getMonth(), beginDate.getDayOfMonth());
      beginDate.plusDays(daysToAdd);
      return Period.between(beginDate, addedDate);
   }

   public static String dateRangeToString(IDateRange dr) {
      StringBuilder sb = new StringBuilder();
      sb.append(dateToString(dr.getStartDate())).append(" - ").append(dateToString(dr.getEndDate()));
      return sb.toString();
   }

   public static String dateToString(LocalDate date) {
      return dateToString(date, DFLT_DATE_FORMAT);
   }

   public static String dateToString(LocalDate date, DateTimeFormatter dateFormat) {
      if (dateFormat == null) {
         dateFormat = DFLT_DATE_FORMAT;
      }
      return date.format(dateFormat);
   }

   public static boolean isEndDateEqualToOrAfterStartDate(IDateRange dr) {
      return dr.getEndDate().equals(dr.getStartDate()) || dr.getEndDate().isAfter(dr.getStartDate());
   }

   public static long getTimeDifferenceInSec(IDateRange dr) {
      return getTimeDifferenceInSec(dr.getStartDate(), dr.getEndDate());
   }

   public static long getTimeDifferenceInSec(LocalDate date1, LocalDate date2) {
      Duration duration = Duration.between(date1, date2);
      return duration.toSeconds();
   }
}
