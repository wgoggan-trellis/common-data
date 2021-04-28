package com.trellis.commondata.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface CommonDataModel {
   @Retention(RetentionPolicy.RUNTIME)
   @interface GroupSum { }
}
