package com.seeu.ywq.search.service;

import com.seeu.ywq.search.model.HotWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 30/01/2018
 * Time: 3:26 PM
 * Describe:
 */

public interface HotWordService {
    List<HotWord> findAll();

    Page<HotWord> findAll(Pageable pageable);

    HotWord add(String word);

    void delete(String word);
}
