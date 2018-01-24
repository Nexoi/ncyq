//package com.seeu.ywq.page.model;
//
//import java.io.Serializable;
//
//public class HomePageCategoryPKeys implements Serializable {
//    private HomePageCategory.PAGE page;
//    private Integer category;
//
//    public HomePageCategoryPKeys() {
//    }
//
//    public HomePageCategoryPKeys(HomePageCategory.PAGE page, Integer category) {
//        this.page = page;
//        this.category = category;
//    }
//
//    public Integer getCategory() {
//        return category;
//    }
//
//    public void setCategory(Integer category) {
//        this.category = category;
//    }
//
//    public HomePageCategory.PAGE getPage() {
//        return page;
//    }
//
//    public void setPage(HomePageCategory.PAGE page) {
//        this.page = page;
//    }
//
//    @Override
//    public int hashCode() {
//        final int PRIME = 31;
//        int result = 1;
//        result = PRIME * result + ((page == null) ? 0 : page.hashCode());
//        result = PRIME * result + ((category == null) ? 0 : category.hashCode());
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//
//        final HomePageCategoryPKeys other = (HomePageCategoryPKeys) obj;
//        if (page == null) {
//            if (other.page != null) {
//                return false;
//            }
//        } else if (!page.equals(other.page)) {
//            return false;
//        }
//        if (category == null) {
//            if (other.category != null) {
//                return false;
//            }
//        } else if (!category.equals(other.category)) {
//            return false;
//        }
//        return true;
//    }
//}
