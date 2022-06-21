package com.msa.repository;

import com.netflix.appinfo.InstanceInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.msa.domain.Feed;
import com.msa.repository.dto.FeedData;
import com.msa.repository.dto.FeedRequestDto;
import com.msa.repository.dto.FeedResponseDto;

@Repository
public class FeedRestRepository {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private DiscoveryClient discoveryClient; //DiscoveryClient 자동연결

	public List<Feed> getFeedList(Long userId) {

		FeedResponseDto response = restTemplate
				.getForEntity("http://localhost:8081/feed?userId={userId}", FeedResponseDto.class, userId).getBody();

		List<FeedData> feedDataList = response.getData();
		List<Feed> feedList = new ArrayList<>();
		for (FeedData data : feedDataList) {
			feedList.add(new Feed(data.getUserId(), data.getFolloweeId(), data.getPostId()));
		}

		return feedList;
	}
	
	public void addFeeds(Long userId, Long postId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		FeedRequestDto requestDto = new FeedRequestDto(userId, postId);
		
		HttpEntity<FeedRequestDto> entity = new HttpEntity<FeedRequestDto>(requestDto, headers);

		restTemplate.exchange("http://localhost:8081/feed", HttpMethod.POST, entity, String.class);
	}
}
