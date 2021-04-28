package com.trellis.commondata.repository;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Filter {
   private String field;
   private QueryOperator operator;
   private String value;
   private List<String> values;
}
