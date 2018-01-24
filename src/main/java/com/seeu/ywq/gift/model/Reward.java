package com.seeu.ywq.gift.model;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ywq_reward")
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiParam(hidden = true)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String iconUrl;
    @Min(0)
    @NotNull
    private Long diamonds;
    @NotNull
    private Integer sortId;

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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Long getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(Long diamonds) {
        this.diamonds = diamonds;
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }
}
