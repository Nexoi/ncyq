package com.seeu.ywq.release.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;

/**
 * 关注/粉丝表 PK
 */
public class FansPKeys implements Serializable {
    private Long fansUid;//用户账号

    private Long followedUid;//被关注的用户账号

    public Long getFansUid() {
        return fansUid;
    }

    public void setFansUid(Long fansUid) {
        this.fansUid = fansUid;
    }

    public Long getFollowedUid() {
        return followedUid;
    }

    public void setFollowedUid(Long followedUid) {
        this.followedUid = followedUid;
    }

    public FansPKeys() {
    }

    public FansPKeys(Long fansUid, Long followedUid) {
        this.fansUid = fansUid;
        this.followedUid = followedUid;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((fansUid == null) ? 0 : fansUid.hashCode());
        result = PRIME * result + ((followedUid == null) ? 0 : followedUid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final FansPKeys other = (FansPKeys) obj;
        if (fansUid == null) {
            if (other.fansUid != null) {
                return false;
            }
        } else if (!fansUid.equals(other.fansUid)) {
            return false;
        }
        if (followedUid == null) {
            if (other.followedUid != null) {
                return false;
            }
        } else if (!followedUid.equals(other.followedUid)) {
            return false;
        }
        return true;
    }
}
