package com.seeu.ywq.release.model;

import java.io.Serializable;

/**
 * 复合主键策略，多关联
 */
public class PublishLikedUserPKeys implements Serializable {
    private Long uid;
    private Long publishId;

    public PublishLikedUserPKeys(){}

    public PublishLikedUserPKeys(Long publishId, Long uid) {
        this.publishId = publishId;
        this.uid = uid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
    }

    //  ***重写hashCode与equals方法***  划重点！
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((uid == null) ? 0 : uid.hashCode());
        result = PRIME * result + ((publishId == null) ? 0 : publishId.hashCode());
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

        final PublishLikedUserPKeys other = (PublishLikedUserPKeys) obj;
        if (uid == null) {
            if (other.uid != null) {
                return false;
            }
        } else if (!uid.equals(other.uid)) {
            return false;
        }
        if (publishId == null) {
            if (other.publishId != null) {
                return false;
            }
        } else if (!publishId.equals(other.publishId)) {
            return false;
        }
        return true;
    }
}
