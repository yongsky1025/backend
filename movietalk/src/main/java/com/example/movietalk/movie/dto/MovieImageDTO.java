package com.example.movietalk.movie.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.example.movietalk.movie.entity.Movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovieImageDTO {

    private Long inum;

    private String uuid;

    private String path;

    private String imgName;

    public String getThumbnailURL() {
        String thumbFullPath = "";

        // java.net.URL~~
        try {
            thumbFullPath = URLEncoder.encode(path + "/s_" + uuid + "_" + imgName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return thumbFullPath;
    }

    public String getImageURL() {
        String fullPath = "";

        // java.net.URL~~
        try {
            fullPath = URLEncoder.encode(path + "/" + uuid + "_" + imgName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fullPath;
    }

}
