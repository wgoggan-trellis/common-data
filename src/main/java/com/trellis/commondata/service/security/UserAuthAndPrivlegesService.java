package com.trellis.commondata.service.security;

import com.trellis.commondata.dto.AccessRightsResponse;
import com.trellis.commondata.model.SecUser;
import com.trellis.commondata.repository.SecUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class UserAuthAndPrivlegesService implements IUserAuthAndPrivlegesService {

   @Autowired
   private SecUserRepository secUserRepository;

   public AccessRightsResponse getAccessRights(CsrfToken csrfToken) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      AccessRightsResponse accessRightsResponse = new AccessRightsResponse();
      accessRightsResponse.setCsrfToken(csrfToken.getToken());

      if (!authentication.getName().equals("anonymousUser")) {
         DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
         accessRightsResponse.setCsrfToken(csrfToken.getToken());
         accessRightsResponse.setName(userDetails.getAttribute("name"));
         accessRightsResponse.setUsername(userDetails.getAttribute("preferred_username"));
         accessRightsResponse.setAdmin(true);
      }
      Iterable<SecUser> sUser = secUserRepository.findAll();

      return accessRightsResponse;
   }

   public boolean hasPrivilege(Authentication auth, String targetType, String permission) {
      for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
         if (grantedAuth.getAuthority().startsWith(targetType)) {
            if (grantedAuth.getAuthority().contains(permission)) {
               return true;
            }
         }
      }
      return false;
   }


}
