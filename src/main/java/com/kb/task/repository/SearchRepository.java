package com.kb.task.repository;

import com.kb.task.model.entity.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SearchRepository extends JpaRepository<KeywordEntity, Long> {

    Optional<KeywordEntity> findByKeyword(String keyword);
    @Modifying
    @Query("UPDATE KeywordEntity SET cnt = cnt +1 WHERE keyword = :keyword")
    Integer updateCnt(String keyword);

    List<KeywordEntity> findTop10ByOrderByCntDesc();
}
