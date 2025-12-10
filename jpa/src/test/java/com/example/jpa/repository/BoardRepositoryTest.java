package com.example.jpa.repository;

import java.util.Arrays;
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
    public void insertTest() {
        for (int i = 1; i < 11; i++) {

            Board board = Board.builder()
                    .writer("writer" + i)
                    .content("board content" + i)
                    .title("board title" + i)
                    .build();

            boardRepository.save(board);
        }
    }

    // 수정 : title
    @Test
    public void updateTest() {
        // Optional<Board> board = boardRepository.findById(3L);
        // board.ifPresent(content -> {
        // content.changeContent("수정된 내용");
        // boardRepository.save(content);
        // });

        Board board = boardRepository.findById(3L).get();
        board.changeContent("수정된 내용");
        boardRepository.save(board);
    }

    // 수정 : content
    @Test
    public void updateTest2() {
        Optional<Board> board = boardRepository.findById(2L);
        board.ifPresent(title -> {
            title.changeTitle("수정된 제목");
            boardRepository.save(title);
        });
    }

    // 조회
    @Test
    public void readTest() {
        Optional<Board> board = boardRepository.findById(5L);
        System.out.println(board.get());
    }

    // 조회
    @Test
    public void readTest2() {
        List<Board> boards = boardRepository.findAll();
        for (Board board : boards) {
            System.out.println(board);
        }
    }

    // 삭제
    @Test
    public void deleteTest() {
        Board board = boardRepository.findById(10L).get();
        boardRepository.delete(board);
        // boardRepository.deleteById(10L);
    }

    // 쿼리메소드
    @Test
    public void queryMethodTest() {
        System.out.println(boardRepository.findByTitle("board title2"));
        System.out.println(boardRepository.findByContent("board content2"));
        System.out.println(boardRepository.findByTitleEndingWith("title3"));
        System.out.println(boardRepository.findByTitleContainingAndIdGreaterThanOrderByIdDesc("title", 3L));
        System.out.println(boardRepository.findByWriterContaining("writer"));
        System.out.println(boardRepository.findByTitleContainingOrContentContaining("title", "content"));
    }

    @Test
    public void queryMethodTest2() {
        System.out.println(boardRepository.findByTitle2("board title2"));
        System.out.println(boardRepository.findByContent2("board content2"));
        System.out.println(boardRepository.findByTitleEndingWith2("title3"));
        System.out.println(boardRepository.findByTitleAndId("title", 3L));
        System.out.println(boardRepository.findByWriterContaining2("writer"));
        System.out.println(boardRepository.findByTitleOrContent("title", "content"));
    }

    @Test
    public void queryMethodTest3() {

        System.out.println(boardRepository.findByTitleAndId2("title", 3L));

    }

    @Test
    public void queryMethodTest4() {

        List<Object[]> result = boardRepository.findByTitle3("title");
        for (Object[] objects : result) {
            // System.out.println(Arrays.toString(objects)); // [board title1, writer1]
            String title = (String) objects[0];
            String writer = (String) objects[1];
            System.out.println(title + ", " + writer); // board title1, writer1
        }

    }
}