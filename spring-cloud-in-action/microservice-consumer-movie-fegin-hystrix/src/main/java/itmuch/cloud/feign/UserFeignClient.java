package itmuch.cloud.feign;


import feign.Param;
import feign.RequestLine;
import itmuch.cloud.entity.User;
import itmuch.config.Configuration1;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "microservice-provider-user", configuration = Configuration1.class ,fallback = HystrixClientFallback.class)
public interface UserFeignClient {

  @RequestLine("GET /simple/{id}")
  public User findById(@Param("id") Long id);
}
