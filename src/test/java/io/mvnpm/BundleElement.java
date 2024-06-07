package io.mvnpm;

import java.util.ArrayList;
import java.util.List;

import static io.mvnpm.Html.*;

public class BundleElement extends Element {

    //@Inject
    Mapping mapping = new Mapping();


    public String key = "main";

    public String tag = "both";

    @Override
    public Html render() {
        List<Html> tags = new ArrayList<>();
        if (tag.equals("script") || tag.equals("both")) {
            final String script = mapping.script(key);
            if(script != null) {
                // language=html
                tags.add(raw("<script type=\"module\" src=\"%s\"></script>".formatted(script)));
            } else {
                // language=html
                tags.add(raw("<!-- no script found for key '{key}' in Bundler mapping !-->"));
            }

        }
        if (tag.equals("style") || tag.equals("both")) {
            final String style = mapping.style(key);
            if(style != null) {
                // language=html
                tags.add(raw("<script type=\"module\" src=\"%s\"></script>".formatted(style)));
            } else {
                // language=html
                tags.add(raw("<!-- no script found for key '{key}' in Bundler mapping !-->"));
            }

        }
        return html(tags);
    }

    public record Mapping(

    ) {

        public String script(String key) {
            return "/static/bundle/" + key + "-ZKDZJE.js";
        }

        public String style(String key) {
            return "/static/bundle/" + key + "-ZZJFNZ.css";
        }

    }
}
