package com.dchealth.common.function;

import java.util.Objects;

/**
 * 表单函数 接收三个参数
 */
@FunctionalInterface
public interface FormConsumer<T, U, E> {

    void accept(T var1, U var2, E var3);

    default FormConsumer<T, U, E> andThen(FormConsumer<? super T, ? super U, ? super E> var1) {
        Objects.requireNonNull(var1);
        return (var2, var3, var4) -> {
            this.accept(var2, var3, var4);
            var1.accept(var2, var3, var4);
        };
    }

}
