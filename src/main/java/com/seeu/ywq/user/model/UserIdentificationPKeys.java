package com.seeu.ywq.user.model;

import java.io.Serializable;

public class UserIdentificationPKeys implements Serializable {

    private Long identificationId;
    private Long uid;

    public Long getIdentificationId() {
        return identificationId;
    }

    public void setIdentificationId(Long identificationId) {
        this.identificationId = identificationId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((uid == null) ? 0 : uid.hashCode());
        result = PRIME * result + ((identificationId == null) ? 0 : identificationId.hashCode());
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

        final UserIdentificationPKeys other = (UserIdentificationPKeys) obj;
        if (uid == null) {
            if (other.uid != null) {
                return false;
            }
        } else if (!uid.equals(other.uid)) {
            return false;
        }
        if (identificationId == null) {
            if (other.identificationId != null) {
                return false;
            }
        } else if (!identificationId.equals(other.identificationId)) {
            return false;
        }
        return true;
    }
}
