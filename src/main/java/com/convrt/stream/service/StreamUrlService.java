package com.convrt.stream.service;

import com.convrt.stream.view.Video;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class StreamUrlService {

    private static final String host = "http://172.31.10.241:8083";
    private static final String hostLocal = "http://localhost:8083";

    public String fetchStreamUrl(Video video){
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/api/videos/%s/metadata", host, video.getId());
        HttpEntity<Video> request = new HttpEntity<>(video);
        Video videoResp = restTemplate.postForObject(url, request, Video.class);
        return Optional.ofNullable(videoResp.getStreamUrl()).orElseThrow(() -> new RuntimeException(String.format("Cannot retrieve stream url for videoId %s", video.getId())));
    }

}
