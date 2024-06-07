package io.mvnpm;

import static io.mvnpm.Html.html;
import static io.mvnpm.Html.raw;

public class BaseElement extends Element {

    public String title;

    @Override
    public Html render() {
        // language=html
        return raw("""
                <!DOCTYPE html>
                <html>
                <head>
                <title>%s</title>
                %s
                </head>
                <body>
                  <nav class="navbar sticky-top bg-dark border-bottom border-body" data-bs-theme="dark">
                    <div class="container-fluid">
                      <a class="navbar-brand" href="/">
                        <img src="/static/assets/images/logo.svg" alt="Logo" width="30" height="24" class="d-inline-block align-text-top">
                        %s
                      </a>
                    </div>
                  </nav>
                  <div class="container mt-5">
                    %s
                  </div>
                </body>
                </html>
                                
                """.formatted(title, html("<bundle />"), title, html(slot())));
    }
}
