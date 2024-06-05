package io.mvnpm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class QompElementRegistry {

    private final Map<String, Supplier<QompElement>> elements = new ConcurrentHashMap<>();
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    private static QompElementRegistry INSTANCE;

    public void define(final String name, final Supplier<QompElement> elementFactory) {
        elements.put(name, elementFactory);
    }

    public void reset() {
        attributes.clear();
    }

    public QompElement getOrFallback(String name) {
        if (!elements.containsKey(name)) {
            elements.put(name, () -> new QompElement.TagElement(name));
        }
        return elements.get(name).get();
    }

    public static QompElementRegistry instance() {
        if (INSTANCE == null) {
            INSTANCE = new QompElementRegistry();
        }
        return INSTANCE;
    }

}
