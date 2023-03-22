package com.kb.task.client;

import com.kb.task.exception.ApplicationException;
import com.kb.task.exception.ErrorCode;
import com.kb.task.model.dto.response.BlogSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ClientUtil {

    private static String restApiKey;
    private static String searchDomain;

    @Value("${rest-api-key.kakao}")
    public void setRestApiKey(String value){
        restApiKey = value;
    }
    @Value("${search-domain.kakao}")
    public void setSearchDomain(String value){
        searchDomain = value;
    }

    public static BlogSearchResponse blogSearch(String query, Pageable pageable){
        return getClient()
                .get()
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
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.just(new ApplicationException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.just(new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage())))
                .bodyToMono(BlogSearchResponse.class)
                .block();
    }

    private static WebClient getClient(){
        ExchangeFilterFunction logRequest = ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.info("Request: {} {}", request.method(), request.url());
            request.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(request);
        });
        ExchangeFilterFunction logResponse = ExchangeFilterFunction.ofResponseProcessor(response -> {
            log.info("Response: {} {}", response.statusCode(), response.headers().asHttpHeaders());
            return Mono.just(response);
        });
        return WebClient.builder()
                .baseUrl(searchDomain)
                .defaultHeader("Authorization", "KakaoAK " + restApiKey)
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
                .filter(logRequest)
                .filter(logResponse)
                .build();
    }
}
