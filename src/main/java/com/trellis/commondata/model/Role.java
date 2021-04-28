package com.trellis.commondata.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "SEC_RL")
public class Role {
   @Id
   @Column(name = "SEC_RL_ID", nullable = false)
   private Long id;

   @Column(name = "SEC_RL_DSC")
   private String description;

   @Column(name = "SEC_RL_NM")
   private String name;

   @JoinTable(name = "SEC_RL_SEC_BUS_WRKFLW",
           joinColumns = @JoinColumn(name = "SEC_RL_ID",
                   referencedColumnName = "SEC_RL_ID"
           ),
           inverseJoinColumns = @JoinColumn(name = "SEC_BUS_WRKFLW_ID",
                   referencedColumnName = "SEC_BUS_WRKFLW_ID")
   )
   @OneToMany(fetch = FetchType.EAGER)
   private Set<Privlege> privleges;

}
