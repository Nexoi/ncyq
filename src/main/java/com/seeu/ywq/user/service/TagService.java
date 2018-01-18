package com.seeu.ywq.user.service;

import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.user.dvo.TagVO;
import com.seeu.ywq.user.dvo.UserTagVO;
import com.seeu.ywq.user.model.Tag;
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

    void resetMine(Long uid, Long[] ids) throws ResourceNotFoundException;

    void resetFocus(Long uid, Long[] ids) throws ResourceNotFoundException;

    void addTags(Long uid, Long[] ids) throws ResourceNotFoundException; // 添加自己关注的标签

    List<UserTagVO> findAllVO(Long uid); // 找到自己所有的标签

    List<UserTagVO> deleteAndGetVO(Long uid, Long[] ids); // 删除自己的标签

}
