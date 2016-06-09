package com.vmware.sdugar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by sourabhdugar on 6/8/16.
 */
@JsonTypeName("1")
public class GlassCup extends Cup {
    @Override
    public String getId() {
        return "1";
    }

    @Override
    @JsonProperty(value = "lid")
    public boolean hasLid() {
        return false;
    }

    @Override
    @JsonProperty(value = "insulated")
    public boolean isInsulated() {
        return false;
    }

    @Override
    public String getBuildMaterial() {
        return "glass";
    }

    @Override
    public String getClazz() {
        return null;
    }
}
