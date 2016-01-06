package cmri.utils.configuration;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by zhuyin on 6/30/15.
 */
public class ConfigManagerTest {
    @Test
    public void testGetProperty() throws Exception {
        String key = "test_" + UUID.randomUUID().toString();
        Long now = System.currentTimeMillis();
        System.setProperty(key, String.valueOf(now));
        Long time = ConfigManager.getLong(key);
        Assert.assertEquals(now, time);
    }
}