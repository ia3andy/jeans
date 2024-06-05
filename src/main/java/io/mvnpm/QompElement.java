package io.mvnpm;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static io.mvnpm.Html.html;

public class QompElement {

    final Map<String, Object> properties = new HashMap<>();

    private String slot;

    public <T> void property(String name, T defaultValue) {
        properties.put(name, defaultValue);
    }


    public String render() {
        return "";
    }

    public <T> T getProperty(String name) {
        return (T) properties.get(name);
    }

    public String slot() {
        return slot;
    }

    public QompElement setSlot(String slot) {
        this.slot = slot;
        return this;
    }

    static class TagElement extends QompElement {
        private final String name;

        public TagElement(String name) {
            this.name = name;
        }

        @Override
        public String render() {
            StringBuilder builder = new StringBuilder("<" + name );
            if (!properties.isEmpty()) {
                builder.append(" ");
                builder.append(properties.entrySet().stream().map(e -> e.getKey() + "=\"" + e.getValue() + "\"" ).collect(Collectors.joining(" ")));
            }
            if(slot() == null) {
                return
                        builder.toString() + "/>";
            }
            return builder.toString() + ">" + html(slot()) + "</" + name + ">";
        }
    }

}
