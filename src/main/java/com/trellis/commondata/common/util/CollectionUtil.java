package com.trellis.commondata.common.util;

import java.util.Map;

public class CollectionUtil {
   public static boolean isMapEmpty(Map map) {
      return (map == null || map.isEmpty()) ? true : false;
   }
}
