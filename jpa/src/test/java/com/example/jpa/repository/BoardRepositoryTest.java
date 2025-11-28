package com.example.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

import com.example.jpa.entity.Board;

@SpringBootTest
public class BoardRepositoryTest {


    @Autowired
    private BoardRepository boardRepository;

    // Board 10 개 삽입
    @Test
    public void insertTest(){
        for (int i = 1; i < 11; i++) {
            
        Board board = Board.builder()
        .writer("작성자"+i)
        .content("내용"+i)
        .title("제목"+i)
        .build();

        boardRepository.save(board);
        }
    }

    // 수정 : title
    @Test
    public void updateTest(){
        // Optional<Board> board = boardRepository.findById(3L);
        // board.ifPresent(content -> {
        //     content.changeContent("수정된 내용");
        //     boardRepository.save(content);
        // });


        Board board = boardRepository.findById(3L).get();
        board.changeContent("수정된 내용");
        boardRepository.save(board);
    }
    // 수정 : content
    @Test
    public void updateTest2(){
        Optional<Board> board = boardRepository.findById(2L);
        board.ifPresent(title -> {
            title.changeTitle("수정된 제목");
            boardRepository.save(title);
        });
    }


    // 조회
    @Test
    public void readTest(){
        Optional<Board> board = boardRepository.findById(5L);
        System.out.println(board.get());
    }    
    // 조회
    @Test
    public void readTest2(){
        List<Board> boards = boardRepository.findAll();
        for (Board board : boards) {
            System.out.println(board);
        }
    }    

    // 삭제
    @Test
    public void deleteTest(){
        Board board = boardRepository.findById(10L).get();
        boardRepository.delete(board);
        // boardRepository.deleteById(10L);
    }
    
}
