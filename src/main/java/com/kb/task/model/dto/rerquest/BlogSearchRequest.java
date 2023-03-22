package com.kb.task.model.dto.rerquest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlogSearchRequest {

    private String query;
    private String sort;
    private Integer page;
    private Integer size;

}
