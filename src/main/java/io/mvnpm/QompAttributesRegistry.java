package io.mvnpm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QompAttributesRegistry {

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    private static volatile QompAttributesRegistry INSTANCE;


    public static QompAttributesRegistry instance() {
        if (INSTANCE == null) {
            INSTANCE = new QompAttributesRegistry();
        }
        return INSTANCE;
    }

}
