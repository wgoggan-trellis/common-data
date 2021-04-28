package com.trellis.commondata;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "commondataEM",
        basePackages = {"com.trellis.commondata.repository"})
public class CommonDataConfig {

   @Bean(name = "commondataDS")
   @ConfigurationProperties(prefix = "spring.commondatadb")
   public DataSource commondataDataSource() {
      return DataSourceBuilder.create().build();
   }

   @Bean(name = "commondataEM")
   public LocalContainerEntityManagerFactoryBean storingEntityManagerFactory(
           EntityManagerFactoryBuilder builder, @Qualifier("commondataDS") DataSource ds) {
      return builder
              .dataSource(ds)
              .packages("com.trellis.commondata")
              .persistenceUnit("commondataPU")
              .build();
   }
}
