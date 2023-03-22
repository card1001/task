package com.kb.task.model.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Meta {
    private Long total_count;
    private Long pageable_count;
    private Boolean is_end;
}
