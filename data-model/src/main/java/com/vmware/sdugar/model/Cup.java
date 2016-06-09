package com.vmware.sdugar.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

/**
 * Created by sourabhdugar on 6/8/16.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "id")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = GlassCup.class),
        @JsonSubTypes.Type(value = CeramicCup.class)})
@JsonTypeIdResolver(value = CommandTypeIdResolver.class)
public abstract class Cup {
    //@JsonProperty(value = "id")
    //abstract String getId();

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
        //gc.setId("2");
        ObjectMapper om = new ObjectMapper();
        Cup c = om.readValue(om.writeValueAsString(gc), Cup.class);
        System.out.println(om.writeValueAsString(gc));
    }
}
