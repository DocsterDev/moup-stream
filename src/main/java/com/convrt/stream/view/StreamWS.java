package com.convrt.stream.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StreamWS {
    private String id;
    private String title;
    private String owner;
    private String description;
    private long duration;
    private LocalDate uploadDate;
    private boolean isChrome;
    private StreamFormatWS recommendedFormat;
    private List<StreamFormatWS> formats;

    public StreamWS (String url, String extension) {
        StreamFormatWS streamFormat = new StreamFormatWS();
        streamFormat.setUrl(url);
        streamFormat.setExtension(extension);
        setRecommendedFormat(streamFormat);
    }
}
