package com.convrt.stream.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "video")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Video {

    @Id
    @Column(name = "id", length = 20)
    private String id;

    @Column(name = "stream_url_date")
    private Instant streamUrlDate;

    @Column(name = "stream_url_expire_date")
    private Instant streamUrlExpireDate;

    @Column(name = "stream_url", length = 1000)
    private String streamUrl;

}
