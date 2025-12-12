package com.example.board.post.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.member.entity.Member;
import com.example.board.post.dto.BoardDTO;
import com.example.board.post.dto.PageRequestDTO;
import com.example.board.post.dto.PageResultDTO;
import com.example.board.post.entity.Board;
import com.example.board.post.repository.BoardRepository;
import com.example.board.reply.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Transactional
@Log4j2
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    // crud
    public void delete(BoardDTO dto) {
        replyRepository.deleteByBno(dto.getBno());
        boardRepository.deleteById(dto.getBno());

    }

    public void update(BoardDTO dto) {

        Board board = boardRepository.findById(dto.getBno()).get();
        board.changeTitle(dto.getTitle());
        board.changeContent(dto.getContent());

        // boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public BoardDTO getRow(Long bno) {
        Object result = boardRepository.getBoardByBno(bno);

        Object[] arr = (Object[]) result;

        BoardDTO dto = entityToDto((Board) arr[0], (Member) arr[1], (Long) arr[2]);

        return dto;
    }

    @Transactional(readOnly = true)
    public PageResultDTO<BoardDTO> getList(PageRequestDTO requestDTO) {
        Pageable pageable = PageRequest.of(requestDTO.getPage() - 1, requestDTO.getSize(),
                Sort.by("bno").descending());
        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        // 번호, 제목(댓글개수), 작성자, 작성일
        Function<Object[], BoardDTO> f = en -> entityToDto((Board) en[0], (Member) en[1], (Long) en[2]);

        List<BoardDTO> dtoList = result.stream().map(f).collect(Collectors.toList());
        long totalCount = result.getTotalElements();

        PageResultDTO<BoardDTO> pageResultDTO = PageResultDTO.<BoardDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(requestDTO)
                .totalCount(totalCount).build();

        return pageResultDTO;
    }

    public void insert(BoardDTO dto) {
    }

    // entity => dto
    private BoardDTO entityToDto(Board board, Member member, Long replyCnt) {

        BoardDTO dto = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .createDate(board.getCreateDate())
                .updateDate(board.getUpdateDate())
                .replyCnt(replyCnt != null ? replyCnt.intValue() : 0)
                .build();
        return dto;
    }
}
