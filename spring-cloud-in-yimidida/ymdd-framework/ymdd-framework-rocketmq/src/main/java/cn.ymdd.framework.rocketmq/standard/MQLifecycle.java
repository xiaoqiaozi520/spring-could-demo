package cn.ymdd.framework.rocketmq.standard;

public abstract interface MQLifecycle
{
  public static final String CONSUMER_MESSAGE_BACK_TIMES = "consumer.message.backtimes";
  public static final int PRODUCER_CONCURRENT_COUNT_TOTAL = 100;
  public static final int CONSUMER_MESSAGE_DEAD_TIMES = 64;
  public static final int CONSUMER_MESSAGE_BACK_TOTAL = 32;
  public static final int CONSUMER_MESSAGE_BACK_LEVEL = 8;
  public static final int CONSUMER_MESSAGE_SIZE = 1;

  public abstract void destory();

  public abstract void init();
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-rocketmq\0.0.1-SNAPSHOT\guludai-framework-rocketmq-0.0.1-20180208.025258-52.jar
 * Qualified Name:     cn.guludai.framework.rocketmq.standard.MQLifecycle
 * JD-Core Version:    0.6.0
 */