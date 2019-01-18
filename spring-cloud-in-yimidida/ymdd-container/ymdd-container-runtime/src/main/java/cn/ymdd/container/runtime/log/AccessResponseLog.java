package cn.ymdd.container.runtime.log;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Map;

public class AccessResponseLog
{

  @JSONField(ordinal=0)
  public String id;

  @JSONField(ordinal=1)
  public String responseTime;

  @JSONField(ordinal=2)
  public int responseCode;

  @JSONField(ordinal=3)
  public Map<String, String> responseHeaders;

  @JSONField(ordinal=4)
  public Object responseBody;

  @JSONField(ordinal=5)
  public long responseSize;
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.log.AccessResponseLog
 * JD-Core Version:    0.6.0
 */