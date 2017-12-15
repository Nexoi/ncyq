package com.seeu.ywq.user.service;

import com.seeu.ywq.user.model.Picture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UserAlbumService {
    /**
     * @param targetUid 目标用户
     * @param myUid     登陆用户
     * @return
     */
    Page findAllByUid(Long targetUid, Long myUid, PageRequest pageRequest);

    /**
     * @param targetUid 目标用户【本人】
     * @return
     */
    Page findAllByUid(Long targetUid, Picture.ALBUM_TYPE albumType, PageRequest pageRequest);
}
