package com.seeu.ywq.search.repository;

import com.seeu.ywq.search.model.HotWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by suneo.
 * User: neo
 * Date: 30/01/2018
 * Time: 3:25 PM
 * Describe:
 */

public interface HotWordRepository extends JpaRepository<HotWord, String> {

    Page<HotWord> findAllByOrderByCreateTimeDesc(Pageable pageable);
}
