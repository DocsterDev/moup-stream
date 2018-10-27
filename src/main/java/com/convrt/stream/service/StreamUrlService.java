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
        headers.add("User-Agent", userAgent);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String url = String.format("%s/api/videos/%s/stream", apiUrl, videoId);
        ResponseEntity<StreamWS> streamWS = restTemplate.exchange(url, HttpMethod.GET, entity, StreamWS.class);
        StreamWS streamInfo = streamWS.getBody();
        log.info("Stream URL returned for video: {}", streamInfo.getStreamUrl());
        if (streamInfo == null || !streamInfo.isSuccess() || streamInfo.getStreamUrl() == null) {
            throw new RuntimeException(String.format("Error fetching URL for video id %s", videoId));
        }
        return streamInfo;
    }

}
