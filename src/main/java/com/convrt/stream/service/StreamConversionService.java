package com.convrt.stream.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StreamConversionService {

    public Process convertVideo(String url, String agent) {
            ProcessBuilder pb;

            if ("Chrome".equals(agent)) {
                pb = new ProcessBuilder("ffmpeg",
                        "-i", url,
                        "-progress",
                        "progress",
                        "-vn",
                        "-c:a",
                        "libopus",
                        "-b:a",
                        "40k",
                        "-ar",
                        "24000", // 48000 24000 16000 12000 8000
                        "-compression_level",
                        "10",
                        "-y",
                        "-f",
                        "webm",
                        "-"
                );
            } else {
                pb = new ProcessBuilder("ffmpeg",
                        "-i", url,
                        "-progress",
                        "progress",
                        "-vn",
                        "-c:a",
                        "libmp3lame",
                        "-b:a",
                        "40k",
                        "-ar",
                        "22050",
                        "-compression_level",
                        "10",
                        "-y",
                        "-f",
                        "mp3",
                        "-"
                );
            }
        //pb.redirectErrorStream(true);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        pb.redirectOutput(ProcessBuilder.Redirect.PIPE);

        Process p;
        try {
            p = pb.start();
        } catch (Exception e) {
            throw new RuntimeException("Cannot start audio conversion process");
        }
        return p;
    }

}
