package com.seeu.ywq.release.service;

import com.seeu.ywq.release.dvo.User$TagVO;
import com.seeu.ywq.release.dvo.TagVO;
import com.seeu.ywq.release.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    TagVO findOne(Long id);

    List<TagVO> findAll();

    Page findAll(Pageable pageable);

    TagVO add(Tag tag);

    TagVO update(Tag tag);

    void deleteFake(Long id);

    STATUS addMine(Long uid, Long[] ids); // 添加自己的标签

    STATUS addFocus(Long uid, Long[] ids); // 添加自己关注的标签

    List<User$TagVO> findAllMine(Long uid); // 找到自己所有的标签

    List<User$TagVO> findAllFocus(Long uid); // 找到自己关注的所有标签

    List<User$TagVO> deleteMine(Long uid, Long[] ids); // 删除自己的标签

    List<User$TagVO> deleteFocus(Long uid, Long[] ids); // 删除自己关注的标签

    public enum STATUS {
        hasAdded,
        success,
        failure
    }

}
