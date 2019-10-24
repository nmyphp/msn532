package cn.edu.util;

import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);
    private static final String config_file = "/config.properties";
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            properties.load(Config.class.getResourceAsStream(config_file));
        } catch (IOException e) {
            log.error("加载配置文件resource/config.properties失败:", e);
            throw new RuntimeException(e);
        }
    }

    public static String getValue(String key) {
        return properties.getProperty(key);
    }
}
