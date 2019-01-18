package cn.ymdd.container.runtime.log;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Map;

public class AccessRequestLog
{

  @JSONField(ordinal=0)
  public String id;

  @JSONField(ordinal=1)
  public String requestTime;

  @JSONField(ordinal=2)
  public String requestIp;

  @JSONField(ordinal=3)
  public String requestUri;

  @JSONField(ordinal=4)
  public String requestDomain;

  @JSONField(ordinal=5)
  public String requestProtocol;

  @JSONField(ordinal=6)
  public String requestMethod;

  @JSONField(ordinal=7)
  public Map<String, String> requestHeaders;

  @JSONField(ordinal=8)
  public Map<String, String[]> requestParamters;

  @JSONField(ordinal=9)
  public Object requestBody;

  @JSONField(ordinal=10)
  public long requestSize;
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.log.AccessRequestLog
 * JD-Core Version:    0.6.0
 */