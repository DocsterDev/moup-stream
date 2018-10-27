package com.convrt.stream.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StreamWS {

    public static final StreamWS ERROR = setError();

    private String id;
    private String extension;
    private String streamUrl;
    private String source;
    private boolean audioOnly;
    private boolean matchesExtension;
    private boolean success;

    public StreamWS(boolean success) {
        this.success = success;
    }

    private static StreamWS setError() {
        StreamWS streamWS = new StreamWS();
        streamWS.setSuccess(false);
        return streamWS;
    }

    public StreamWS (String streamUrl, String extension) {
        setStreamUrl(streamUrl, extension);
    }

    private void setStreamUrl(String streamUrl, String extension) {
        this.streamUrl = streamUrl;
        this.extension = extension;
        this.audioOnly = false;
        this.success = true;
        if (streamUrl != null) {
            MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(streamUrl).build().getQueryParams();
            List<String> expire = parameters.get("expire");
            List<String> mime = parameters.get("mime");
            if (!mime.isEmpty()) {
                String mimeStr = mime.get(0);
                if (StringUtils.isNotBlank(mimeStr)) {
                    this.audioOnly = mimeStr.contains("audio");
                    if (StringUtils.isNotBlank(this.extension)) {
                        this.matchesExtension = this.audioOnly && mimeStr.contains(this.extension.equals("m4a") ? "mp4":this.extension);
                    }
                }
            }
        }
    }
}
