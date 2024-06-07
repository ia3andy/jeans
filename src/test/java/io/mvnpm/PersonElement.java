package io.mvnpm;


import static io.mvnpm.Html.html;

public class PersonElement extends Element {


    public Person value;

    @Override
    public Html render() {

        // language=html
        return html("""
           <div class="person">
                <span class='first-name'>%s</span>
                <span class='last-name'>%s</span>
                <span class="details">
                    %s
                </span>
           </div>
        """.formatted(value.firstName(), value.lastName, html(slot())));
    }

    public record Person(String firstName, String lastName){}


}
