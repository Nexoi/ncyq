package com.seeu.ywq.user.service;

import com.seeu.ywq.user.dto.PictureVO;
import com.seeu.ywq.user.dto.PublishVO;
import com.seeu.ywq.user.model.Publish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PublishService {

    PublishVO transferToVO(Publish publish, Long uid);

    List<PublishVO> transferToVO(List<Publish> publishs, Long uid);

    //【匿名】
    PublishVO transferToVO(Publish publish);

    //【匿名】
    List<PublishVO> transferToVO(List<Publish> publishs);

    /**
     * 根据不同的 uid 访问不同的相册图片时候会得到不同的图片地址，图片组具体信息被置空
     *
     * @param publishId
     * @param uid
     * @return
     */
    Page findAllByPublishId(Long publishId, Long uid, Pageable pageable);

    /**
     * 匿名访问动态，私密相册照片必须全部为模糊照片
     *
     * @param uid
     * @return
     */
    Page findAllByUid(Long uid, Pageable pageable);

    /**
     * 匿名用户
     *
     * @param publishId
     * @return
     */
    PublishVO findOneByPublishId(Long publishId);

    /**
     * 登陆用户
     *
     * @param publishId
     * @param uid
     * @return
     */
    PublishVO findOneByPublishId(Long publishId, Long uid);

}
