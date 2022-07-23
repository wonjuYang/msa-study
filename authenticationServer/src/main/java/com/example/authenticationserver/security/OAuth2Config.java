package com.example.authenticationserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration //설정 클래스
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override //서비스에 등록될 클라이언트 정의
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
        .withClient("eagleeye") //애플리케이션 이름
        .secret("thisissecret") //서버호출시 제시할 시크릿
        .authorizedGrantTypes("refresh_token", "password", "client_credentials") //인가 그랜트 타입 전달
        .scopes("webclient", "mobileclient"); //동작 범위
  }

  @Override //기본인증 관리자와 사용자 상세 서비스 이용한다고 알려줌
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService);
  }
}
