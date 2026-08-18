package com.sist.web.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.sist.web.security.LoginFailHandler;
import com.sist.web.security.LoginSuccessHandler;

import lombok.RequiredArgsConstructor;
/*
 *   1. Spring Security
 *   	=> 보안을 담당하는 프레임워크
 *      - 인증 : Authentication => 사용자가 누구인지 확인 => 로그인
 *      - 인가 : Authorization => 인증된 사용자가 사이트에 접근 가능한지 확인 => 권한
 *      - 저장(인증 => 권한 => 저장 => Session)
 *      - 서버 종료시 메모리 해제
 *      - Cookie 기반 : JWT
 *      
 *      1) 인증 => 회원가입된 사람 / 게스트 / 관리자
 *         DispatcherServlet => HandlerMapping => ViewResolver => JSP/ThymeLeaf
 *                |                                     |
 *            Interceptor                          Interceptor
 *                |                                     |
 *            preHandle()                          postHandle()
 *            
 *      2) Authentication Filter
 *      	- 책임 전가
 *      3) Authentication Manager
 *      	- 인증 방법
 *      4) Authentication Provider
 *      	- DataBase 연동
 *      	4-1) User 비교 : PasswordEncoding / 암호 비교
 *      5) UserDetailService
 *      	- 결과값 return
 *      
 *      
 *      /login => permitAll
 *      /admin => hasRole("ADMIN")
 *      /user  => 
 *      /board
 *      /member
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final LoginSuccessHandler loginSuccessHandler;
	private final LoginFailHandler loginFailHandler;
	private final DataSource dataSource;
	
	// 접근 권한 => SecurityFilterChain
	/*
	 *   - 권한(permitAll: 모든 사람에게 권한, hasRole('ROLE_ADMIN'): 특정 역할에게 권한)
	 *   	- login
	 *   	- logout
	 *   	- remember-me
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) {
		// CSRF(Cross Site Request Forgery) 공격 방어
		/*
		 *   CSRF : 크로스 사이트 요청 위조
		 *   	- 공격자가 인증된 브라우저에서 저장된ㄷ 쿠키나 세션정보를 활용해서
		 *   	  다른 사이트 요청값 전달 => 위조
		 *      - 일반 보안 : crsf.disable()
		 */
		http
		  .csrf(csrf->csrf.disable())
		  .authorizeHttpRequests(auth->auth
				  .requestMatchers("/","/member/**").permitAll()
				  .requestMatchers("/admin/**").hasRole("ADMIN")
				  .anyRequest().permitAll())
		  .formLogin(form->form
				  .loginPage("/member/login")
				  .loginProcessingUrl("/member/login_process")
				  .usernameParameter("userid")
				  .passwordParameter("userpwd")
				  .defaultSuccessUrl("/",false)
				  .successHandler(loginSuccessHandler)
				  .failureHandler(loginFailHandler)
				  .permitAll())
		  .rememberMe(remember->remember
				  .key("my-secret-key")
				  .rememberMeParameter("remember-me")
				  .tokenValiditySeconds(60*60*24))
		  .logout(logout->logout
				  .logoutUrl("/member/logout")
				  .logoutSuccessUrl("/")
				  .invalidateHttpSession(true)
				  .deleteCookies("remember-me","JSESSIONID"));
		// remember-me
		return http.build();
		
	}
	
	
	// 인증 관리자
	/*
	 *   
	 */
	@Bean
	public AuthenticationManager authenticationManager(
			HttpSecurity http,BCryptPasswordEncoder passwordEncoder)
			throws Exception {
		return null;
	}
	
	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsService() {
		return null;
	}
	

	// 비밀번호 암호화
	/*
	 *  
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	// PersistentLogins 등록
	/*
	 * 
	 */
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		return null;
	}
}
