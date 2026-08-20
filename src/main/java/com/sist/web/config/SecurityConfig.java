package com.sist.web.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
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
@Configuration // xml => 자바
@EnableWebSecurity // Security Intercept
@RequiredArgsConstructor // lombok : 생성자를 통해 @Autowired => final 변수 자동 매개변수
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
	 *   
	 *    HTTP 요청이 있는 경우 Spring Security가 어떻게 처리할지 지시
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
		/*
		 *   1. Authentication : 인증
		 *   	- 로그인 / 사용자 확인
		 *   2. Authorization : 인가
		 *   	- 권한 확인
		 *   
		 *   동작(실행순서)
		 *   	- 사용자 => /member/login
		 *                      |
		 *                /member/login_process
		 *                      |
		 *                SecurityFilterChain
		 *                      |
		 *               UsernamePasswordAuthenticationFilter
		 *              .usernameParameter("userid")
		 *              => String username=request.getParameter("userid");
		 *                  => <input type="text" name="userid">
		 *              
		 *              .passwordParameter("userpwd")
		 *              => String password=request.getParameter("userpwd");
		 *                  => <input type="password" name="userpwd">
		 *              
		 *                        암호화된 비밀번호 ---- 비밀번호 비교
		 *                              |                |
		 *                              ------------------
		 *                                      |
		 *                                   match encoder
		 *                       |
		 *                       |
		 *                 AuthenticationManager
		 *                       |
		 *                 AuthenticationProvider
		 *                       |
		 *                 JdbcUserDetailsManager
		 *                       | => DB 조회(springmember,authority)
		 *                  UserDetails 생성
		 *                       |
		 *                 BCryptPasswordEncoder
		 *                       | => 비밀번호 비교
		 *                ----------------
		 *                |              |
		 *             인증 성공         인증 실패
		 *       LoginSuccessHandler  LoginFailHandler
		 *                |
		 *          SecurityContext
		 *                |
		 *           Session에 저장
		 *                |
		 *           사용자 인증 완료
		 *           
		 *      /member/login_process
		 *      => Controller가 아닌 SecurityContext가 인터셉트해서 처리
		 *                 
		 */ 
		http
	    .csrf(csrf-> csrf.disable())
	    // 접근 권한 설정(URL)
	    .authorizeHttpRequests(auth-> auth
	          .requestMatchers("/","/member/**").permitAll() // 모든 사람 접근 가능
	          .requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN만 접근 가능
	          .anyRequest().permitAll()
	    )
	    // 로그인 처리
	    .formLogin(form -> form 
	          .loginPage("/member/login") // 로그인 화면 처리
	          // **로그인 처리 담당 => Security에서 인터셉트
	          // 개발자 처리 => Controller / RestController
	          .loginProcessingUrl("/member/login_process") 
	          .usernameParameter("userid")
	          .passwordParameter("userpwd")
	          /*
	           *  인증 객체로 전송
	           *  .userDetailsService(jdbcUserDetailsService()) => 사용자 정보
	           *  .passwordEncoder(passwordEncoder()) => 비밀번호 비교
	           *  
	           *  Session 저장
	           *  사용자 정보 Session 형식으로 Principal에 저장
	           *  
	           */
	          .defaultSuccessUrl("/",false)
	          .successHandler(loginSuccessHandler)
	          .failureHandler(loginFailHandler)
	          .permitAll() 
	    )
	    // 자동 로그인
	    /*
	     *   persistent_logins 테이블에 저장
	     *   
	     *   로그인
	     *     |
	     *   remember-me 체크 => true
	     *     |
	     *   토큰 생성 => 구분자
	     *     |
	     *   JdbcTokenRepository
	     *     |
	     *   DB 저장
	     *    => persistent_logins(username,series,token,last_used)
	     */
	    .rememberMe(remember-> remember
	         .key("my-secret-key")
	         .rememberMeParameter("remember-me")
	         .tokenValiditySeconds(60*60*24) // 저장 기간
	         .tokenRepository(persistentTokenRepository())
	    )
	    /*
	     *   /member/logout
	     *        |
	     *   SpringSecurityLogoutFilter
	     *        |
	     *   SecurityContext 제거
	     *        |
	     *   Session 제거
	     *        |
	     *    Cookie 삭제
	     */
	    .logout(logout -> logout 
	          .logoutUrl("/member/logout")
	          .logoutSuccessUrl("/")
	          .invalidateHttpSession(true)
	          .deleteCookies("remember-me","JSESSIONID")
	    );
		return http.build();
		
	}
	
	
	// 인증 관리자
	/*
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
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	// PersistentLogins 등록
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		return null;
	}
	*/
	@Bean
	   public AuthenticationManager authenticationManager(
	      HttpSecurity http,
	      BCryptPasswordEncoder passwordEncoder
	   ) throws Exception
	   {
		   AuthenticationManagerBuilder builder=
				   http.getSharedObject(AuthenticationManagerBuilder.class);
		   builder
		     .userDetailsService(jdbcUserDetailsService())
		     .passwordEncoder(passwordEncoder());
		   return builder.build();
	   }
	   @Bean
	   public JdbcUserDetailsManager jdbcUserDetailsService() {
		   JdbcUserDetailsManager manager=
				   new JdbcUserDetailsManager(dataSource);
		   manager.setUsersByUsernameQuery(
				   "SELECT userid as username,userpwd as password,enable "
				   +"FROM springmember WHERE userid=?"
		   );
		   manager.setAuthoritiesByUsernameQuery(
				   "SELECT userid as username , authority "
				  +"FROM authority WHERE userid=?"
		   );
		   return manager;
	   }
	   @Bean
	   public BCryptPasswordEncoder passwordEncoder() {
		   return new BCryptPasswordEncoder();
	   }
	   @Bean
	   public PersistentTokenRepository persistentTokenRepository() {
	       JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
	       repo.setDataSource(dataSource);
	       return repo;
	   }
	   
	   
}
/*
 *    [사용자]
 *       | => POST : /member/login_process
 *   -------------------------
 *   SpringSecurityFilterChain
 *   -------------------------
 *             |
 *   UsernamePasswordAuthenticationFilter
 *             | => http.formLogin(form->form.usernameParameter("userid").passwordParameter("userpwd"))
 *   ---------------------
 *   AuthenticationManager
 *   ---------------------
 *             |
 *   AuthenticationProvider
 *             |
 *   JdbcUserDetailsManager
 *             | => DB 검색
 *   ----------------------
 *   |                    |
 *   springmember       authority
 *   (기본 사용자 정보)     (권한 정보)
 *   |                    |
 *   -------------------------> 조건: username이 존재한다면
 *             |
 *        UserDetails
 *             |
 *        BCryptPasswordEncoder
 *             |
 *         비밀번호 검증
 *             |     
 *      --------------------------
 *      |                        |
 *  LoginSuccessHandler   LoginFailHandler
 *          |
 *   SecurityContext
 *          |
 *     Session에 저장
 *          |
 *       인증 완료
 *  
 *  ----------------------------------------
 *  
 *  - 라이브러리 역할
 *    - @EnableWebSecurity: Spring Security 활성화
 *    - @Configuration: 설정 파일
 *      -  SecurityConfig.class : 보안 전체 설정을 담당 => 사용자 정의
 *          1. HttpSecurity
 *          	- 로그인 / 로그아웃 / 권한 / CSRF 보안 등 설정
 *          2. SecurityFilterChain
 *          	- HTTP 요청에 대한 Spring Security filter 처리순서 정의
 *          3. AuthenticationManager
 *          	- 사용자의 인증 과정 총괄
 *          4. AuthenticationProvider
 *          	- 실제 사용자 인증 수행하는 객체
 *          	==> /member/login_process
 *          5. UserDetailsService
 *          	- 로그인한 사용자의 정보 조회
 *          6. JdbcUserDetailsManager
 *          	- DB에서 사용자 정보나 권한 정보를 조회해서 저장
 *          7. UserDetails
 *          	- 사용자 정보 저장된 객체
 *          8. BCryptPasswordEncoder
 *          	- 비밀번호 암호화 처리
 *          9. JdbcTokenRepositoryImpl
 *          	- 자동 로그인시 사용자 구분 토큰을 저장하는 역할
 *          10. LoginSuccessHandler
 *          	- 로그인 성공시 처리하는 Handler
 *          11. LoginFailHandler
 *          	- 로그인 실패시 처리하는 Handler
 *          12. SecurityContext
 *          	- 인증 정보를 보관하는 클래스
 *          13. formLogin
 *          	- 로그인시 처리 방법 설정
 *          14. rememberMe
 *          	- 자동로그인 설정
 *          15. logout
 *          	- 로그아웃시 처리 방법(Session 해제, 쿠키 삭제 등) 설정
 *          
 *     Filter - Manager - UserDetailsService - DB
 *     		  - PasswordEncoder
 *     		  - Authentication
 *            - SecurityContext
 *            
 *     DB
 *       회원정보 / 권한
 *       
 *       Authentication - 로그인된 사용자 정보
 *           |
 *       SecurityContext - Authentication 보관
 *           |
 *       Session - 로그인 상태 유지
 *    ----------------------------------
 *    인증 AuthenticationManager
 *    성공 Authentication => SecurityContext
 *    유지 Session(remember-me) => Cookie+DB
*/