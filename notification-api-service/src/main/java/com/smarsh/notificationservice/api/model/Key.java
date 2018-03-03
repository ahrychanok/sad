package com.smarsh.notificationservice.api.model;

import com.smarsh.notificationservice.client.model.TemplateType;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Class that will be used as cache map key.
 *
 * @author Dzmitry_Sulauka
 */
public class Key {

    private TemplateType type;

    private Integer clientId;

    private String guid;

    private boolean isDefault;

    public Key() {
    }

    public Key(TemplateType type) {
        this.type = type;
    }

    public Key(TemplateType type, Integer clientId) {
        this.type = type;
        this.clientId = clientId;
    }

    public Key(TemplateType type, Integer clientId, String guid) {
        this.type = type;
        this.clientId = clientId;
        this.guid = guid;
    }

    public Key(TemplateType type, String guid) {
        this.type = type;
        this.guid = guid;
    }

    public Key(TemplateType type, boolean isDefault) {
        this.type = type;
        this.isDefault = isDefault;
    }

    public Key(TemplateType type, Integer clientId, boolean isDefault) {
        this.type = type;
        this.clientId = clientId;
        this.isDefault = isDefault;
    }

    public Key(TemplateType type, Integer clientId, String guid, boolean isDefault) {
        this.type = type;
        this.clientId = clientId;
        this.guid = guid;
        this.isDefault = isDefault;
    }

    public TemplateType getType() {
        return type;
    }

    public void setType(TemplateType type) {
        this.type = type;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + (isDefault ? 1 : 0);
        result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
        result = prime * result + ((guid == null) ? 0 : guid.hashCode());
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
        Key other = (Key) obj;
        if (type != null && !type.equals(other.type)) {
            return false;
        }
        if (clientId == null) {
            if (other.clientId != null) {
                return false;
            }
        } else if (!clientId.equals(other.clientId)) {
            return false;
        }
        if (guid == null) {
            return other.guid == null;
        } else {
            return guid.equals(other.guid);
        }

    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
