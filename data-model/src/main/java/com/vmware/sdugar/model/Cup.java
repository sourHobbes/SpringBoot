package com.vmware.sdugar.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by sourabhdugar on 6/8/16.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "id")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = GlassCup.class, name = "1"),
        @JsonSubTypes.Type(value = CeramicCup.class, name = "2")})
public abstract class Cup {
    //@JsonProperty(value = "id")
    abstract String getId();

    @JsonProperty(value = "lid")
    abstract boolean hasLid();

    @JsonProperty(value = "insulated")
    abstract boolean isInsulated();

    @JsonIgnore
    abstract String getBuildMaterial();

    @JsonIgnore
    abstract String getClazz();

    void setLid(boolean lid) {}

    void setInsulated(boolean insulated) {}

    void setClazz(Class clazz) {}

    void setId(String id) {}

    public static void main(String[] args) throws IOException {
        Cup gc = new CeramicCup();
        gc.setClazz(CeramicCup.class);
        gc.setId("2");
        ObjectMapper om = new ObjectMapper();
        CeramicCup c = om.readValue(om.writeValueAsString(gc), CeramicCup.class);
        System.out.println(om.writeValueAsString(gc));
    }
}
