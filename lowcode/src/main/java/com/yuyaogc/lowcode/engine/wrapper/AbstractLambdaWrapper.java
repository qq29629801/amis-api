package com.yuyaogc.lowcode.engine.wrapper;

import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.func.SFunction;

/**
 * Lambda 语法使用 Wrapper
 * <p>统一处理解析 lambda 获取 column</p>
 *
 * @author hubin miemie HCL
 * @since 2017-05-26
 */
public abstract class AbstractLambdaWrapper<T, Children extends AbstractLambdaWrapper<T, Children>>
        extends Criteria<T, SFunction<T, ?>, Children> {


    @Override
    @SafeVarargs
    public final Children groupBy(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.groupBy(condition, column, columns);
    }


    @Override
    @SafeVarargs
    public final Children orderByAsc(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.orderByAsc(column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderByAsc(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.orderByAsc(condition, column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderByDesc(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.orderByDesc(column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderByDesc(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return super.orderByDesc(condition, column, columns);
    }


}
