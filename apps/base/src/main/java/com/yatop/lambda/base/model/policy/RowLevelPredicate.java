package com.yatop.lambda.base.model.policy;

import java.io.Serializable;
import java.util.function.Predicate;

@FunctionalInterface
public interface RowLevelPredicate<T> extends Predicate<T>, Serializable {
}
