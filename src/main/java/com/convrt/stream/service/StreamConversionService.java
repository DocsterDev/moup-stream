package com.convrt.stream.service;

import com.convrt.stream.utils.UserAgentService;
import com.convrt.stream.view.UserSettingsWS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class StreamConversionService {

    @Autowired
    private UserAgentService userAgentService;

    public Process convertVideo(String url) {
            ProcessBuilder pb;
            UserSettingsWS userSettings = new UserSettingsWS();
            userSettings.setSampleRate(BigDecimal.valueOf(128));
            userSettings.setBitrate(BigDecimal.valueOf(48000));
            if (userAgentService.isChrome()) {
                pb = getAudioEncoderWebm(userSettings, url);
            } else {
                pb = getAudioEncoderMp3(userSettings, url);
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


    private ProcessBuilder getAudioEncoderMp3(UserSettingsWS userSettings, String url) {
        return new ProcessBuilder("ffmpeg",
                "-i", url,
                "-progress",
                "progress",
                "-vn",
                "-c:a",
                "libmp3lame",
                "-b:a",
                String.format("%sk", userSettings.getSampleRate()),
                "-ar",
                userSettings.getBitrate().toString(),
                "-compression_level",
                "10",
                "-y",
                "-f",
                "mp3",
                "-"
        );
    }

    private ProcessBuilder getAudioEncoderWebm(UserSettingsWS userSettings, String url) {
        return new ProcessBuilder("ffmpeg",
                "-i", url,
                "-progress",
                "progress",
                "-vn",
                "-c:a",
                "libopus",
                "-b:a",
                String.format("%sk", userSettings.getSampleRate()),
                "-ar",
                userSettings.getBitrate().toPlainString(), // 48000 24000 16000 12000 8000
                "-compression_level",
                "10",
                "-y",
                "-f",
                "webm",
                "-"
        );
    }

    private ProcessBuilder getAudioEncoderOgg(UserSettingsWS userSettings, String url) {
        return new ProcessBuilder("ffmpeg",
                "-i", url,
                "-progress",
                "progress",
                "-vn",
                "-acodec",
                "libvorbis",
                "-ab",
                String.format("%sk", userSettings.getSampleRate()),
                "-ar",
                userSettings.getBitrate().toPlainString(),
                "-ac",
                "2",
                "-compression_level",
                "10",
                "-y",
                "-f",
                "ogg",
                "-"
        );
    }


    private String getStreamUrl(String videoId){
        return null;
    }

}
