package com.msa;

import brave.sampler.Sampler;
import com.msa.service.FeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.web.client.RestTemplate;

@EnableEurekaClient
@SpringBootApplication
@EnableBinding(SimplesnsSocialApplication.ConsumerChannel.class)
public class SimplesnsSocialApplication {

	@Autowired
	FeedService feedService;

	public static void main(String[] args) {

		SpringApplication.run(SimplesnsSocialApplication.class, args);


	}

	@StreamListener(ConsumerChannel.DIRECT)
	public void handleDirect(String message) {

		Logger logger = LoggerFactory.getLogger(getClass());
		logger.info("##### direct message : {}", message);


		feedService.addFeeds(Long.parseLong(message.split(":")[0]), Long.parseLong(message.split(":")[1]));
	}

	@StreamListener(ConsumerChannel.BROADCAST)
	public void handleBroadcast(String message) {
		Logger logger = LoggerFactory.getLogger(getClass());
		logger.info("##### broadcast message : {}", message);
	}


	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}


	public interface ConsumerChannel {

		String DIRECT = "direct";

		String BROADCAST = "broadcast";

		@Input(ConsumerChannel.DIRECT)
		SubscribableChannel directMessage();

		@Input(ConsumerChannel.BROADCAST)
		SubscribableChannel broadcastMessage();
	}

	@Bean
	public Sampler defaultSampler(){return Sampler.ALWAYS_SAMPLE;}



}
