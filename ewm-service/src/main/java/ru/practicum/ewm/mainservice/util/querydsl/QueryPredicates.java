package ru.practicum.ewm.mainservice.util.querydsl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryPredicates {
    private final List<Predicate> predicates = new ArrayList<>();

    public <T> QueryPredicates add(T obj, Function<T, Predicate> function) {
        if (obj != null) {
            predicates.add(function.apply(obj));
        }
        return this;
    }

    public <T> QueryPredicates add(Predicate predicate) {
        if (predicate != null) predicates.add(predicate);
        return this;
    }

    public Predicate buildAnd() {
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate buildOr() {
        return ExpressionUtils.anyOf(predicates);
    }

    public static QueryPredicates builder() {
        return new QueryPredicates();
    }
}