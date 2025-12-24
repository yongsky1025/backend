package com.example.movietalk.common;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.movietalk.movie.dto.MovieImageDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Controller
@RequestMapping("/upload")
public class UploadController {

    @Value("${com.example.movietalk.upload.path}")
    private String uploadPath;

    @GetMapping("/upload")
    public void getUpload() {
        log.info("업로드 폼 요청");
    }

    @ResponseBody
    @PostMapping("/upload")
    public List<MovieImageDTO> postUpload(MultipartFile[] uploadFiles) {

        // 이미지 화면에 보여주기

        // 폴더에 저장
        // 1. 폴더 생성 메소드 호출
        String saveDirPath = makeDir();
        List<MovieImageDTO> upList = new ArrayList<>();

        for (MultipartFile file : uploadFiles) {
            log.info("OriginalFilename {}", file.getOriginalFilename());
            log.info("Size {}", file.getSize());
            log.info("Content Type {}", file.getContentType());

            // uuid 생성
            String uuid = UUID.randomUUID().toString();
            // 파일명
            String oriName = file.getOriginalFilename();
            // 저장되는 파일명
            String saveName = uploadPath + File.separator + saveDirPath + File.separator + uuid + "_" + oriName;

            upList.add(MovieImageDTO.builder().imgName(oriName).uuid(uuid).path(saveDirPath).build());

            try {
                // 저장
                file.transferTo(new File(saveName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return upList;

    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "utf-8");
            File file = new File(uploadPath + File.separator + srcFileName);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return result;
    }

    // 폴더 생성 메소드
    private String makeDir() {

        // new Date(); => SimpleDateFormat

        // 오늘날짜 구하기
        LocalDate today = LocalDate.now(); // 2025-12-24 => 2025/12/24
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        // 디렉토리 생성
        File file = new File(uploadPath, dateStr);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dateStr;
    }

}
