package com.example.memo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.memo.dto.MemoDTO;
import com.example.memo.entity.Memo;
import com.example.memo.repository.MemoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor


@Log4j2
@Service
public class MemoService {
    

    // @Autowired
    // private MemoRepository memoRepository;
    // @Autowired
    // private ModelMapper modelMapper;

    private final MemoRepository memoRepository;
    private final ModelMapper modelMapper;

    // 전체조회
    public List<MemoDTO> readAll(){
        List<Memo> memos = memoRepository.findAll();
        
        // Entity : service => repository, repository => service
        // ~~DTO : service => controller, controller => service

        // 리턴하기 전 Memo entity => MemoDTO 로 변경 후 리턴
        // List<MemoDTO> list = new ArrayList<>();
        // for (Memo memo : memos) {
        //     // MemoDTO dto = MemoDTO.builder()
        //     // .id(memo.getId())
        //     // .text(memo.getText())
        //     // .createDate(memo.getCreateDate())
        //     // .updateDate(memo.getUpdateDate())
        //     // .build();
        //     MemoDTO dto = modelMapper.map(memo,MemoDTO.class);
        //     list.add(dto);
        // }
        // return list;

        List<MemoDTO> list = memos.stream()
        .map(memo -> modelMapper.map(memo,MemoDTO.class))
        .collect(Collectors.toList());

        return list;

    }

    // 하나 조회
    public MemoDTO read(Long id){

        // Memo memo = memoRepository.findById(id).get();

        // Optional<Memo> result = memoRepository.findById(id);
        // Memo memo = null;
        // if (result.isPresent()){
        //     memo = result.get();
        // }

        // NoSuchElementException
        Memo memo = memoRepository.findById(id).orElseThrow();
        // entity => dto 변환 후 리턴
        return modelMapper.map(memo,MemoDTO.class);
    }

    // 하나 수정
    public Long modify(MemoDTO dto){
        // 1) 수정대상 찾기
        Memo memo = memoRepository.findById(dto.getId()).orElseThrow();

        // 2) 변경
        memo.changeText(dto.getText());
        // memo = memoRepository.save(memo);
        // return memo.getId();
        return memoRepository.save(memo).getId();
    }

    // 하나 삭제
    public void remove(Long id){
        memoRepository.deleteById(id);
    }

    // 새로운 메모 삽입
    public Long insert(MemoDTO dto){
        // dto => entity
        Memo memo = modelMapper.map(dto, Memo.class);
        return memoRepository.save(memo).getId();
    }
    
    
    
}
