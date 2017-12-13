package com.seeu.core;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neo on 15/10/2017.
 * <p>
 * 根据分页结果进行辅助添加信息，信息包含：具体的页码
 */
@Service("pageInfoUtil")
public class Util4PageIndex {

    /**
     * @param currentPageIndex 当前页码
     * @param totalPages       总页码
     * @return 最多 10 页的页码数据
     */
    public List<Integer> genPageIndexList(Integer currentPageIndex, Integer totalPages) {
        if (currentPageIndex < 0) currentPageIndex = 0;
        if (currentPageIndex > totalPages) currentPageIndex = totalPages;
        List<Integer> list = new ArrayList<>();
        int startIndex = currentPageIndex - 5; // 确保前面只有 5 页的页码
        startIndex = startIndex < 0 ? 0 : startIndex;
        for (int i = 0; i < 10; i++) { // 确保最多 10 页
            if (startIndex < totalPages) {
                list.add(startIndex++); // 确保页码从 0 开始
            } else {
                break;
            }
        }
        return list;
    }
}
