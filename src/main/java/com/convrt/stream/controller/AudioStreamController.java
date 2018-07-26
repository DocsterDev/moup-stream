package com.convrt.stream.controller;

import com.convrt.stream.service.StreamConversionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/stream")
public class AudioStreamController {

    @Autowired
    private StreamConversionService streamConversionService;

    @GetMapping
    public StreamingResponseBody handleRequest(@RequestParam("u") String u, HttpServletResponse response) {
        response.setContentType("audio/webm");
        response.setHeader("Content-disposition", "inline; filename=output.webm");
        byte[] decodedUrl =  Base64.getDecoder().decode(u);
        String url = new String(decodedUrl);
        return (outputStream) ->  {
            Process p = streamConversionService.convertVideo(url);
            try {
                IOUtils.copyLarge(p.getInputStream(), outputStream);
            } catch (Exception e) {
                throw new RuntimeException("Error streaming video", e);
            } finally {
                p.destroy();
                IOUtils.closeQuietly(outputStream);
            }
            log.info("STREAM CLOSED!!!");
        };
    }
}
