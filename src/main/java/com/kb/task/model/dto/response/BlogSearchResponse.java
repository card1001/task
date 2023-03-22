package com.kb.task.model.dto.response;

import lombok.*;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BlogSearchResponse {
    private Meta meta;
    private List<Document> documents;
}
