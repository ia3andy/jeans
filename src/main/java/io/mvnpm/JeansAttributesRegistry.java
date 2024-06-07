package io.mvnpm;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JeansAttributesRegistry {

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    private static volatile JeansAttributesRegistry INSTANCE;


    public String register(Object object) {
        final String key = UUID.randomUUID().toString();
        attributes.put(key, object);
        return key;
    }

    public Object read(String key) {
       return attributes.remove(key);
    }


    public static JeansAttributesRegistry instance() {
        if (INSTANCE == null) {
            INSTANCE = new JeansAttributesRegistry();
        }
        return INSTANCE;
    }

}
