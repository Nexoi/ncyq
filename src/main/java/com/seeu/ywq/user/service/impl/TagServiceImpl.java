package com.seeu.ywq.user.service.impl;

import com.seeu.ywq.user.dvo.User$TagVO;
import com.seeu.ywq.user.dvo.TagVO;
import com.seeu.ywq.user.model.Tag;
import com.seeu.ywq.user.model.User$Tag;
import com.seeu.ywq.user.repository.User$TagRepository;
import com.seeu.ywq.user.repository.TagRepository;
import com.seeu.ywq.user.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Resource
    private TagRepository tagRepository;
    @Resource
    private User$TagRepository user$TagRepository;

    @Override
    public TagVO findOne(Long id) {
        return transferToVO(tagRepository.findByIdAndDeleteFlag(id, Tag.DELETE_FLAG.show));
    }

    @Override
    public List<TagVO> findAll() {
        return transferToVO(tagRepository.findAllByDeleteFlag(Tag.DELETE_FLAG.show));
    }

    @Override
    public Page findAll(Pageable pageable) {
        Page page = tagRepository.findAllByDeleteFlag(Tag.DELETE_FLAG.show, pageable);
        if (page.getTotalElements() == 0) return page;
        List<TagVO> vos = transferToVO(page.getContent());
        return new PageImpl(vos, pageable, page.getTotalElements());
    }

    @Override
    public TagVO add(Tag tag) {
        return transferToVO(tagRepository.save(tag));
    }

    @Override
    public TagVO update(Tag tag) {
        if (tag.getId() == null) return null;
        Tag existTag = tagRepository.findByIdAndDeleteFlag(tag.getId(), Tag.DELETE_FLAG.show);
        if (existTag == null) return null;
        BeanUtils.copyProperties(tag, existTag);
        return transferToVO(tagRepository.save(existTag));
    }

    @Override
    public void deleteFake(Long id) {
        Tag existTag = tagRepository.findByIdAndDeleteFlag(id, Tag.DELETE_FLAG.show);
        if (existTag == null) return;
        existTag.setDeleteFlag(Tag.DELETE_FLAG.delete);
    }

    @Transactional
    @Override
    public STATUS resetMine(Long uid, Long[] ids) {
        user$TagRepository.deleteAllByUid(uid);
        return addMine(uid, ids);
    }

    @Transactional
    @Override
    public STATUS resetFocus(Long uid, Long[] ids) {
        user$TagRepository.deleteAllByUid(uid);
        return addFocus(uid, ids);
    }

    @Override
    public STATUS addMine(Long uid, Long[] ids) {
        List<User$Tag> mines = new ArrayList<>();
        for (Long id : ids) {
            User$Tag mine = new User$Tag();
            mine.setCreateTime(new Date());
            mine.setTagId(id);
            mine.setUid(uid);
            mines.add(mine);
        }
        user$TagRepository.save(mines);
        return STATUS.success;
    }

    @Override
    public STATUS addFocus(Long uid, Long[] ids) {
        List<User$Tag> foci = new ArrayList<>();
        for (Long id : ids) {
            User$Tag focus = new User$Tag();
            focus.setCreateTime(new Date());
            focus.setTagId(id);
            focus.setUid(uid);
            foci.add(focus);
        }
        try {
            user$TagRepository.save(foci);
        } catch (DataIntegrityViolationException exception) {
            return STATUS.no_such_tag;
        }
        return STATUS.success;
    }

    @Override
    public List<User$TagVO> findAllMine(Long uid) {
        List<Object[]> mines = user$TagRepository.findAllTagsByUid(uid);
        if (mines == null || mines.size() == 0) return new ArrayList<>();
        List<User$TagVO> vos = new ArrayList<>();
        for (Object[] objects : mines) {
            if (objects == null) continue;
            User$TagVO vo = new User$TagVO();
            vo.setTagId(Long.parseLong(objects[0].toString()));
            vo.setTagName(objects[1].toString());
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<User$TagVO> findAllFocus(Long uid) {
        List<Object[]> foci = user$TagRepository.findAllTagsByUid(uid);
        if (foci == null || foci.size() == 0) return new ArrayList<>();
        List<User$TagVO> vos = new ArrayList<>();
        for (Object[] objects : foci) {
            if (objects == null) continue;
            User$TagVO vo = new User$TagVO();
            vo.setTagId(Long.parseLong(objects[0].toString()));
            vo.setTagName(objects[1].toString());
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<User$TagVO> deleteMine(Long uid, Long[] ids) {
        List<User$Tag> mines = new ArrayList<>();
        for (Long id : ids) {
            if (id == null) continue;
            User$Tag mine = new User$Tag();
            mine.setUid(uid);
            mine.setTagId(id);
            mine.setCreateTime(new Date());
            mines.add(mine);
        }
        user$TagRepository.delete(mines);
        return findAllMine(uid);
    }

    @Override
    public List<User$TagVO> deleteFocus(Long uid, Long[] ids) {
        List<User$Tag> foci = new ArrayList<>();
        for (Long id : ids) {
            if (id == null) continue;
            User$Tag focus = new User$Tag();
            focus.setUid(uid);
            focus.setTagId(id);
            focus.setCreateTime(new Date());
            foci.add(focus);
        }
        user$TagRepository.delete(foci);
        return findAllFocus(uid);
    }


    private TagVO transferToVO(Tag tag) {
        if (tag == null) return null;
        TagVO vo = new TagVO();
        vo.setId(tag.getId());
        vo.setTagName(tag.getTagName());
        return vo;
    }

    private List<TagVO> transferToVO(List<Tag> tags) {
        if (tags == null || tags.size() == 0) return new ArrayList<>();
        List<TagVO> vos = new ArrayList<>();
        for (Tag tag : tags) {
            vos.add(transferToVO(tag));
        }
        return vos;
    }
}
