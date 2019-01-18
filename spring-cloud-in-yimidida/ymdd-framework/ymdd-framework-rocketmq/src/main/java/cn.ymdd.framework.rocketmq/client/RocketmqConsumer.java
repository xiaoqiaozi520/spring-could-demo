//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.framework.rocketmq.client;

import cn.ymdd.framework.rocketmq.standard.MQConsumer;
import cn.ymdd.framework.rocketmq.standard.MQError;
import cn.ymdd.framework.rocketmq.standard.MQLifecycle;
import cn.ymdd.framework.rocketmq.standard.MQMessage;
import cn.ymdd.framework.rocketmq.standard.MQResult;
import cn.ymdd.framework.toolkit.util.StringUtil;
import com.alibaba.fastjson.JSON;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RocketmqConsumer implements MQConsumer, MQLifecycle {
    private static final Logger log = LoggerFactory.getLogger(RocketmqConsumer.class);
    private final DefaultMQPushConsumer consumer;
    private final RocketmqModel model;
    private final MQConsumer client;
    private Boolean status = false;

    public RocketmqConsumer(MQConsumer client, RocketmqModel model) {
        this.consumer = new DefaultMQPushConsumer(model.getGroup());
        this.consumer.setInstanceName(RocketmqProperties.formatName(new String[]{model.getName(), this.getClass().getCanonicalName()}));
        this.consumer.setNamesrvAddr(model.getAddress());
        this.client = client;
        this.model = model;
    }

    public MQResult receive(MQMessage message) {
        if (this.status) {
            return this.client.receive(message);
        } else {
            throw new IllegalStateException("Consumer shutdown");
        }
    }

    public final synchronized void destory() {
        if (this.status) {
            this.consumer.shutdown();
            this.status = false;
        }

    }

    public final synchronized void init() {
        if (!this.status) {
            try {
                this.consumer.setConsumeThreadMin(1);
                this.consumer.setConsumeThreadMax(this.model.getConsumer());
                this.consumer.subscribe(this.model.getTopic(), this.model.getTag());
                this.consumer.setConsumeMessageBatchMaxSize(1);
                this.consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
                this.consumer.registerMessageListener(new MessageListenerConcurrently() {
                    public RocketmqConsumer.RecoveryConcurrentlyModel recoveryMessage(MessageExt msg, ConsumeConcurrentlyContext context) {
                        RocketmqConsumer.RecoveryConcurrentlyModel model = new RocketmqConsumer.RecoveryConcurrentlyModel();

                        try {
                            int times = model.setTimes(msg.getReconsumeTimes()).getTimes();
                            String backs = msg.getUserProperty("consumer.message.backtimes");
                            if (StringUtil.isNotEmpty(backs)) {
                                times += Integer.parseInt(backs) * 8;
                                backs = String.valueOf(Integer.parseInt(backs) + 1);
                                model.setTimes(times);
                            }

                            if (Integer.parseInt(backs) >= 32) {
                                model.setStatus(false);
                                return model;
                            }

                            DefaultMQProducer producer = RocketmqConsumer.this.consumer.getDefaultMQPushConsumerImpl().getmQClientFactory().getDefaultMQProducer();
                            Message newMsg = new Message(msg.getTopic(), msg.getTags(), msg.getKeys(), msg.getBody());
                            newMsg.putUserProperty("consumer.message.backtimes", backs);
                            SendResult mr = producer.send(newMsg);
                            switch(mr.getSendStatus()) {
                                case SEND_OK:
                                    model.setStatus(true);
                                    break;
                                default:
                                    model.setStatus(false);
                                    throw new IllegalStateException(mr.getSendStatus().name());
                            }
                        } catch (Exception var9) {
                            if (RocketmqConsumer.log.isErrorEnabled()) {
                                RocketmqConsumer.log.error(var9.getMessage(), var9);
                            }
                        }

                        return model;
                    }

                    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                        MessageExt msg = (MessageExt)msgs.get(0);
                        int times = msg.getReconsumeTimes();
                        MQMessage message = null;
                        boolean success = false;
                        Exception ex = null;
                        String body = null;
                        String key = null;
                        boolean var17 = false;

                        RocketmqConsumer.RecoveryConcurrentlyModel model;
                        boolean deadx;
                        MQError errorx;
                        label204: {
                            try {
                                var17 = true;
                                key = msg.getKeys();
                                body = new String(msg.getBody(), Charset.forName("UTF-8"));
                                message = new MQMessage(key, body, msg.getTags(), true);
                                MQResult result = RocketmqConsumer.this.receive(message);
                                if (result == null || result.isFailure()) {
                                    throw new IllegalStateException("Consumer return 'false'");
                                }

                                success = result.isSuccess();
                                var17 = false;
                                break label204;
                            } catch (Exception var18) {
                                success = false;
                                ex = var18;
                                var17 = false;
                            } finally {
                                if (var17) {
                                    if (!success) {
                                        boolean dead = false;
                                        if (times >= 64) {
                                            success = true;
                                            dead = true;
                                        }

                                        if (!dead && times != 0 && times % 8 == 0) {
                                            RocketmqConsumer.RecoveryConcurrentlyModel modelx = this.recoveryMessage(msg, context);
                                            success = modelx.isStatus();
                                            times = modelx.getTimes();
                                        }

                                        if (ex != null && RocketmqConsumer.log.isErrorEnabled()) {
                                            MQError error = MQError.createMQError(RocketmqConsumer.this.model.getTopic(), times, dead, ex.getMessage(), message);
                                            RocketmqConsumer.log.error("[ GULUDAI ] Consumer error=" + JSON.toJSONString(error), ex);
                                        }
                                    }

                                }
                            }

                            if (!success) {
                                deadx = false;
                                if (times >= 64) {
                                    success = true;
                                    deadx = true;
                                }

                                if (!deadx && times != 0 && times % 8 == 0) {
                                    model = this.recoveryMessage(msg, context);
                                    success = model.isStatus();
                                    times = model.getTimes();
                                }

                                if (ex != null && RocketmqConsumer.log.isErrorEnabled()) {
                                    errorx = MQError.createMQError(RocketmqConsumer.this.model.getTopic(), times, deadx, ex.getMessage(), message);
                                    RocketmqConsumer.log.error("[ GULUDAI ] Consumer error=" + JSON.toJSONString(errorx), ex);
                                    return success ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
                                }
                            }

                            return success ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
                        }

                        if (!success) {
                            deadx = false;
                            if (times >= 64) {
                                success = true;
                                deadx = true;
                            }

                            if (!deadx && times != 0 && times % 8 == 0) {
                                model = this.recoveryMessage(msg, context);
                                success = model.isStatus();
                                times = model.getTimes();
                            }

                            if (ex != null && RocketmqConsumer.log.isErrorEnabled()) {
                                errorx = MQError.createMQError(RocketmqConsumer.this.model.getTopic(), times, deadx, ex.getMessage(), message);
                                RocketmqConsumer.log.error("[ GULUDAI ] Consumer error=" + JSON.toJSONString(errorx), ex);
                            }
                        }

                        return success ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                });
                this.consumer.start();
                this.status = true;
            } catch (Exception var2) {
                this.status = false;
                if (log.isErrorEnabled()) {
                    log.error(var2.getMessage(), var2);
                }
            }
        }

    }

    static final class RecoveryConcurrentlyModel {
        private boolean status;
        private int times;

        RecoveryConcurrentlyModel() {
        }

        public int getTimes() {
            return this.times;
        }

        public RocketmqConsumer.RecoveryConcurrentlyModel setTimes(int times) {
            this.times = times;
            return this;
        }

        public boolean isStatus() {
            return this.status;
        }

        public RocketmqConsumer.RecoveryConcurrentlyModel setStatus(boolean status) {
            this.status = status;
            return this;
        }
    }
}
