package com.kb.task.controller;

import com.kb.task.model.dto.response.BlogSearchResponse;
import com.kb.task.model.dto.response.Response;
import com.kb.task.model.entity.KeywordEntity;
import com.kb.task.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 키워드입력한경우_정상결과(){
        //Given
        String query = "키워드";
        Pageable pageable = PageRequest.of(1,10);

        //WHen

        //Then
        webTestClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/v2/search/blog")
                            .queryParam("query", query)
                            .queryParam("page", pageable.getPageNumber())
                            .queryParam("size", pageable.getPageSize());
                    if(pageable.getSort().isSorted()){
                        pageable.getSort().get().forEach(order -> uriBuilder.queryParam("sort", order.getProperty() + "," + order.getDirection()));
                    }
                    return uriBuilder.build();
                })
                .exchange()
                .expectStatus().isOk()
                .expectBody(BlogSearchResponse.class)
                ;
       }


    @Test
    void 키워드를_입력하지_않은경우(){
        //Given
        String query = "";

        //WHen

        //Then
        webTestClient.get()
                .uri("/v2/search/blog")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(Response.class)
        ;

    }

    @Test
    void 여러번_검색한_키워드_카운트증가(){
        //Given
        String query = "검색어";

        //WHen
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v2/search/blog")
                        .queryParam("query", query).build()
                )
                .exchange()
        ;
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v2/search/blog")
                        .queryParam("query", query).build()
                )
                .exchange()
        ;
        //Then
        webTestClient.get()
                .uri("/v2/search/rank")
                .exchange()
                .expectBodyList(KeywordEntity.class)
                .consumeWith(listEntityExchangeResult -> {
                    int cnt = listEntityExchangeResult.getResponseBody().get(0).getCnt().intValue();
                    assertThat(cnt).isGreaterThan(1);
                })
        ;

    }

    @Test
    void 인기검색어_상위10개_목록조회(){
        //Given
        String query = "스프링";
        for(int i = 0;i<20;i++){
            for(int j=0;j<i;j++){
                int finalI = i;
                webTestClient.get()
                        .uri("/v2/search/blog?query=" + query + i)
                        .exchange()
                ;
            }
        }

        //WHen

        //Then
        webTestClient.get()
                .uri("/v2/search/rank")
                .exchange()
                .expectBodyList(KeywordEntity.class)
                .hasSize(10)
        ;
    }
}
