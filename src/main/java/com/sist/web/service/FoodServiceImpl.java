package com.sist.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.mapper.FoodMapper;
import com.sist.web.vo.FoodVO;

import lombok.RequiredArgsConstructor;
/*
 *   1. Mapper : 재료(데이터베이스만 연동)
 *   2. Service : 쉐프(전체 요청 처리) => DB+OpenAPI
 *   3. Controller     서빙(Router)
 *   	RestController 음식 전달(값 전송)
 */
@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
	private final FoodMapper fMapper;
	private int ROWSIZE=12;
	@Override
	public List<FoodVO> foodListData(int page) {
		// TODO Auto-generated method stub
		int start=(page*ROWSIZE)-ROWSIZE;
		return fMapper.foodListData(start);
	}

	@Override
	public int foodTotalPage() {
		// TODO Auto-generated method stub
		return fMapper.foodTotalPage();
	}

	@Override
	public FoodVO foodDetailData(int no) {
		// TODO Auto-generated method stub
		fMapper.foodHitIncrement(no);
		return fMapper.foodDetailData(no);
	}

	@Override
	public int[] foodPages(int page) {
		// TODO Auto-generated method stub
		int totalpage=foodTotalPage();
		final int BLOCK=10;
		int startpage=((page-1)/BLOCK*BLOCK)+1;
		int endpage=((page-1)/BLOCK*BLOCK)+BLOCK;
		int[] pages= {page,totalpage,startpage,endpage};
		return pages;
	}

}
