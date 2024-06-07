package io.mvnpm;


import static io.mvnpm.Html.raw;

public class RawElement extends Element {



    @Override
    public Html render() {
        // language=html
        return raw("""
                
                
                <b>W00000T</b>
                
                """);
    }


}
