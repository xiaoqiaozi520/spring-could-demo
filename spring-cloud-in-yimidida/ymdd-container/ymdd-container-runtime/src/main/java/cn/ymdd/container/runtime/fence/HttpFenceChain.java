package cn.ymdd.container.runtime.fence;

public abstract interface HttpFenceChain
{
  public abstract void requestHttpFence(HttpFenceContent paramHttpFenceContent);

  public abstract void responseHttpFence(HttpFenceContent paramHttpFenceContent);
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.fence.HttpFenceChain
 * JD-Core Version:    0.6.0
 */