package cn.ymdd.framework.rocketmq.standard;

public abstract interface MQProducer
{
  public abstract MQResult send(String paramString, MQMessage paramMQMessage);
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-rocketmq\0.0.1-SNAPSHOT\guludai-framework-rocketmq-0.0.1-20180208.025258-52.jar
 * Qualified Name:     cn.guludai.framework.rocketmq.standard.MQProducer
 * JD-Core Version:    0.6.0
 */