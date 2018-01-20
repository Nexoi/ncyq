package com.seeu.ywq.skill.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by suneo.
 * User: neo
 * Date: 20/01/2018
 * Time: 6:38 PM
 * Describe:
 * 目前暂定所有的技能都一个价格（针对某一个用户）
 */
@Entity
@Table(name = "ywq_user_skill_price")
public class UserSkillPrice {
    @Id
    private Long uid;
    @NotNull
    @Min(0)
    private Long price;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
