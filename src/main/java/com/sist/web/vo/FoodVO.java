package com.sist.web.vo;

import lombok.Data;

@Data
public class FoodVO {
	private int no;
	
	private int cno,likecount,replycount,jjimcount,hit;
	private String name,type,phone,address,price,theme,time,reserve,
					parking,content,poster,images;
	private double score;
}
