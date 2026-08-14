package com.sist.web.restcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import com.sist.web.vo.*;
import com.sist.web.service.*;
import lombok.RequiredArgsConstructor;
/*
 *   RestFul : 다른 프로그램과 연동
 *   	=> JavaScript , Kotlin
 *   	=> GET(SELECT) / POST(INSERT) / PUT(UPDATE) / DELETE(DELETE)
 *   
 *   클라이언트   |   서버
 *   Vue           SpringFramework
 *   React         Spring-Boot
 *                 NodeJS
 *                 Django / FastAPI
 *                 ASP.NET
 *                 
 *                 
 *   @RequestBody : JSON => Java (Vue,React)
 *   @ModelAttribute : VO => Java (ThymeLeaf)
 */
@RestController
@RequiredArgsConstructor
public class FoodRestController {
	private final FoodService fService;
	
	@GetMapping("/food/list_vue")
	public ResponseEntity<Map> food_list_vue(
			@RequestParam(value = "page",required = false) String page){
		Map map=new HashMap();
		try {
			if(page==null) page="1";
			List<FoodVO> list=fService.foodListData(Integer.parseInt(page));
			int[] pages=fService.foodPages(Integer.parseInt(page));
			map.put("list", list);
			//map.put("pages", pages);
			map.put("curpage", pages[0]);
			map.put("totalpage", pages[1]);
			map.put("startpage", pages[2]);
			map.put("endpage", pages[3]);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/food/detail_vue")
	public ResponseEntity<Map> food_detail_vue(
			@RequestParam("no") int no){
		Map map=new HashMap();
		try {
			FoodVO vo=fService.foodDetailData(no);
			map.put("vo", vo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
}
