package com.vmware.sdugar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by sourabhdugar on 6/8/16.
 */
@JsonTypeName("2")
public class CeramicCup extends Cup {

    public String getClazz() {
        return this.getClass().getName();
    }

    @Override
    //@JsonProperty(value = "id")
    public String getId() {
        return "2";
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
        return "ceramic";
    }
}
