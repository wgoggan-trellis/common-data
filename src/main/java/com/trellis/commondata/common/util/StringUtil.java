package com.trellis.commondata.common.util;

public class StringUtil {
   public static final String CURLY_BRACKET_LEFT   = "{";
   public static final String CURLY_BRACKET_RIGHT  = "}";
   public static final String PERIOD               = ".";
   public static final String UNDERSCORE           = "_";

   public static boolean isStringEmpty(String str) {
      return (str == null || str.length() == 0) ? true : false;
   }
}
