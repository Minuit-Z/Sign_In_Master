package com.zjmy.signin.utils.files;

import java.util.List;


public abstract class Json {
    private static Json json;

    Json() {
    }

    public static Json get() {
        if (json == null) {
            json = new GsonUtils();
        }
        return json;
    }

    public abstract String toJson(Object src);

    public abstract <T> T toObject(String json, Class<T> claxx);

    public abstract <T> List<T> toList(String json, Class<T[]> claxx);
}
