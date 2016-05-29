package com.capella.zookeeper.guice;

import com.capella.zookeeper.client.ZookeeperClientImpl;
import com.capella.zookeeper.loader.PropertiesLoader;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.util.Properties;

import static com.google.inject.name.Names.bindProperties;

/**
 * Created by ramesh on 28/05/2016.
 */
public class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        binder().bind(Properties.class).toProvider(PropertiesProvider.class).in(Singleton.class);
        bindProperties(binder(), PropertiesProvider.getProperties());

        bind(ServiceName.class);
        bind(PropertiesLoader.class);
    }

    @Provides
    public ZookeeperClientImpl getZooKeeperConnection() {
        return ZookeeperClientImpl.getInstance();
    }
}
