package com.crawl.VietCap.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
    private static Gson gson = new Gson();

    public static String getJsonResponseAsString(String result, String content) {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("result", result);
        responseMap.put("content", content);
        return gson.toJson(responseMap);
    }

    public static JsonObject getJsonAsObject(String jsonString) {
        return JsonParser.parseString(jsonString).getAsJsonObject();
    }

    public static String getJsonValue(JsonObject jsonObject, String key) {
        return jsonObject.get(key).getAsString();
    }

    public static String getMapToStringJson(Object object) {
        Type gsonType = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        return gson.toJson(object, gsonType);
    }
}
