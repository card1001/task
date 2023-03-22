package com.kb.task.service;

import com.kb.task.client.ClientUtil;
import com.kb.task.model.dto.response.BlogSearchResponse;
import com.kb.task.model.entity.KeywordEntity;
import com.kb.task.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final SearchRepository searchRepository;
    public BlogSearchResponse blogSearch(String query, Pageable pageable){
        return ClientUtil.blogSearch(query, pageable);
    }
    @Transactional
    public Long addKeyword(String keyword){
        if(searchRepository.findByKeyword(keyword).isPresent())
            return searchRepository.updateCnt(keyword).longValue();
        return searchRepository.save(KeywordEntity.of(keyword)).getCnt();
    }
    public Mono keyword(){
        return Mono.just(searchRepository.findTop10ByOrderByCntDesc()).log();
    }
}
