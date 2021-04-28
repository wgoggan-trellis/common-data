package com.trellis.commondata.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "SEC_BUS_WRKFLW")
public class Privlege {

   @Id
   @Column(name = "SEC_BUS_WRKFLW_ID", nullable = false)
   private Long id;

   @Column(name = "SEC_BUS_WRKFLW_DSC")
   private String description;

   @Column(name = "SEC_BUS_WRKFLW_NM")
   private String name;

}
