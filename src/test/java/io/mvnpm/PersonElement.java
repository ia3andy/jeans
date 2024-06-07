package io.mvnpm;


import static io.mvnpm.Html.*;

public class PersonElement extends Element {


    public Person value;

    @Override
    public Html render() {

        // language=html
        return RAW."""
           <div class="person">
                <span class='first-name'>\{value.firstName()}</span>
                <span class='last-name'>\{value.lastName}</span>
                <span class="details">
                    \{HTML(slot())}
                </span>
           </div>
        """;
    }

    public record Person(String firstName, String lastName){}


}
