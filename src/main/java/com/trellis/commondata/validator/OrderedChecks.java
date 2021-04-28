package com.trellis.commondata.validator;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * Collective grouper class used by @Validated 
 * Example public ResponseEntity<ResponseEntity> getContracts(@Validated(OrderedChecks.class) @RequestBody DateRangeRequest requestBody)
 */
@GroupSequence(value = {Default.class, ClassChecks.class})
public interface OrderedChecks {
}
