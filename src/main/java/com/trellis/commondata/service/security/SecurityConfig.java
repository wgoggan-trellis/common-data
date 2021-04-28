package com.trellis.commondata.service.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
   private final Log logger = LogFactory.getLog(WebSecurityConfigurerAdapter.class);

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      System.out.println("hello from SecurityConfig");
      http
              //.csrf().disable()
              .authorizeRequests()
              .antMatchers("/api/**").authenticated() // more specific rules go first
              .antMatchers("/iAmSecured").authenticated() // more specific rules go first
              .antMatchers("/**").permitAll()
              .and()
              .oauth2Login()
              .and()
              .httpBasic();

   }
}