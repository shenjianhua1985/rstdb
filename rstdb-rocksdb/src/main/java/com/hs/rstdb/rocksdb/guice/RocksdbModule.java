package com.hs.rstdb.rocksdb.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.hs.rstdb.rocksdb.RocksDbDecompose;
import com.hs.rstdb.rocksdb.RocksDbStorageAdapter;
import com.hs.rstdb.spi.Decompose;
import com.hs.rstdb.spi.Resolver;
import com.hs.rstdb.spi.StorageAdapter;
import com.hs.rstdb.spi.impl.DefaultResolver;

/**
 * Created by sjh on 2018/9/14.
 */
public class RocksdbModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(StorageAdapter.class).to(RocksDbStorageAdapter.class).in(Scopes.SINGLETON);
        binder.bind(Decompose.class).to(RocksDbDecompose.class).in(Scopes.SINGLETON);
        binder.bind(Resolver.class).to(DefaultResolver.class).in(Scopes.SINGLETON);
    }
}
