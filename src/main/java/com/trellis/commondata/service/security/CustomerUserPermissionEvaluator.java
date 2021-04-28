package com.trellis.commondata.service.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public class CustomerUserPermissionEvaluator implements PermissionEvaluator {
    private final Log logger = LogFactory.getLog(CustomerUserPermissionEvaluator.class);
    @Autowired
    IUserAuthAndPrivlegesService userAuthAndPrivleges;

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if (auth == null) {
            return false;
        } else if ((targetDomainObject != null) && (permission instanceof String)) {
            String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();

            return hasPrivilege(auth, targetType, permission.toString().toUpperCase());
        } else if (permission instanceof String) {
            return hasPrivilege(auth, permission.toString().toUpperCase());
        } else {
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, targetType.toUpperCase(),
                permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
//  TODO: Wes to implement API call to verify that workflow is existing for TSP/USER
        return userAuthAndPrivleges.hasPrivilege(auth, targetType, permission);
    }

    private boolean hasPrivilege(Authentication auth, String permission) {
        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
            if (grantedAuth.getAuthority().contains(permission)) {
                logger.debug(String.format("User %s has permission %s.", auth.getName(), permission));
                return true;
            }
        }
        logger.debug(String.format("User %s does not have permission %s.", auth.getName(), permission));
        return false;
    }
}
