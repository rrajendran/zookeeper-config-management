package com.capella.zookeeper.loader;

import com.capella.zookeeper.client.ZooKeeperClientImpl;
import com.capella.zookeeper.guice.PropertiesWatcher;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Properties loader
 */
public class PropertiesLoaderImpl implements PropertiesLoader{

    @Inject
    private ZooKeeperClientImpl zkConnection;

    /**
     * Load properties to zookeeper
     *
     * @param inputStream
     * @param namespace
     * @throws Exception
     */
    @Override
    public void load(InputStream inputStream, String namespace) throws Exception {
        Properties properties = new Properties();
        properties.load(inputStream);
        Map<String, String> map = (Map) properties;

        String rootNode = zkConnection.create("/" + namespace, null);
        map.entrySet().stream().forEach(entry -> {
            try {
                //byte[] data = SerializationUtils.serialize(entry.getValue());
                zkConnection.create(rootNode + "/" + entry.getKey(), entry.getValue().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Read properties from zookeeper
     *
     * @param namespace
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> readProperties(String namespace) throws Exception {
        List<String> children = zkConnection.getChildren(namespace, new PropertiesWatcher());
        Map<String, String> props = new HashMap<String, String>();
        for (String child : children) {
            String path = namespace + "/" + child;
            props.put(child, new String(zkConnection.get(path)));
        }
        return props;

    }
}
