package com.sist.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import com.sist.web.service.*;
import com.sist.web.vo.FoodVO;

@Controller
@RequiredArgsConstructor
public class RouterController {
	private final FoodService fService;
	
	@GetMapping("/")
	public String main_main(Model model) {
		model.addAttribute("main_html","main/home");
		return "main/main";
	}

	// 같은 메소드에서 Cookie / HTML 둘다 보내기 불가능
	@GetMapping("/food/detail_before")
	public String food_detail_before(
			@RequestParam("no") int no,
			HttpServletResponse response,
			RedirectAttributes ra) {
		// Cookie는 value 값이 무조건 String
		Cookie cookie=new Cookie("food_"+no, String.valueOf(no));
		cookie.setMaxAge(60*60*24); // 저장 기간
		response.addCookie(cookie); // 브라우저로 전송
		ra.addAttribute("no",no);
		return "redirect:/food/detail";
	}
	
	@GetMapping("/food/detail")
	public String food_detail(
			@RequestParam("no") int no,
			Model model) {
		//model.addAttribute("no",no);
		FoodVO vo=fService.foodDetailData(no);
		model.addAttribute("vo",vo);
		model.addAttribute("main_html","food/detail");
		return "main/main";
	}
	
	
	@RequestMapping("/member/login")
	public String member_login(Model model)
	   {
		   model.addAttribute("main_html", "member/login");
		   return "main/main";
	   }
}
