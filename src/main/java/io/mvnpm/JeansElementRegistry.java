package io.mvnpm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class JeansElementRegistry {

    private final Map<String, Supplier<Element>> elements = new ConcurrentHashMap<>();
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    private static JeansElementRegistry INSTANCE;

    public void define(final String name, final Supplier<Element> elementFactory) {
        elements.put(name, elementFactory);
    }

    public <T extends Element> void define(final Class<T> clazz) {
        try {
            final Constructor<T> constructor = clazz.getConstructor();
            elements.put(camelToKebab(clazz.getSimpleName().replace("Element", "")), () -> {
                try {
                    return constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Default constructor not found in Element:" + clazz , e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static String camelToKebab(String camel) {
        return camel.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public void reset() {
        attributes.clear();
    }

    public Element getOrFallback(String name) {
        if (!elements.containsKey(name)) {
            elements.put(name, () -> new Element.TagElement(name));
        }
        return elements.get(name).get();
    }

    public static JeansElementRegistry instance() {
        if (INSTANCE == null) {
            INSTANCE = new JeansElementRegistry();
        }
        return INSTANCE;
    }

}
