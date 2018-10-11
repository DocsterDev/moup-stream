package com.convrt.stream.service;

import com.convrt.stream.view.VideoWS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
public class StreamUrlService {

    @Value("${server.apiUrl}")
    private String apiUrl;

    public String fetchStreamUrl(String videoId, String token) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/api/videos/%s/metadata?token=%s", apiUrl, videoId, token);
        VideoWS videoResp = restTemplate.getForObject(url, VideoWS.class);
        log.info("Stream URL returned for video: {}", videoResp.getStreamUrl());
        return Optional.ofNullable(videoResp.getStreamUrl()).orElseThrow(() -> new RuntimeException(String.format("Cannot retrieve stream url for videoId %s", videoId)));
    }

}
