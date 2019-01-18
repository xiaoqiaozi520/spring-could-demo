package cn.ymdd.framework.rocketmq.bind;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RocketMQ
{
  public static final String PACKAGE = "cn.guludai.framework.rocketmq.bind.RocketMQ";
  public static final String TAGS = "*";

  public abstract String group();

  public abstract String topic();

  public abstract String tag();

  public abstract int consumer();
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-rocketmq\0.0.1-SNAPSHOT\guludai-framework-rocketmq-0.0.1-20180208.025258-52.jar
 * Qualified Name:     cn.guludai.framework.rocketmq.bind.RocketMQ
 * JD-Core Version:    0.6.0
 */