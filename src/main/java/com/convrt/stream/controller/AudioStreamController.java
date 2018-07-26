package com.convrt.stream.controller;

import com.convrt.stream.service.StreamConversionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/videos")
public class AudioStreamController {

    @Autowired
    private StreamConversionService streamConversionService;

    @GetMapping("/{videoId}/stream")
    public StreamingResponseBody handleRequest(@PathVariable("videoId") String videoId, @RequestParam("u") String url, HttpServletResponse response) {
        response.setContentType("audio/webm");
        response.setHeader("Content-disposition", "inline; filename=output.webm");
        return (outputStream) ->  {
            Process p = streamConversionService.convertVideo(url);
            try {
                IOUtils.copyLarge(p.getInputStream(), outputStream);
            } catch (Exception e) {
                throw new RuntimeException("Error streaming videoid " + videoId, e);
            } finally {
                p.destroy();
                IOUtils.closeQuietly(outputStream);
            }
            log.info("STREAM CLOSED!!!");
        };
    }
}
