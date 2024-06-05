package io.mvnpm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.mvnpm.Html.html;
import static org.assertj.core.api.Assertions.assertThat;

class HtmlTest {

    @BeforeEach
    void setUp() {
        final QompElementRegistry registry = QompElementRegistry.instance();
        registry.define("base-elem", BaseElem::new);
        registry.define("my-elem", MyElem::new);
    }

    @Test
    void testTags() {

        // language=html
        final String content = """
                    <div>
                        <h1>Hello World</h1>
                    </div>        
                """;
        assertThat(html(content)).isEqualToIgnoringWhitespace(content);
    }


    @Test
    void testMyElem() {
        // language=html
        final String content = """
                    <div class="something">
                        <my-elem />
                    </div>        
                """;
        assertThat(html(content)).isEqualToIgnoringWhitespace(
                """
                    <div class="something">
                        <b>W00000T</b>
                    </div>        
                """
        );
    }

    @Test
    void testComposition() {
        // language=html
        final String content = """
                    <base-elem title="Awesome App">
                        <div class="something">
                            <my-elem />
                        </div> 
                    </base-elem>       
                """;
        assertThat(html(content)).isEqualToIgnoringWhitespace(
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
                        <b>W00000T</b>
                    </div> 
                  </div>
                </body>
                </html>
                """);
    }

}