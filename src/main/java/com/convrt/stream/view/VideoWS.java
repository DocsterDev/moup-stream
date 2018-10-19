package com.convrt.stream.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoWS {
    private String id;
    private String streamUrl;
    private boolean audioOnly;
}
