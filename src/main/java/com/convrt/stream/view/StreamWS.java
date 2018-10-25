package com.convrt.stream.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private boolean success;

    public StreamWS(boolean success) {
        this.success = success;
    }

    private static StreamWS setError() {
        StreamWS streamWS = new StreamWS();
        streamWS.setSuccess(false);
        return streamWS;
    }
}
