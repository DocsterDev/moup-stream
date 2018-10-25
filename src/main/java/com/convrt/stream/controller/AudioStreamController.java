package com.convrt.stream.controller;

import com.convrt.stream.service.StreamConversionService;
import com.convrt.stream.service.StreamUrlService;
import com.convrt.stream.utils.UserAgentService;
import com.convrt.stream.view.VideoWS;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/stream")
public class AudioStreamController {

    @Autowired
    private StreamConversionService streamConversionService;
    @Autowired
    private StreamUrlService streamUrlService;
    @Autowired
    private UserAgentService userAgentService;

    @GetMapping
    public StreamingResponseBody handleRequest(@RequestHeader("User-Agent") String userAgent,
                                               @RequestParam("v") String videoId,
                                               @RequestParam(value = "token", required = false) String token,
                                               HttpServletResponse response) {
        if (videoId == null) {
            throw new RuntimeException("No videoId provided");
        }
        userAgentService.parseUserAgent(userAgent);
        String fileType = userAgentService.isChrome() ? "webm" : "mp3";
        response.setContentType(String.format("audio/%s", fileType));
        response.setHeader("Content-disposition", String.format("inline; filename=output.%s", fileType));
        VideoWS videoWS = streamUrlService.fetchStreamUrl(videoId, token);
        return (output) ->  {
           // if (!videoWS.isAudioOnly() || !userAgentService.isChrome()) {
                log.info("Streaming url through ffmpeg for browser {}", userAgentService.getBrowserFamily());
                Process p = streamConversionService.convertVideo(videoWS.getStreamUrl()).start();
                try (InputStream input = p.getInputStream(); InputStream es = p.getErrorStream();) {
                    String error = org.apache.commons.io.IOUtils.toString(es, "UTF-8");
                    if (Objects.nonNull(error)) {
                        log.error("ERROR MESSAGE IN STREAM PROCESS: {}", error);
                    }
                    try {
                        IOUtils.copy(input, output);
                    } catch (Exception e) {
                        log.error("IO EXCEPTION: {}", e.getMessage());
                    }
                } finally {
                    log.info("Stream closed for stream playing video id {}", videoId);
                }
//            } else {
//                log.info("Streaming origin stream url for browser {}", userAgentService.getBrowserFamily());
//                URL url = new URL(videoWS.getStreamUrl());
//                try (InputStream input = url.openStream()) {
//                    IOUtils.copy(input, output);
//                } finally {
//                    log.info("Stream closed for stream playing video id {}", videoId);
//                }
//            }
        };
    }
}
