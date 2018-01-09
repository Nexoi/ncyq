package com.seeu.ywq.page.service.impl;

import com.seeu.ywq.page.model.HomePageCategory;
import com.seeu.ywq.page.model.HomePageCategoryPKeys;
import com.seeu.ywq.page.repository.HomePageCategoryRepository;
import com.seeu.ywq.page.service.HomePageCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class HomePageCategoryServiceImpl implements HomePageCategoryService {
    @Resource
    private HomePageCategoryRepository repository;

    @Override
    public HomePageCategory findById(HomePageCategory.PAGE page, Integer category) {
        return repository.findByPageAndCategory(page, category);
    }

    @Override
    public HomePageCategory findFirstByPage(HomePageCategory.PAGE page) {
        return repository.findFirstByPage(page);
    }

    @Override
    public List<HomePageCategory> findAllByPage(HomePageCategory.PAGE page) {
        return repository.findAllByPage(page);
    }

    @Override
    public List<HomePageCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public HomePageCategory save(HomePageCategory category) {
        return repository.save(category);
    }

    @Transactional
    @Override
    public void delete(HomePageCategory.PAGE page, Integer category) {
        repository.deleteByPageAndCategory(page, category);
    }
}
