package cn.ymdd.framework.rocketmq.standard;

public abstract interface MQConsumer
{
  public abstract MQResult receive(MQMessage paramMQMessage);
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-rocketmq\0.0.1-SNAPSHOT\guludai-framework-rocketmq-0.0.1-20180208.025258-52.jar
 * Qualified Name:     cn.guludai.framework.rocketmq.standard.MQConsumer
 * JD-Core Version:    0.6.0
 */