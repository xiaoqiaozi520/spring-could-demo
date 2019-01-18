//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.framework.rocketmq.client;

import cn.ymdd.container.runtime.profile.RuntimeEnvironment;
import cn.ymdd.framework.rocketmq.bind.RocketMQ;
import cn.ymdd.framework.rocketmq.standard.MQConfigurator;
import cn.ymdd.framework.rocketmq.standard.MQConsumer;
import cn.ymdd.framework.rocketmq.standard.MQLifecycle;
import cn.ymdd.framework.rocketmq.standard.MQMetadata;
import cn.ymdd.framework.toolkit.util.CollectionUtil;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties({RocketmqProperties.class})
@ConditionalOnProperty(
        prefix = "rocketmq",
        value = {"serverAddress"}
)
public class RocketmqConfigurator {
    private static final Logger log = LoggerFactory.getLogger(RocketmqConfigurator.class);

    public RocketmqConfigurator() {
        if (log.isInfoEnabled()) {
            log.info("[ GULUDAI ] Inited rocketmq configurator...");
        }

    }

    @Primary
    @Configuration
    protected class DefaultRocketmqConsumer implements InitializingBean {
        private final Map<MQConsumer, MQLifecycle> cycles = Maps.newConcurrentMap();

        protected DefaultRocketmqConsumer() {
        }

        public synchronized void afterPropertiesSet() throws Exception {
            RocketmqProperties properties = (RocketmqProperties)RuntimeEnvironment.getBean(RocketmqProperties.class);
            String name = properties.getServerName();
            String address = properties.getServerAddress();
            Map<String, RocketmqModel> models = Maps.newConcurrentMap();
            Map<String, MQConfigurator> configs = RuntimeEnvironment.getBeansByType(MQConfigurator.class);
            if (CollectionUtil.isNotEmpty(configs)) {
                Iterator var6 = configs.entrySet().iterator();

                while(var6.hasNext()) {
                    Entry<String, MQConfigurator> config = (Entry)var6.next();
                    MQMetadata meta = ((MQConfigurator)config.getValue()).configMetadata();
                    if (meta == null) {
                        throw new NullPointerException("MQ Metadata");
                    }

                    models.put(config.getKey(), RocketmqModel.createRocketmqModel(meta, name, address));
                }
            }

            Map<String, MQConsumer> consumers = RuntimeEnvironment.getBeansByType(MQConsumer.class);
            Iterator var12;
            Entry consumer;
            if (CollectionUtil.isNotEmpty(consumers)) {
                var12 = consumers.entrySet().iterator();

                while(var12.hasNext()) {
                    consumer = (Entry)var12.next();
                    RocketMQ mq = (RocketMQ)AopUtils.getTargetClass(consumer.getValue()).getAnnotation(RocketMQ.class);
                    if (models.containsKey(consumer.getKey())) {
                        if (mq != null) {
                            throw new IllegalStateException("More Configuration");
                        }
                    } else {
                        if (mq == null) {
                            throw new NullPointerException("MQ Annotation");
                        }

                        models.put((String) consumer.getKey(), RocketmqModel.createRocketmqModel(mq, name, address));
                    }
                }
            }

            var12 = models.entrySet().iterator();

            while(var12.hasNext()) {
                consumer = (Entry)var12.next();
                MQConsumer consumerx = (MQConsumer)consumers.get(consumer.getKey());
                if (consumerx != null && this.cycles.get(consumerx) == null) {
                    RocketmqConsumer rmq = new RocketmqConsumer(consumerx, (RocketmqModel)consumer.getValue());
                    this.cycles.put(consumerx, rmq);
                    rmq.init();
                }
            }

        }

        @PreDestroy
        public synchronized void afterDestroySet() {
            Iterator var1 = this.cycles.values().iterator();

            while(var1.hasNext()) {
                MQLifecycle jms = (MQLifecycle)var1.next();
                jms.destory();
            }

            this.cycles.clear();
        }
    }

    @Primary
    @Configuration
    protected class DefaultRocketmqProducer extends RocketmqProducer {
        @Autowired
        public DefaultRocketmqProducer(RocketmqProperties properties) {
            super(properties.getServerName(), properties.getServerAddress());
        }
    }
}
