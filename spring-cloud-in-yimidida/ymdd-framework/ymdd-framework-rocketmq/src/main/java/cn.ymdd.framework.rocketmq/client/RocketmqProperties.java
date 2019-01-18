//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.framework.rocketmq.client;

import cn.ymdd.framework.toolkit.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rocketmq")
public class RocketmqProperties {
    public static final String PREFIX = "rocketmq";
    private String serverAddress;
    private String serverType;
    @Value("${spring.application.name}")
    private String serverName;

    public RocketmqProperties() {
    }

    public String getServerType() {
        return this.serverType;
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getServerAddress() {
        return this.serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setServerType(String serverType) {
        if (!"single".equals(serverType) && !"cluster".equals(serverType)) {
            throw new IllegalStateException("Not support");
        } else {
            this.serverType = serverType;
        }
    }

    public final boolean isCluster() {
        return "cluster".equals(this.serverType);
    }

    public static final String formatName(String... names) {
        return StringUtil.join(names, "-");
    }
}
