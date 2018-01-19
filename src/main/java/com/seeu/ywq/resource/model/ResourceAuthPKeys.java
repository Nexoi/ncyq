package com.seeu.ywq.resource.model;

import java.io.Serializable;

public class ResourceAuthPKeys implements Serializable {
    private Long resourceId;
    private Long uid;
    private ResourceAuth.TYPE type;

    public ResourceAuthPKeys() {
    }

    public ResourceAuthPKeys(Long uid, ResourceAuth.TYPE type, Long resourceId) {
        this.uid = uid;
        this.type = type;
        this.resourceId = resourceId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceAuth.TYPE getType() {
        return type;
    }

    public void setType(ResourceAuth.TYPE type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((uid == null) ? 0 : uid.hashCode());
        result = PRIME * result + ((resourceId == null) ? 0 : resourceId.hashCode());
        result = PRIME * result + ((type == null) ? 0 : type.hashCode());
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

        final ResourceAuthPKeys other = (ResourceAuthPKeys) obj;
        if (uid == null) {
            if (other.uid != null) {
                return false;
            }
        } else if (!uid.equals(other.uid)) {
            return false;
        }
        if (resourceId == null) {
            if (other.resourceId != null) {
                return false;
            }
        } else if (!resourceId.equals(other.resourceId)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
