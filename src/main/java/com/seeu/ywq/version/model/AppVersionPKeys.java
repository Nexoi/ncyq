package com.seeu.ywq.version.model;

import java.io.Serializable;

public class AppVersionPKeys implements Serializable {
    private Integer version;
    private AppVersion.CLIENT client;

    public AppVersionPKeys() {
    }

    public AppVersionPKeys(Integer version, AppVersion.CLIENT client) {
        this.version = version;
        this.client = client;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public AppVersion.CLIENT getClient() {
        return client;
    }

    public void setClient(AppVersion.CLIENT client) {
        this.client = client;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((client == null) ? 0 : client.hashCode());
        result = PRIME * result + ((version == null) ? 0 : version.hashCode());
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

        final AppVersionPKeys other = (AppVersionPKeys) obj;
        if (client == null) {
            if (other.client != null) {
                return false;
            }
        } else if (!client.equals(other.client)) {
            return false;
        }
        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }

        return true;
    }
}
