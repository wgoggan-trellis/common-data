package com.trellis.commondata.validator;

import com.trellis.commondata.common.util.CollectionUtil;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class ValidationUtil {
   /**
    * Compose a Spring Boot validation error message via using the message bundle key.
    *
    * @param constraintValidatorContext
    * @param msgBundleKey
    * @param msgBundleParamValueMap Optional message bundle map for parameter key and value pair. Set to null if not used.
    */
   public static void composeErrMsg(ConstraintValidatorContext constraintValidatorContext, String msgBundleKey, Map<String,String> msgBundleParamValueMap) {
      HibernateConstraintValidatorContext hibernateContext = constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);
      hibernateContext.disableDefaultConstraintViolation();
      if (!CollectionUtil.isMapEmpty(msgBundleParamValueMap)) {
         msgBundleParamValueMap.forEach((k, v) -> {
            hibernateContext.addMessageParameter(k, msgBundleParamValueMap.get(k));
         });
      }
      hibernateContext.buildConstraintViolationWithTemplate("{" + msgBundleKey + "}").addConstraintViolation();
   }
}
