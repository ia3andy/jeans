package io.mvnpm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.mvnpm.Html.html;
import static io.mvnpm.Html.typed;
import static org.assertj.core.api.Assertions.assertThat;

class HtmlTest {

    @BeforeEach
    void setUp() {
        final JeansElementRegistry registry = JeansElementRegistry.instance();
        registry.define(BaseElement.class);
        registry.define(RawElement.class);
        registry.define(PersonElement.class);
        registry.define(BundleElement.class);
    }

    @Test
    void testTagElement() {

        // language=html
        final String content = """
                    <div>
                        <h1>Hello World</h1>
                    </div>        
                """;
        assertThat(html(content).toString()).isEqualToIgnoringWhitespace(content);
    }

    @Test
    void testPerson() {
        final PersonElement.Person person = new PersonElement.Person("Andy", "Damevin");
        // language=html
        final String content = """
                    <div>
                        <person value="%s"><h3>Some more info</h3></person>
                        <br/>
                    </div>        
                """.formatted(typed(person));
        assertThat(html(content).toString()).isEqualToIgnoringWhitespace(
                """
                    <div><div class="person"><span><span>Damevin</span><h3>Some more info</h3></span></div><br/></div>    
                """
        );
    }

    @Test
    void testRaw() {
        // language=html
        final String content = """
                    <div class="something">
                        <raw />
                    </div>        
                """;
        assertThat(html(content).toString()).isEqualToIgnoringWhitespace(
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
                    <base title="Awesome App">
                        <div class="something">
                            <my-elem />
                        </div> 
                    </base>       
                """;
        assertThat(html(content).toString()).isEqualToIgnoringWhitespace(
                """
                    <!DOCTYPE html>
                               <html>
                               <head>
                               <title>Awesome App</title>
                               <script type="module" src="/static/bundle/main-ZKDZJE.js"></script><script type="module" src="/static/bundle/main-ZZJFNZ.css"></script>
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
                                   <div class="something"><my-elem /></div>
                                 </div>
                               </body>
                               </html>
                """);
    }

}