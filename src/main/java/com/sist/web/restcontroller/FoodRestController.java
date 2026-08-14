package com.sist.web.restcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import com.sist.web.vo.*;
import com.sist.web.service.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FoodRestController {
	private final FoodService fService;
	
	@GetMapping("/food/list")
	public ResponseEntity<Map> food_list(
			@RequestParam(value = "page",required = false) String page){
		Map map=new HashMap();
		try {
			if(page==null) page="1";
			List<FoodVO> list=fService.foodListData(Integer.parseInt(page));
			int[] pages=fService.foodPages(Integer.parseInt(page));
			map.put("list", list);
			map.put("pages", pages);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
}
