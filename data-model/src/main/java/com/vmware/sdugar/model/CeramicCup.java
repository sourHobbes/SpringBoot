package com.vmware.sdugar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

/**
 * Created by sourabhdugar on 6/8/16.
 */
@JsonTypeIdResolver(CommandTypeIdResolver.class)
public class CeramicCup extends Cup {

    public String getClazz() {
        return this.getClass().getName();
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
