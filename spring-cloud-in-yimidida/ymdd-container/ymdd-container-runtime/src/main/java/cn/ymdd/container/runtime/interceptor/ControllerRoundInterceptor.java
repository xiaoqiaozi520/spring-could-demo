package cn.ymdd.container.runtime.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public abstract interface ControllerRoundInterceptor extends ControllerBeforeInterceptor
{
  public abstract void postHandle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, ModelAndView paramModelAndView);

  public abstract void afterCompletion(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Exception paramException);
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.interceptor.ControllerRoundInterceptor
 * JD-Core Version:    0.6.0
 */