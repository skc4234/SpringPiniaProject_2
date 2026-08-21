package com.sist.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.mapper.CommentMapper;
import com.sist.web.vo.CommentVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentMapper cMapper;
	
	@Override
	public List<CommentVO> commentListData(int page,int fno) {
		// TODO Auto-generated method stub
		int start=(page*10)-10;
		return cMapper.commentListData(start,fno);
	}

	@Override
	public int commentRowCount(int fno) {
		// TODO Auto-generated method stub
		return cMapper.commentRowCount(fno);
	}

	@Override
	public void commentInsert(CommentVO vo) {
		// TODO Auto-generated method stub
		cMapper.commentInsert(vo);
	}

	@Override
	public void commentDelete(int no) {
		// TODO Auto-generated method stub
		cMapper.commentDelete(no);
	}

	@Override
	public void commentUpdate(CommentVO vo) {
		// TODO Auto-generated method stub
		cMapper.commentUpdate(vo);
	}

}
