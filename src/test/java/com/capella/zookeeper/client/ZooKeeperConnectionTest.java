package com.capella.zookeeper.client;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.zookeeper.KeeperException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ZooKeeperConnectionTest extends BaseTest {
    private ZooKeeperConnection zoo = ZooKeeperConnection.getInstance();
    private String path;

    @Before
    public void testCreate() throws Exception {
        byte[] data = SerializationUtils.serialize("helloworld");
        path = zoo.create("/test", data);
        assertThat(path, is("/test"));

    }

    @Test
    public void testGetNode() throws KeeperException, InterruptedException {
        assertThat(zoo.get(path, String.class), is("helloworld"));
    }

    @Test
    public void testGetChildren() throws Exception {
        List<String> children = zoo.getChildren(path);
        for (String child : children) {
            System.out.println("child : " + child);
        }
    }

    @After
    public void tearDown() throws Exception {
        zoo.delete(path);
    }
}