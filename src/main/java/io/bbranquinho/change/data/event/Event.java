package io.bbranquinho.change.data.event;

public interface Event {

    Aggregate apply(Aggregate aggregate);

    default String typeTransformer() {
        return null;
    }

    default String dataTransformer() {
        return null;
    }

}
