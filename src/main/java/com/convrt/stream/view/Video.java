package com.convrt.stream.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Video {

    private String id;
    private String title;
    private String owner;
    private String duration;
    private String streamUrl;

}
