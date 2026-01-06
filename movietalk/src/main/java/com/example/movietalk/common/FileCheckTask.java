package com.example.movietalk.common;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.movietalk.movie.dto.MovieImageDTO;
import com.example.movietalk.movie.entity.MovieImage;
import com.example.movietalk.movie.repository.MovieImageRepository;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class FileCheckTask {

    @Autowired
    private MovieImageRepository movieImageRepository;

    @Value("${com.example.movietalk.upload.path}")
    private String uploadPath;

    // 전일자 폴더 추출
    private String getFolderYesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        // LocalDate => String
        String str = yesterday.toString(); // "2026-01-05"
        // - => 폴더 구분자
        return str.replace("-", File.separator);

    }

    @Scheduled(cron = "* * 2 * * *")
    public void checkFile() {
        log.info("file check test");

        // 어제날짜의 MovieImage 데이터베이스 가져오기
        String path = LocalDate.now()
                .minusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        List<MovieImage> oldImages = movieImageRepository.getOldFileImages(path);

        // entity => dto
        List<MovieImageDTO> movieImageDTOs = oldImages.stream().map(e -> {
            return MovieImageDTO.builder()
                    .inum(e.getInum())
                    .uuid(e.getUuid())
                    .imgName(e.getImgName())
                    .path(e.getPath())
                    .build();
        }).collect(Collectors.toList());

        // 파일제거 시 원본파일 + s_
        List<Path> fileList = movieImageDTOs.stream()
                // c:\\upload\\2026\\01\\05\\uuuid_파일명
                .map(dto -> Paths.get(uploadPath, dto.getPath(), dto.getUuid() + "_" + dto.getImgName()))
                .collect(Collectors.toList());

        movieImageDTOs.stream()
                .map(dto -> Paths.get(uploadPath, dto.getPath(), "s_" + dto.getUuid() + "_" + dto.getImgName()))
                .forEach(p -> fileList.add(p));

        // 데이터베이스 파일과 폴더 파일 비교하기
        // c:\\upload\\2026\\01\\05
        File tarFile = Paths.get(uploadPath, getFolderYesterday()).toFile();

        File[] removeFiles = tarFile.listFiles(f -> fileList.contains(f.toPath()) == false);

        if (removeFiles != null) {
            for (File file : removeFiles) {
                log.warn(file.getAbsolutePath());
                file.delete();
            }
        }
    }
}
