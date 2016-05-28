package com.capella.zookeeper;// import java classes

import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

// import zk classes

public class ZooKeeperConnection {
    private static ZooKeeperConnection instance;
    private static CuratorFramework client;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    private ZooKeeperConnection() throws IOException, InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient("localhost:2121", retryPolicy);
        client.start();
    }

    /**
     * Singleton instance
     *
     * @return
     */
    public static ZooKeeperConnection getInstance() {
        try {
            return instance == null ? instance = new ZooKeeperConnection() : instance;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CuratorFramework getZookeeper() {
        return this.client;
    }

    /**
     * Create or update data for a given path
     *
     * @param path
     * @param data
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     * @throws IOException
     */
    public String create(String path, byte[] data) throws Exception {
        return client.create().forPath(path, data);
    }

    /**
     * check if a node exists
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public Stat exists(String path) throws Exception {
        return client.checkExists().forPath(path);
    }

    /**
     * Get data for a give node path
     *
     * @param path
     * @param t
     * @param <T>
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public <T> T get(final String path, Class<T> t) throws InterruptedException, KeeperException {
        try {
            Stat stat = exists(path);

            if (stat != null) {
                byte[] b = client.getData().forPath(path);
                return (T) SerializationUtils.deserialize(b);
            } else {
                System.out.println("Node does not exists");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Get all children
     *
     * @param path
     * @return
     * @throws Exception
     */
    public List<String> getChildren(String path) throws Exception {
        List<String> children;
        try {
            Stat stat = exists(path); // Stat checks the path

            if (stat != null) {
                //“getChildren” method- get all the children of znode.It has two
                children = client.getChildren().forPath(path);

            } else {
                throw new RuntimeException("Node does not exists");
            }

        } catch (Exception e) {
            throw e;
        }

        return children;

    }

    /**
     * Delete node
     *
     * @param path
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void delete(String path) throws Exception {
        client.delete().forPath(path);
    }

    /**
     * Close the zk connection
     *
     * @throws InterruptedException
     */
    // Method to disconnect from zk server
    public void close() throws InterruptedException {
        client.close();
    }

}