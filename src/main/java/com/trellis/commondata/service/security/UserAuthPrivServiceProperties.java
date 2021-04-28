package com.trellis.commondata.service.security;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "commondata")
public class UserAuthPrivServiceProperties {
   private String datasourceUrl;
   private String datasourceDriver;
   private String datasourceUsername;
   private String datasourcePassword;

}
