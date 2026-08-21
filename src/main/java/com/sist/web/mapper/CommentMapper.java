package com.sist.web.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import com.sist.web.vo.*;

@Mapper
@Repository
public interface CommentMapper {
	public List<CommentVO> commentListData(@Param("start") int start,@Param("fno")int fno);
	public int commentRowCount(int fno);
	public void commentInsert(CommentVO vo);
	public void commentDelete(int no);
	public void commentUpdate(CommentVO vo);
}
