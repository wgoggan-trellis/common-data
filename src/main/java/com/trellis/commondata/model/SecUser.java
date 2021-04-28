package com.trellis.commondata.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "SEC_USR")
public class SecUser implements CommonDataModel, Serializable {
   @Id
   @Column(name = "SEC_USR_ID", nullable = false)
   private Long id;

   @Column(name = "ACTV_IND", nullable = false)
   private Long activeInd;

   @Column(name = "DFLT_LOCALE_ID", nullable = false)
   private Long defaultLocaleId;

   @Column(name = "DFLT_MNG_PPL_ID", nullable = false)
   private Long defaultMngPplId;

   @Column(name = "SEC_USR_FRST_NM")
   private String firstName;

   @Column(name = "SEC_USR_INTL_IND", nullable = false)
   private String intlInd;

   @Column(name = "SEC_USR_LOGIN_CD", nullable = false)
   private String loginCode;

   @Column(name = "SEC_USR_LST_NM")
   private String lastName;

   @Column(name = "SEC_USR_MID_NM")
   private String middleName;

   @JoinTable(name = "SEC_RL_ASSOC",
         joinColumns = @JoinColumn(name = "SEC_USR_ID",
            referencedColumnName = "SEC_USR_ID"
         ),
         inverseJoinColumns = @JoinColumn(name = "SEC_RL_ID",
         referencedColumnName = "SEC_RL_ID")
   )
   @OneToMany(fetch = FetchType.EAGER)
   private Set<Role> roles;


}

