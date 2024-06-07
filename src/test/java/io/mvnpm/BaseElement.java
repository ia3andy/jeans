package io.mvnpm;

import static io.mvnpm.Html.*;

public class BaseElement extends Element {

    public String title;

    @Override
    public Html render() {
        // language=html
        return RAW."""
                <!DOCTYPE html>
                <html>
                <head>
                <title>\{title}</title>
                \{HTML("<bundle />")}
                </head>
                <body>
                  <nav class="navbar sticky-top bg-dark border-bottom border-body" data-bs-theme="dark">
                    <div class="container-fluid">
                      <a class="navbar-brand" href="/">
                        <img src="/static/assets/images/logo.svg" alt="Logo" width="30" height="24" class="d-inline-block align-text-top">
                        \{title}
                      </a>
                    </div>
                  </nav>
                  <div class="container mt-5">
                    \{HTML(slot())}
                  </div>
                </body>
                </html>

                """;
    }
}
