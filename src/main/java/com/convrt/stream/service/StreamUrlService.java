package com.convrt.stream.service;

import com.convrt.stream.view.Video;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
public class StreamUrlService {

    // private static final String host = "http://172.31.10.241:8083";
    private static final String host = "http://localhost:8083";

    public String fetchStreamUrl(String videoId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/api/videos/%s/metadata", host, videoId);
        Video videoResp = restTemplate.getForObject(url, Video.class);
        log.info("Stream URL returned for video: {}", videoResp.getStreamUrl());
        return Optional.ofNullable(videoResp.getStreamUrl()).orElseThrow(() -> new RuntimeException(String.format("Cannot retrieve stream url for videoId %s", videoId)));
    }

}
