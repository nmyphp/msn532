package cn.edu.util;

import org.junit.Assert;
import org.junit.Test;

public class ConfigTest {

    @Test
    public void getValue() {
        String value = Config.getValue("server.ip");
        Assert.assertNotNull(value);
    }
}
