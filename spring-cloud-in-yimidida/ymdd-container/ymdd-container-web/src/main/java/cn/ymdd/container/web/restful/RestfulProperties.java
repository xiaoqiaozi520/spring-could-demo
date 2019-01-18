//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.container.web.restful;

import cn.ymdd.container.runtime.profile.RuntimeEnvironment;
import cn.ymdd.framework.toolkit.util.StringUtil;
import java.util.HashMap;
import java.util.Map;

final class RestfulProperties {
    public static final String RESTFUL_STATIC_RESOURCE = "spring.static.resources";
    public static final Map<String, String[]> RESTFUL_STATIC_RESOURCE_VALUE = new HashMap<String, String[]>() {
        {
            this.put("/templates/**", new String[]{"classpath:/templates/"});
            this.put("/static/**", new String[]{"classpath:/static/"});
        }
    };
    public static final String RESTFUL_CLIENT_TIMEOUT = "feign.timeout";
    public static final String RESTFUL_CLIENT_TIMEOUT_VALUE = "5000,10000";
    public static final String RESTFUL_CLIENT_NAME = "ribbon.client.name";

    RestfulProperties() {
    }

    public static final Integer[] getClientTimeout() {
        String[] times = StringUtil.split("5000,10000", ",");
        String time = RuntimeEnvironment.getProperties("feign.timeout");
        return new Integer[]{Integer.parseInt(times[0]), Integer.parseInt(StringUtil.isEmpty(time) ? times[1] : time)};
    }
}
