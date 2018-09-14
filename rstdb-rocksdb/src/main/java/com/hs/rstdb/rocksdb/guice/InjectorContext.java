package com.hs.rstdb.rocksdb.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 对象容器的封装
 */
public class InjectorContext {
    private static Injector injector = Guice.createInjector(new RocksdbModule());


    /**
     * 返回class指定的对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T get(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

}
