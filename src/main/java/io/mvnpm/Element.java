package io.mvnpm;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static io.mvnpm.Html.HTML;
import static io.mvnpm.Html.TEXT;

public class Element {

    final Map<String, String> properties = new HashMap<>();

    private String slot;
    private String rawElement;

    public void property(String name, String defaultValue) {
        properties.put(name, defaultValue);
    }

    /**
     * Should render this element content
     */
    public Html render() {
        return Html.empty();
    }

    /**
     * Get String properties by name.
     * Properties can also be declared as fields
     */
    public String getProperty(String name) {
        return properties.get(name);
    }

    /**
     * @return the inner content of this element
     */
    public String slot() {
        return slot;
    }

    public Element setSlot(String slot) {
        this.slot = slot;
        return this;
    }

    public String rawElement() {
        return rawElement;
    }

    public Element setRawElement(String rawElement) {
        this.rawElement = rawElement;
        return this;
    }

    /**
     * Element used for tags (<div>, <em>, ...)
     */
    static class TagElement extends Element {
        private final String name;

        public TagElement(String name) {
            this.name = name;
        }

        @Override
        public Html render() {
            if (slot() == null) {
                return TEXT(rawElement());
            }
            StringBuilder builder = new StringBuilder("<" + name);
            if (!properties.isEmpty()) {
                builder.append(" ");
                builder.append(properties.entrySet().stream().map(e -> e.getKey() + "=\"" + e.getValue() + "\"").collect(Collectors.joining(" ")));
            }
            return TEXT(builder.append(">").append(HTML(slot())).append( "</").append( name).append(">").toString());
        }
    }

}
