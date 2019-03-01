package com.zzz.myapp.util.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JsonExclusionStrategy implements ExclusionStrategy {

    private final Class<?> typeToSkip;

    public JsonExclusionStrategy() {
        this.typeToSkip = null;
    }

    public JsonExclusionStrategy(Class<?> typeToSkip) {
        this.typeToSkip = typeToSkip;
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return (clazz == typeToSkip);
    }

    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(NotSerialize.class) != null;
    }

}
