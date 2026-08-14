package com.sist.web.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import java.util.*;
import com.sist.web.vo.*;
/*
 *   Spring에 메모리 할당 요청
 *   1. @Repository : 데이터베이스 연동 => 구분(저장소)
 *   2. @Service : 요청 처리 => Business Logic
 *   3. @Controller : 화면 변경
 *   	@RestController : JavaScript(Ajax,axios,fetch)와 연동 시 JSON/문자열로 결과값 전송
 *   4. @Component : AOP, ManagerClass, API용 클래스 => 일반 클래스 할당 시 사용
 *   5. @ControllerAdvice     : 예외처리
 *   	@RestControllerAdvice :
 *   6. @Configuration : 자바 환경설정
 *   		- 스프링 보안(JWT)
 *          - 웹소켓 설정
 *          - QueryDSL 설정
 *   ==========> @Component 구분
 */
@Mapper
@Repository
public interface FoodMapper {
	public List<FoodVO> foodListData(int start);
	
	// 총 페이지
	@Select("SELECT CEIL(COUNT(*)/12.0) FROM food")
	public int foodTotalPage();
	
	// 조회수 증가
	@Update("UPDATE food SET hit=hit+1 WHERE no=#{no}")
	public void foodHitIncrement(int no);
	
	// 상세보기
	@Select("SELECT * FROM food WHERE no=#{no}")
	public FoodVO foodDetailData(int no);
}
