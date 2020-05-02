package com.moup.stream.view;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @NoArgsConstructor
public class UserSettingsWS {

    private BigDecimal bitrate;
    private BigDecimal sampleRate;
}
