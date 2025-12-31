package com.example.movietalk.movie.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movietalk.member.entity.Member;
import com.example.movietalk.movie.dto.ReviewDTO;
import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.Review;
import com.example.movietalk.movie.repository.MovieRepository;
import com.example.movietalk.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Transactional
@Log4j2
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    public Long insertRow(ReviewDTO dto) {
        // dto => entity
        // Review review = dtoToEntity(dto);
        // return reviewRepository.save(review).getRno();

        return reviewRepository.save(dtoToEntity(dto)).getRno();
    }

    public void deleteRow(Long rno) {
        reviewRepository.deleteById(rno);
    }

    public Long updateRow(ReviewDTO dto) {
        // 업데이트 대상 찾기
        Review review = reviewRepository.findById(dto.getRno()).get();
        // 변경 사항 적용
        review.changeText(dto.getText());
        review.changeGrade(dto.getGrade());
        // save() X => dirty checking
        return review.getRno();
    }

    @Transactional(readOnly = true)
    public ReviewDTO getRow(Long rno) {

        // Review review = reviewRepository.findById(rno).get();
        // ReviewDTO dto = entityToDto(review);
        // return dto;

        return entityToDto(reviewRepository.findById(rno).get());
    }

    public List<ReviewDTO> getList(Long mno) {

        Movie movie = movieRepository.findById(mno).get();
        List<Review> reviews = reviewRepository.findByMovie(movie);

        // List<ReviewDTO> list = new ArrayList<>();
        // reviews.forEach(review -> {
        // ReviewDTO dto = entityToDto(review);
        // list.add(dto);
        // });

        List<ReviewDTO> list = reviews.stream()
                .map(review -> entityToDto(review))
                .collect(Collectors.toList());

        return list;

    }

    private ReviewDTO entityToDto(Review review) {
        // Review => ReviewDTO
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .rno(review.getRno())
                .grade(review.getGrade())
                .text(review.getText())
                .email(review.getMember().getEmail())
                .mid(review.getMember().getMid())
                .nickname(review.getMember().getNickname())
                .mno(review.getMovie().getMno())
                .createDate(review.getCreateDate())
                .updateDate(review.getUpdateDate())
                .build();
        return reviewDTO;
    }

    private Review dtoToEntity(ReviewDTO dto) {
        // Review => ReviewDTO

        Review review = Review.builder()
                .rno(dto.getRno())
                .grade(dto.getGrade())
                .text(dto.getText())
                .movie(Movie.builder().mno(dto.getMno()).build())
                .member(Member.builder().mid(dto.getMid()).build())
                .build();
        return review;
    }
}
