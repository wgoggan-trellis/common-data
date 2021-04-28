package com.trellis.commondata.common.util;

import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MessageSourceUtil {
   /**
    * @see MessageSourceUtil#getMsg(MessageSource, String, Map)
    */
   public static String getMsg(MessageSource messageSource, String msgKey) {
      return getMsg(messageSource, msgKey, null);
   }

   /**
    * @see MessageSourceUtil#getMsg(MessageSource, String, Map, Locale)
    */
   public static String getMsg(MessageSource messageSource, String msgKey, Map<String,String> msgBundleParamValueMap) {
      return MessageSourceUtil.getMsg(messageSource, msgKey, msgBundleParamValueMap);
   }

   /**
    * Get message from message source.
    *
    * Sample usage:
    *
    * <code>
    *    Map<String,String> map = new HashMap();
    *    map.put("param1", "value of param 1");
    *    map.put("param2", "value of param 2");
    *    String str = MessageSourceUtil.getMsg(messageSource, "this.is.the.key", map, null);
    * </code>
    *
    * @param messageSource
    * @param msgKey
    * @param msgBundleParamValueMap Optional parameter map that contains key/value entries for each named parameter used in the message key.
    * @param locale Optional locale, set to null to use the default locale.
    * @return The retrieved message with parameters populated (if any).
    */
   public static String getMsg(MessageSource messageSource, String msgKey, Map<String,String> msgBundleParamValueMap, Locale locale) {
      String msg = messageSource.getMessage(msgKey, null, locale == null ? Locale.getDefault() : locale);
      if (!StringUtil.isStringEmpty(msg) && !CollectionUtil.isMapEmpty(msgBundleParamValueMap)) {
         Set<String> keys = msgBundleParamValueMap.keySet();
         for (String key : keys) {
            msg = msg.replaceAll(composeMessageSourceParam(key), msgBundleParamValueMap.get(key));
         }
      }
      return msg;
   }

   private static String composeMessageSourceParam(String msgKey) {
      return  (new StringBuilder()).append("\\{").append(msgKey).append("\\}").toString();
   }
}
