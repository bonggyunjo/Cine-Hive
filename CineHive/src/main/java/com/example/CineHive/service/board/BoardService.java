package com.example.CineHive.service.board;

import com.example.CineHive.dto.board.BoardDto;
import com.example.CineHive.entity.User;
import com.example.CineHive.entity.board.Board;
import com.example.CineHive.repository.UserRepository;
import com.example.CineHive.repository.board.BoardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    public Board createBoard(BoardDto boardDto){
        User user = userRepository.findByMemUserid(boardDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Board board = new Board();
        board.setBrdTitle(boardDto.getBrdTitle());
        board.setBrdContent(boardDto.getBrdContent());
        board.setUser(user);

        return boardRepository.save(board);
    }
}
