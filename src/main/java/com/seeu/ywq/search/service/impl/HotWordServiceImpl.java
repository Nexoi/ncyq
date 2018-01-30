package com.seeu.ywq.search.service.impl;

import com.seeu.ywq.search.model.HotWord;
import com.seeu.ywq.search.repository.HotWordRepository;
import com.seeu.ywq.search.service.HotWordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 30/01/2018
 * Time: 3:27 PM
 * Describe:
 */
@Service
public class HotWordServiceImpl implements HotWordService {
    @Resource
    private HotWordRepository repository;

    @Override
    public List<HotWord> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<HotWord> findAll(Pageable pageable) {
        return repository.findAllByOrderByCreateTimeDesc(pageable);
    }

    @Override
    public HotWord add(String word) {
        HotWord wd = new HotWord();
        wd.setWord(word);
        wd.setCreateTime(new Date());
        return repository.save(wd);
    }

    @Override
    public void delete(String word) {
        repository.delete(word);
    }
}
