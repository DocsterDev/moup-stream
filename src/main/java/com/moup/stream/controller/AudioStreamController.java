package com.moup.stream.controller;

import com.moup.stream.service.StreamConversionService;
import com.moup.stream.service.StreamUrlService;
import com.moup.stream.utils.UserAgentService;
import com.moup.stream.view.StreamWS;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Base64;
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

    @GetMapping("/videos/{videoId}")
    public StreamingResponseBody handleStreamRequest(@RequestHeader("User-Agent") String userAgent, @PathVariable("videoId") String videoId, HttpServletResponse response) {
        if (videoId == null) {
            throw new RuntimeException("No videoId provided");
        }
        userAgentService.parseUserAgent(userAgent);
        String fileType = userAgentService.isChrome() ? "webm" : "mp3";
        response.setContentType(String.format("audio/%s", fileType));
        response.setHeader("Content-disposition", String.format("inline; filename=output.%s", fileType));
        StreamWS streamWS = streamUrlService.fetchStreamUrl(videoId, userAgent);
        return (output) ->  {
                log.info("Streaming url through ffmpeg for browser {}", userAgentService.getBrowserFamily());
                Process p = streamConversionService.convertVideo(streamWS).start();
                try (InputStream input = p.getInputStream(); InputStream es = p.getErrorStream();) {
                    String error = org.apache.commons.io.IOUtils.toString(es, "UTF-8");
                    if (Objects.nonNull(error)) {
                        log.error("ERROR MESSAGE IN STREAM PROCESS: {}", error);
                    }
                    try {
                        IOUtils.copyLarge(input, output);
                    } catch (Exception e) {
                        log.error("IO EXCEPTION: {}", e.getMessage());
                    }
                } finally {
                    IOUtils.closeQuietly(output);
                    log.info("Stream closed for stream playing video id {}", videoId);
                }
        };
    }

    @GetMapping("/{streamUrl}")
    public StreamingResponseBody handleStreamUrlRequest(@RequestHeader("User-Agent") String userAgent, @PathVariable("streamUrl") String streamUrl, HttpServletResponse response) {
        if (streamUrl == null) {
            throw new RuntimeException("Cannot stream: Stream URL is null.");
        }
        byte[] decodedBytes = Base64.getDecoder().decode(streamUrl);
        String decodedString = new String(decodedBytes);
        userAgentService.parseUserAgent(userAgent);
        String fileType = userAgentService.isChrome() ? "webm" : "mp3";
        response.setContentType(String.format("audio/%s", fileType));
        response.setHeader("Content-disposition", String.format("inline; filename=output.%s", fileType));
        return (output) ->  {
            log.info("Streaming url through ffmpeg for browser {}", userAgentService.getBrowserFamily());
            StreamWS streamWS = new StreamWS(decodedString, userAgentService.isChrome() ? "webm" : "m4a");
            Process p = streamConversionService.convertVideo(streamWS).start();
            try (InputStream input = p.getInputStream(); InputStream es = p.getErrorStream();) {
                String error = org.apache.commons.io.IOUtils.toString(es, "UTF-8");
                if (Objects.nonNull(error)) {
                    log.error("ERROR MESSAGE IN STREAM PROCESS: {}", error);
                }
                try {
                    IOUtils.copyLarge(input, output);
                } catch (Exception e) {
                    log.error("IO EXCEPTION: {}", e.getMessage());
                }
            } finally {
                IOUtils.closeQuietly(output);
                log.info("Stream closed for stream playing");
            }
        };
    }
}
