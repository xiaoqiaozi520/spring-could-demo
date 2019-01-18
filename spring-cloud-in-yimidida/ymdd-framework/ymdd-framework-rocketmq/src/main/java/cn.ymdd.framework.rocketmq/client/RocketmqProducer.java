//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.framework.rocketmq.client;

import cn.ymdd.api.code.BaseCode;
import cn.ymdd.container.runtime.exception.RunningException;
import cn.ymdd.container.runtime.profile.RuntimeEnvironment;
import cn.ymdd.framework.rocketmq.standard.MQError;
import cn.ymdd.framework.rocketmq.standard.MQLifecycle;
import cn.ymdd.framework.rocketmq.standard.MQMessage;
import cn.ymdd.framework.rocketmq.standard.MQProducer;
import cn.ymdd.framework.rocketmq.standard.MQResult;
import cn.ymdd.framework.toolkit.util.StringUtil;
import com.alibaba.fastjson.JSON;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RocketmqProducer implements MQProducer, MQLifecycle {
    private static final Logger log = LoggerFactory.getLogger(RocketmqProducer.class);
    private final AtomicLong counter = new AtomicLong();
    private final DefaultMQProducer producer;
    private Boolean status = false;

    public RocketmqProducer(String name, String address) {
        this.producer = new DefaultMQProducer(name);
        this.producer.setInstanceName(RocketmqProperties.formatName(new String[]{name, this.getClass().getCanonicalName()}));
        this.producer.setVipChannelEnabled(false);
        this.producer.setNamesrvAddr(address);
    }

    public final MQResult send(String topic, MQMessage message) {
        if (!this.status) {
            throw new IllegalStateException("Producer shutdown");
        } else if (StringUtil.isEmpty(topic)) {
            throw new NullPointerException("Message topic");
        } else {
            byte[] body;
            if (null != message && (body = message.getContent()) != null && body.length != 0) {
                long offset = 0L;
                long count = 1L;
                long timeout = 1500L;

                do {
                    if ((offset = this.counter.get()) > 100L) {
                        if (count++ > 2L) {
                            return MQResult.failure(new RunningException(BaseCode.TIMEOUT));
                        }

                        AtomicLong var10 = this.counter;
                        synchronized(this.counter) {
                            try {
                                this.counter.wait(timeout);
                            } catch (Exception var18) {
                                log.error("[ GULUDAI ] Producer flow control, try [" + count + "] times", var18);
                            }
                        }
                    }
                } while(!this.counter.compareAndSet(offset, offset + 1L));

                try {
                    Message msg = new Message(topic, message.getTag(), message.getKey(), body);
                    msg.putUserProperty("consumer.message.backtimes", "0");
                    SendResult result = this.producer.send(msg);
                    MQResult var12;
                    switch(result.getSendStatus()) {
                        case SEND_OK:
                            var12 = MQResult.success();
                            return var12;
                        case SLAVE_NOT_AVAILABLE:
                            if (((RocketmqProperties)RuntimeEnvironment.getBean(RocketmqProperties.class)).isCluster()) {
                                var12 = MQResult.success();
                                return var12;
                            }

                            throw new IllegalStateException(result.toString());
                        default:
                            throw new IllegalStateException(result.toString());
                    }
                } catch (Exception var20) {
                    if (log.isErrorEnabled()) {
                        MQError error = MQError.createMQError(topic, true, var20.getMessage(), message);
                        log.error("[ GULUDAI ] Producer error=" + JSON.toJSONString(error), var20);
                    }

                    MQResult var23 = MQResult.failure(var20.getMessage(), var20);
                    return var23;
                } finally {
                    this.counter.decrementAndGet();
                }
            } else {
                throw new NullPointerException("Message content");
            }
        }
    }

    @PreDestroy
    public final synchronized void destory() {
        if (this.status) {
            this.producer.shutdown();
            this.status = false;
        }

    }

    @PostConstruct
    public final synchronized void init() {
        if (!this.status) {
            try {
                this.producer.start();
                this.status = true;
            } catch (Exception var2) {
                this.status = false;
                if (log.isErrorEnabled()) {
                    log.error(var2.getMessage(), var2);
                }
            }
        }

    }
}
