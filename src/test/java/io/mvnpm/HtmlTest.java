package io.mvnpm;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HtmlTest {


    @Test
    void testTags() {
        final QuteElementRegistry registry = new QuteElementRegistry();
        final String content = """
                    <div>
                        <h1> Hello World</h1>
                    </div>        
                """;
        assertThat(Html.html(registry, content)).isEqualToIgnoringWhitespace(content);
    }


    @Test
    void testMyElem() {
        final QuteElementRegistry registry = new QuteElementRegistry();
        registry.define("my-elem", new MyElem());
        final String content = """
                    <div class="something">
                        <my-elem />
                    </div>        
                """;
        assertThat(Html.html(registry, content)).isEqualToIgnoringWhitespace(
                """
                    <div class="something">
                        W00000T
                    </div>        
                """
        );
    }

    @Test
    void testComposition() {
        final QuteElementRegistry registry = new QuteElementRegistry();
        registry.define("base-elem", new BaseElem());
        registry.define("my-elem", new MyElem());
        final String content = """
                    <base-elem title="Awesome App">
                        <div class="something">
                            <my-elem />
                        </div> 
                    </base-elem>       
                """;
        assertThat(Html.html(registry, content)).isEqualToIgnoringWhitespace(
                """
                    <!DOCTYPE html>
                <html>
                <head>
                <title>Awesome App</title>
                {#bundle /}
                </head>
                <body>
                  <nav class="navbar sticky-top bg-dark border-bottom border-body" data-bs-theme="dark">
                    <div class="container-fluid">
                      <a class="navbar-brand" href="/">
                        <img src="/static/assets/images/logo.svg" alt="Logo" width="30" height="24" class="d-inline-block align-text-top">
                        Awesome App
                      </a>
                    </div>
                  </nav>
                  <div class="container mt-5">
                    <div class="something">
                        W00000T
                    </div> 
                  </div>
                </body>
                </html>
                """);
    }

}