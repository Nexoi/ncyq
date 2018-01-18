package com.seeu.ywq.user.model;

import java.io.Serializable;

public class UserLikePKeys implements Serializable {
    private Long uid;
    private Long likedUid;

    public UserLikePKeys(Long uid, Long likedUid) {
        this.uid = uid;
        this.likedUid = likedUid;
    }

    public UserLikePKeys() {
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getLikedUid() {
        return likedUid;
    }

    public void setLikedUid(Long likedUid) {
        this.likedUid = likedUid;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((uid == null) ? 0 : uid.hashCode());
        result = PRIME * result + ((likedUid == null) ? 0 : likedUid.hashCode());
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

        final UserLikePKeys other = (UserLikePKeys) obj;
        if (uid == null) {
            if (other.uid != null) {
                return false;
            }
        } else if (!uid.equals(other.uid)) {
            return false;
        }
        if (likedUid == null) {
            if (other.likedUid != null) {
                return false;
            }
        } else if (!likedUid.equals(other.likedUid)) {
            return false;
        }
        return true;
    }
}
