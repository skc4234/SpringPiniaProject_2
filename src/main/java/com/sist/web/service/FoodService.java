package com.sist.web.service;
import java.util.*;
import com.sist.web.vo.*;

public interface FoodService {
	public List<FoodVO> foodListData(int page);
	public int foodTotalPage();
	public FoodVO foodDetailData(int no);
	public int[] foodPages(int page);
}
