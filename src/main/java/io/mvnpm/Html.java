package io.mvnpm;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Html {

    private final String content;
    private final Html[] tags;

    private static final Html EMPTY = new Html();

    private static final Pattern TAG_PATTERN = Pattern.compile("<([^/>\\s]+)([^>]*)(>(.*)(</\\1>)|/>)", Pattern.DOTALL);


    private Html() {
        this.content = null;
        this.tags = null;
    }

    private Html(String content) {
       this.content = content;
       this.tags =  null;
    }

    private Html(Html[] tags) {
        this.tags = tags;
        this.content = null;
    }

    public static Html empty() {
        return EMPTY;
    }

    public boolean isEmpty() {
        return content == null && tags == null;
    }

    @Override
    public String toString() {
        if(content != null) {
            return content;
        }
        if(tags == null) {
            return "";
        }
        return Arrays.stream(tags).map(Html::toString).collect(Collectors.joining());
    }

    /**
     * Content will be passed as is, it won't be parsed to resolve elements
     *
     * @param content raw html content
     * @return
     */
    public static Html raw(String content) {
        return new Html(content);
    }

    /**
     * Array of Html tags will be concatenated together
     *
     * @param tags an array of Html tags
     * @return a new Html for the list
     */
    public static Html html(Html[] tags) {
        return new Html(tags);
    }

    /**
     * List of Html tags will be concatenated together
     *
     * @param tags a list of Html tags
     * @return a new Html for the list
     */
    public static Html html(List<Html> tags) {
        return new Html(tags.toArray(new Html[0]));
    }

    /**
     * The content will be processed and elements will be resolved
     * @param content the content to be processed
     * @return the processed Html
     */
    public static Html html(String content) {
        return parse(JeansElementRegistry.instance(), content);
    }

    public static Html html(JeansElementRegistry registry, String content) {
        return parse(registry, content);
    }

    /**
     * To be used to pass a typed attribute to another element
     *
     * FIXME: This option is fragile and needs to be replaced someway
     *
     * @param attribute the attribute to pass
     * @return a placeholder for the value
     * @param <T>
     */
    public static <T> String typed(T attribute) {
        return JeansAttributesRegistry.instance().register(attribute);
    }

    private static Html parse(JeansElementRegistry registry, String content) {
        final List<ParsedTag> parsedTags = extractFirstLevelTags(content);
        List<Html> tags = new ArrayList<>(parsedTags.size());
        if (parsedTags.isEmpty()) {
            return Html.raw(content);
        }
        for (ParsedTag tag : parsedTags) {
            final Element element = registry.getOrFallback(tag.name);
            element.properties.putAll(tag.attributes);
            for (Field field : element.getClass().getFields()) {
                if (tag.attributes.containsKey(field.getName())) {
                    try {
                        final String value = tag.attributes.get(field.getName());
                        if (String.class.isAssignableFrom(field.getType())) {
                            field.set(element, value);
                        } else {
                            final Object read = JeansAttributesRegistry.instance().read(value);
                            if (field.getType().isAssignableFrom(read.getClass())) {
                                field.set(element, read);
                            } else {
                                throw new IllegalStateException("Invalid type for attribute: <" + tag.name + " " + field.getName() + "=\""+field.getType().getName()+"\">, Found: " + read.getClass().getName());
                            }
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            element.setSlot(tag.inner);
            element.setRawElement(tag.rawTag);
            tags.add(element.render());
        }
        return Html.html(tags);
    }


    public static List<ParsedTag> extractFirstLevelTags(String content) {
        Matcher matcher = TAG_PATTERN.matcher(content);
        List<ParsedTag> tags = new LinkedList<>();
        while (matcher.find()) {
            String rawTag = matcher.group(0);
            String tagName = matcher.group(1);
            String attributes = matcher.group(2);
            String tagContent = matcher.group(4);
            if (tagContent == null || tagContent.isEmpty()) {
                tagContent = null;
            }
            Map<String, String> attributeMap = parseAttributes(attributes);

            tags.add(new ParsedTag(rawTag, tagName, attributeMap, tagContent));
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

    static record ParsedTag(String rawTag, String name, Map<String, String> attributes, String inner) {}

}
