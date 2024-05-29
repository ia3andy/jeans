package io.mvnpm;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Html {
    private static final Pattern TAG_PATTERN = Pattern.compile("<([^/>\\s]+)([^>]*)(>(.*?)(</\\1>)|/>)", Pattern.DOTALL);

    public static String html(QuteElementRegistry registry, String content) {
        final List<Tag> tags = extractFirstLevelTags(content);
        StringBuilder builder = new StringBuilder();
        if(tags.isEmpty()) {
            return content;
        }
        for (Tag tag : tags) {
            final QuteElement element = registry.getOrFallback(tag.name);
            element.setRegistry(registry);
            element.properties.putAll(tag.attributes);
            element.content = tag.content;
            builder.append(element.render());
        }
        return builder.toString();
    }


    public static List<Tag> extractFirstLevelTags(String content) {
        Matcher matcher = TAG_PATTERN.matcher(content);
        List<Tag> tags = new LinkedList<>();
        while (matcher.find()) {
            String tagName = matcher.group(1);
            String attributes = matcher.group(2);
            String tagContent = matcher.group(4);
            Map<String, String> attributeMap = parseAttributes(attributes);

            tags.add(new Tag(tagName, attributeMap, tagContent));
        }
        return tags;
    }

    private static Map<String, String> parseAttributes(String attributes) {
        Map<String, String> attributeMap = new HashMap<>();
        Matcher attributeMatcher = Pattern.compile("(\\w+)=\"([^\"]*)\"").matcher(attributes);
        while (attributeMatcher.find()) {
            attributeMap.put(attributeMatcher.group(1), attributeMatcher.group(2));
        }
        return attributeMap;
    }

    static record Tag(String name, Map<String, String> attributes, String content) {}

}
