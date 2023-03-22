package com.kb.task.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="keyword")
public class KeywordEntity {

    @Id
    @Column(name="keyword")
    private String keyword;
    @Column(name="cnt")
    private Long cnt;
    @PrePersist
    void initCnt(){
        this.cnt = (this.cnt == null ? 1L : this.cnt+1);
    }

    public static KeywordEntity of(String keyword) {
        KeywordEntity keywordEntity = new KeywordEntity();
        keywordEntity.setKeyword(keyword);
        return keywordEntity;
    }
}
