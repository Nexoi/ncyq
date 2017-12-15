package com.seeu.ywq.userlogin.model;

import io.swagger.annotations.ApiParam;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 权限分配角色表
 * <p>
 * Created by neo on 25/09/2017.
 */
@Entity
@Table(name = "user_auth_role")
@DynamicUpdate
public class UserAuthRole {

    @ApiParam(hidden = true)
    @Id
    @GeneratedValue
    private Long id;

    @ApiParam(hidden = true)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
