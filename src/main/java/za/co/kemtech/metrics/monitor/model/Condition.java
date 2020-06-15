package za.co.kemtech.metrics.monitor.model;

import java.util.function.BiFunction;

public enum Condition {
            LESS_THAN((a, b) -> ((Comparable) a).compareTo(b) < 0),
            GREATER_THAN((a,b) -> ((Comparable) a).compareTo(b) > 0),
            EQUALS((a,b) -> a.equals(b));

            final BiFunction<Comparable<?>, Comparable<?>, Boolean> fxn;

    <T> Condition(final BiFunction<Comparable<?>, Comparable<?>, Boolean> fxn) {
        this.fxn = fxn;
    }

    public boolean apply(final Comparable<?> a, final Comparable<?> b){
        return fxn.apply(a, b);
    };
}
