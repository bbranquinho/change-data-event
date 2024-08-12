package io.github.bbranquinho;

import java.util.Optional;

public interface Event {

    Aggregate apply(Aggregate aggregate);

    default String typeTransformer() {
        return null;
    }

    default String dataTransformer() {
        return null;
    }

}
