package com.seeu.ywq.release.repository.apppage;

import com.seeu.ywq.release.model.Publish;
import com.seeu.ywq.release.model.apppage.PageConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PageConfigRepository extends JpaRepository<PageConfig, Long> {
    @Query(value = "select new Publish( ) from PageConfig cfg, Publish p where cfg.category = :category and cfg.srcId = p.id")
    List<Publish> findXXByCategoryFromConfig(@Param("category") Integer category);

//    @Query(value = "select p.* from ywq_page_configurer cfg, ywq_publish p where cfg.category = :category and cfg.src_id = p.id", nativeQuery = true)
//    List<Publish> findXXByCategoryFromConfig(@Param("category") Integer category);
}
