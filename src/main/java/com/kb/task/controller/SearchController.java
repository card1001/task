package com.kb.task.controller;

import com.kb.task.model.dto.response.BlogSearchResponse;
import com.kb.task.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequestMapping("/v2/search")
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    @GetMapping("/blog")
    public ResponseEntity<BlogSearchResponse> search(
             String query,
            @PageableDefault(size=10, page=1, sort="accuracy") Pageable pageable
    ){
        log.info("start {}, {}", query, pageable);
        searchService.addKeyword(query);;
        return ResponseEntity.ok().body(searchService.blogSearch(query, pageable));
    }

    @GetMapping("/rank")
    public Mono keyword(){
        return searchService.keyword();
    }
}
