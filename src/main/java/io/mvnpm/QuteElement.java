package io.mvnpm;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QuteElement {

    final Map<String, Object> properties = new HashMap<>();

    protected String content;

    private QuteElementRegistry registry;

    public <T> void property(String name, T defaultValue) {
        properties.put(name, defaultValue);
    }

    void setRegistry(QuteElementRegistry registry) {
        this.registry = registry;
    }

    String html(String content) {
        return this.registry.html(content);
    }

    public <T> T getProperty(String name) {
        return (T) properties.get(name);
    }

    public String render() {
        return "";
    }

    static class TagElement extends QuteElement {
        private final String name;

        public TagElement(String name) {
            this.name = name;
        }

        @Override
        public String render() {
            String start = "<" + name + " " +  properties.entrySet().stream().map(e -> e.getKey() + "=\"" + e.getValue() + "\"" ).collect(Collectors.joining(" "));
            if(content == null) {
                return
                        start + "/>";
            }
            return start + ">" + html(content) + "</" + name + ">";
        }
    }

}
