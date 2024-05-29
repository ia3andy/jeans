package io.mvnpm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QuteElementRegistry {

    private final Map<String, QuteElement> elements = new ConcurrentHashMap<>();

    public void define(final String name, final QuteElement element) {
        elements.put(name, element);
    }

    public String html(String content) {
        return Html.html(this, content);
    }

    public QuteElement getOrFallback(String name) {
        if (!elements.containsKey(name)) {
            elements.put(name, new QuteElement.TagElement(name));
        }
        return elements.get(name) ;
    }
}
