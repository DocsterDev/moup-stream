package com.convrt.stream.service;

import com.convrt.stream.view.StreamWS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
public class StreamUrlService {

    @Value("${server.apiUrl}")
    private String apiUrl;

    public StreamWS fetchStreamUrl(String videoId, String userAgent) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // TODO - Overriding to simulate a Chrome browser to get a webm format if available
        userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/53";
        headers.add("User-Agent", userAgent);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String url = String.format("%s/api/videos/%s/stream", apiUrl, videoId);
        ResponseEntity<StreamWS> streamWS = restTemplate.exchange(url, HttpMethod.GET, entity, StreamWS.class);
        StreamWS streamInfo = streamWS.getBody();
        if (streamWS == null || streamInfo == null || streamInfo.getRecommendedFormat() == null) {
            throw new RuntimeException(String.format("Error fetching URL for video id %s", videoId));
        }
        log.info("Stream URL returned for video: {}", streamInfo.getRecommendedFormat().getUrl());
        return streamInfo;
    }

}
