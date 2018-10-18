package com.convrt.stream.controller;

import com.convrt.stream.service.StreamConversionService;
import com.convrt.stream.service.StreamUrlService;
import com.convrt.stream.utils.UserAgentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
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
        String url = streamUrlService.fetchStreamUrl(videoId, token);
        return (output) ->  {
            ProcessBuilder pb = streamConversionService.convertVideo(url);
            Process p = pb.start();
            try (InputStream input = p.getInputStream(); InputStream es = p.getErrorStream();) {
                String error = org.apache.commons.io.IOUtils.toString(es, "UTF-8");
                if (Objects.nonNull(error)) {
                    log.error("ERROR MESSAGE IN STREAM PROCESS: {}", error);
                }
                IOUtils.copy(input, output);
            } finally {
                log.info("Stream closed for stream playing video id {}", videoId);
            }
        };
    }
}
