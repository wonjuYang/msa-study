package com.msa.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name="baseservice", url="http://localhost:8080/auth")
public interface BaseServiceFeignClient {

  @GetMapping(value = "auth?token={token}")
  String getAuth(@PathVariable("token") String token);

}