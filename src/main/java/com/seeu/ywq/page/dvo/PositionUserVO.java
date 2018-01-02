package com.seeu.ywq.page.dvo;

import com.seeu.ywq.resource.model.Image;

import java.math.BigDecimal;
import java.util.List;

public class PositionUserVO {
    private Long uid;
    private String nickname;
    private String headIconUrl;
    private BigDecimal distance;
    private List<Long> identifications;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public List<Long> getIdentifications() {
        return identifications;
    }

    public void setIdentifications(List<Long> identifications) {
        this.identifications = identifications;
    }
}
