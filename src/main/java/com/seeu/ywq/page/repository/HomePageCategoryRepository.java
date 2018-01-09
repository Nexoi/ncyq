package com.seeu.ywq.page.repository;

import com.seeu.ywq.page.model.HomePageCategory;
import com.seeu.ywq.page.model.HomePageCategoryPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomePageCategoryRepository extends JpaRepository<HomePageCategory, Integer> {

    HomePageCategory findByPageAndCategory(@Param("page") HomePageCategory.PAGE page, @Param("category") Integer category);

    List<HomePageCategory> findAllByPage(@Param("page") HomePageCategory.PAGE page);

    HomePageCategory findFirstByPage(@Param("page") HomePageCategory.PAGE page);

    void deleteByPageAndCategory(@Param("page") HomePageCategory.PAGE page, @Param("category") Integer category);
}
