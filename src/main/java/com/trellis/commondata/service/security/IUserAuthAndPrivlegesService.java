package com.trellis.commondata.service.security;

import com.trellis.commondata.dto.AccessRightsResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;

public interface IUserAuthAndPrivlegesService {

   boolean hasPrivilege(Authentication auth, String targetType, String permission);

   AccessRightsResponse getAccessRights(CsrfToken csrfToken);

   }
