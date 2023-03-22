package com.kb.task.model.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
@Getter
@RequiredArgsConstructor
public class Document {
    private String title;
    private String contents;
    private String url;
    private String blogname;
    private String thumbnail;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime datetime;
}
