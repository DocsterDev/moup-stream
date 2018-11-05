package com.convrt.stream.service;

import com.convrt.stream.utils.UserAgentService;
import com.convrt.stream.view.StreamWS;
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

    public ProcessBuilder convertVideo(StreamWS streamWS) {
            ProcessBuilder pb;
            UserSettingsWS userSettings = new UserSettingsWS();
            userSettings.setSampleRate(BigDecimal.valueOf(128));
            userSettings.setBitrate(BigDecimal.valueOf(48000));
            if (userAgentService.isChrome()) {
                pb = getAudioEncoderWebm(userSettings, streamWS);
            } else {
                pb = getAudioEncoderMp3(userSettings, streamWS);
            }
        //pb.redirectErrorStream(true);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        pb.redirectOutput(ProcessBuilder.Redirect.PIPE);

        return pb;
    }

    private ProcessBuilder getAudioEncoderMp3(UserSettingsWS userSettings, StreamWS streamWS) {
        return new ProcessBuilder("ffmpeg",
                //"-re",
                "-i",
                streamWS.getStreamUrl(),
                "-vn",
                "-c:a",
                "libmp3lame",
//                "-map_metadata",
//                "-1",
//                "-b:a",
//                String.format("%sk", userSettings.getSampleRate()),
//                "-ar",
//                userSettings.getBitrate().toString(),
                "-y",
                "-f",
                "mp3",
                "-"
        );
    }

    private ProcessBuilder getAudioEncoderWebm(UserSettingsWS userSettings, StreamWS streamWS) {
        return new ProcessBuilder("ffmpeg",
                //"-re",
                "-i",
                streamWS.getStreamUrl(),
                "-vn",
                "-c:a",
                "libopus",
//                "-map_metadata",
//                "-1",
//                "-b:a",
//                String.format("%sk", userSettings.getSampleRate()),
//                "-ar",
//                userSettings.getBitrate().toPlainString(), // 48000 24000 16000 12000 8000
                "-compression_level",
                "10",
                "-y",
                "-f",
                "webm",
                "-"
        );
    }
}
