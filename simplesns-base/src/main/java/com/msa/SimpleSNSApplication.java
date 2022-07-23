package com.msa;

import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableBinding(SimpleSNSApplication.ProducerChannel.class)
public class SimpleSNSApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleSNSApplication.class, args);
	}


	public interface ProducerChannel {

		String DIRECT = "direct";

		String BROADCAST = "broadcast";

		@Output(ProducerChannel.DIRECT)
		MessageChannel directMessage();

		@Output(ProducerChannel.BROADCAST)
		MessageChannel broadcastMessage();
	}

	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public Sampler defalutSampler(){return Sampler.ALWAYS_SAMPLE;}
}
