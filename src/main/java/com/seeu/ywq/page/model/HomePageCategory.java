package com.seeu.ywq.page.model;

import com.seeu.ywq.page.dvo.HomePageVOUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "ywq_page_category")
public class HomePageCategory {

    public enum PAGE {
        home,
        hotsperson,
        youwu
    }

    @NotNull
    private PAGE page;
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer category;
    @Transient
    private String pageName;
    @NotNull
    private String categoryTitle;
    @Transient
    private List<HomePageVOUser> data;

    public PAGE getPage() {
        return page;
    }

    public void setPage(PAGE page) {
        this.page = page;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public List<HomePageVOUser> getData() {
        return data;
    }

    public void setData(List<HomePageVOUser> data) {
        this.data = data;
    }
}
