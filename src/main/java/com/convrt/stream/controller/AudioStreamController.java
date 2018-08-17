package com.convrt.stream.controller;

import com.convrt.stream.service.StreamConversionService;
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

@Slf4j
@RestController
@RequestMapping("/stream")
public class AudioStreamController {

    @Autowired
    private StreamConversionService streamConversionService;

    @GetMapping
    public StreamingResponseBody handleRequest(@RequestParam("u") String u, @RequestHeader("User-Agent") String userAgent, HttpServletResponse response) {
        response.setContentType("audio/webm");
        response.setHeader("Content-disposition", "inline; filename=output.webm");
        Parser parser;
        try {
            parser = new Parser();
        } catch (IOException ioException) {
            throw new RuntimeException(String.format("Cannot read user agent string: %s", userAgent));
        }
        Client c = parser.parse(userAgent);
        if (u == null) {
            throw new RuntimeException("No stream URL provided");
        }
        byte[] decodedUrl =  Base64.getDecoder().decode(u);
        String url = new String(decodedUrl);
        return (outputStream) ->  {
            Process p = streamConversionService.convertVideo(url, c.userAgent.family);
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
