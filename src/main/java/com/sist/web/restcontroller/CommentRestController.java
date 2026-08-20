package com.sist.web.restcontroller;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.sist.web.vo.*;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import com.sist.web.service.*;

@Controller
@RequiredArgsConstructor
public class CommentRestController {
	private final CommentService cService;
	
	public Map commonsData(int page,int fno) {
		Map map=new HashMap();
		List<CommentVO> list=cService.commentListData(page,fno);
		int count=cService.commentRowCount(fno);
		int totalpage=(int)(Math.ceil(count/10.0));
		map.put("rList", list);
		map.put("count", count);
		map.put("curpage", page);
		map.put("totalpage", totalpage);
		return map;
	}
	
	@GetMapping("/comment/list_vue")
	public ResponseEntity<Map> comment_list_vue(
			@RequestParam("page") int page,
			@RequestParam("fno") int fno){
		Map map=new HashMap();
		try {
			map=commonsData(page, fno);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	@PostMapping("/comment/insert_vue")
	// {} json => vo 객체 변환 : @RequestBody
	// 일반 데이터(String) => vo 객체 변환 : @ModelAttribute
	public ResponseEntity<Map> comment_insert_vue(
			@RequestBody CommentVO vo,
			HttpSession session){
		Map map=new HashMap();
		try {
			String id=(String)session.getAttribute("userid");
			String name=(String)session.getAttribute("username");
			vo.setId(id);
			vo.setName(name);
			cService.commentInsert(vo);
			map=commonsData(vo.getPage(), vo.getFno());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
}
