package com.seeu.ywq.user.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;

/**
 * 认证信息列表
 */
@Entity
@Table(name = "identification")
public class Identification {
    @ApiParam(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiParam(hidden = true)
    @Column(length = 20)
    private String identificationName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificationName() {
        return identificationName;
    }

    public void setIdentificationName(String identificationName) {
        this.identificationName = identificationName;
    }
}
