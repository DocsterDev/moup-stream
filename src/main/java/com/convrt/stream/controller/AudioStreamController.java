package com.convrt.stream.controller;

import com.convrt.stream.service.StreamConversionService;
import com.convrt.stream.service.StreamUrlService;
import com.convrt.stream.utils.UserAgentService;
import com.convrt.stream.view.Video;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

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
        return (outputStream) ->  {
            Process p = streamConversionService.convertVideo(url);
            try {
                IOUtils.copyLarge(p.getInputStream(), outputStream);
            } catch (Exception e) {
                throw new RuntimeException("Error streaming video", e);
            } finally {
                IOUtils.closeQuietly(outputStream);
                p.destroy();
                log.info("CLOSED OUTPUT STREAM !!!!");
            }
            log.info("STREAM CLOSED!!!");
        };
    }
}
