package cn.ymdd.container.runtime.interceptor;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface ControllerBeforeInterceptor
{
  public abstract ControllerBeforeResult preHandle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject);

  public abstract List<String> getIgnoreUri();

  public abstract List<String> getForbidUri();
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.interceptor.ControllerBeforeInterceptor
 * JD-Core Version:    0.6.0
 */