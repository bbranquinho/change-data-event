package io.github.bbranquinho;

public interface Event {

    Aggregate apply(Aggregate aggregate);

}
