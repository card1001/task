package com.kb.task.service;

import com.kb.task.exception.ApplicationException;
import com.kb.task.exception.ErrorCode;
import com.kb.task.model.dto.response.BlogSearchResponse;
import com.kb.task.model.entity.KeywordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class SearchServiceTest {
    @Autowired
    private SearchService searchService;

    @DisplayName("키워드입력한경우 정상결과")
    @Test
    void givenKeyword_whenSearch_thenBloglist(){
        //Given
        String query = "검색어";
        Pageable pageable = PageRequest.of(1,10);
        
        //WHen
        BlogSearchResponse blogSearchResponse = searchService.blogSearch(query, pageable);
        //Then
        assertThat(blogSearchResponse).isInstanceOf(BlogSearchResponse.class);


    }

    @DisplayName("키워드를 입력하지 않은경우")
    @Test
    void givenNoKeyword_whenSearch_thenNoList(){
        //Given
        String query = "";
        Pageable pageable = PageRequest.of(1,10, Sort.by("accuracy"));

        //WHen
         assertThatThrownBy(()-> searchService.blogSearch(query, pageable))
        //Then
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.BAD_REQUEST.getMessage());

    }


    @DisplayName("최초 검색한 키워드 카운트 1")
    @Test
    void givenFirstKeyword_whenSearch_thenCnt1(){
        //Given
        String query = "검색어";

        //WHen
        Long cnt = searchService.addKeyword(query);
        //Then
        assertThat(cnt).isEqualTo(1);
    }

    @DisplayName("검색어 여러번 검색시 카운트 증가")
    @Test
    void givenKewordRepeat_whenAddKeyword_thenCntPlus(){
        //Given
        String query = "스프링";

        //WHen
        searchService.addKeyword(query);
        searchService.addKeyword(query);
        searchService.addKeyword(query);

        //Then
        StepVerifier.create(searchService.keyword())
                .assertNext(entity -> assertThat(((List<KeywordEntity>)entity).get(0).getCnt()).isGreaterThan(1L))
                .verifyComplete();
    }

    @DisplayName("인기검색어 상위10개 목록 조회")
    @Test
    void givenKeywordList_whenSearch_thenTop10KeywordList(){
        //Given
        for(int i = 0;i<20;i++){
            for(int j=0;j<i;j++){
                searchService.addKeyword("검색어" + i);
            }
        }

        //WHen

        //Then
        StepVerifier.create(searchService.keyword())
                .assertNext(entity -> assertThat((List<KeywordEntity>)entity).hasSize(10))
                .verifyComplete();
    }
}
