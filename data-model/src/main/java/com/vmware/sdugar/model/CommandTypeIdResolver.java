package com.vmware.sdugar.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CommandTypeIdResolver implements TypeIdResolver {
    private JavaType baseType = null;

    @Override
    public void init(JavaType javaType) {
        baseType = javaType;
    }

    @Override
    public String idFromValue(Object o) {
        if (o instanceof CeramicCup) {
            return "2";
        } else if (o instanceof GlassCup) {
            return "1";
        } else {
            throw new IllegalArgumentException("Unknown type");
        }
    }

    @Override
    public String idFromValueAndType(Object o, Class<?> aClass) {
        if (o instanceof CeramicCup) {
            return "1";
        } else if (o instanceof GlassCup) {
            return "2";
        } else {
            throw new IllegalArgumentException("Unknown type");
        }
    }

    @Override
    public String idFromBaseType() {
        return null;
    }

    @Override
    public JavaType typeFromId(DatabindContext ctx, String s) {
        if (s.equals("2")) {
            return TypeFactory.defaultInstance().constructSpecializedType(baseType, GlassCup.class);
        } else if (s.equals("1")) {
            return TypeFactory.defaultInstance().constructSpecializedType(baseType, CeramicCup.class);
        } else {
            throw new IllegalArgumentException("Unknown type");
        }
    }

    @Override
    public String getDescForKnownTypeIds() {
        return "unknown type";
    }

    @Override
    public JsonTypeInfo.Id getMechanism()
    {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
