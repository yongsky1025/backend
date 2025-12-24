package com.example.movietalk.movie.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movietalk.movie.dto.MovieDTO;
import com.example.movietalk.movie.dto.MovieImageDTO;
import com.example.movietalk.movie.dto.PageRequestDTO;
import com.example.movietalk.movie.dto.PageResultDTO;
import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.MovieImage;
import com.example.movietalk.movie.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Transactional
@Service
@Log4j2
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    // 전체조회
    @Transactional(readOnly = true)
    public PageResultDTO<MovieDTO> getMovieList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                Sort.by("mno").descending());

        Page<Object[]> result = movieRepository.getListPage(pageable);

        // entity <=> dto : ModelMapper (dto, entity 동일한 모양일 때)

        // 방법1
        // List<MovieDTO> dtoList = new ArrayList<>();
        // result.forEach(obj -> {
        // MovieDTO dto = entityToDto((Movie) obj[0], List.of((MovieImage) obj[1]),
        // (Long) obj[2], (Double) obj[3]);
        // dtoList.add(dto);
        // });

        // 방법2
        Function<Object[], MovieDTO> function = (obj -> entityToDto((Movie) obj[0], List.of((MovieImage) obj[1]),
                (Long) obj[2], (Double) obj[3]));

        List<MovieDTO> dtoList = result.stream().map(function).collect(Collectors.toList());

        Long totalCount = result.getTotalElements();

        return PageResultDTO.<MovieDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    // 상세조회
    @Transactional(readOnly = true)
    public void getMovie(Long mno) {
        movieRepository.getMovieWithAll(mno);
    }

    private MovieDTO entityToDto(Movie movie, List<MovieImage> mImages, Long reviewCnt, Double avg) {
        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .avg(avg)
                .reviewCnt(reviewCnt)
                .createDate(movie.getCreateDate())
                .build();
        // List<MovieImage> => List<MovieImageDTO>
        List<MovieImageDTO> imageDTOs = mImages.stream().map(movieImage -> {
            return MovieImageDTO.builder()
                    .inum(movieImage.getInum())
                    .imgName(movieImage.getImgName())
                    .uuid(movieImage.getUuid())
                    .path(movieImage.getPath())
                    .build();
        }).collect(Collectors.toList());

        movieDTO.setMovieImages(imageDTOs);

        return movieDTO;
    }

}
