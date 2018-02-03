package com.seeu.ywq.trend_lite.service.impl;

import com.seeu.ywq.trend.model.PublishAudio;
import com.seeu.ywq.trend_lite.dvo.PublishLiteVO;
import com.seeu.ywq.trend_lite.dvo.PublishLiteVOAudio;
import com.seeu.ywq.trend_lite.dvo.PublishLiteVOPicture;
import com.seeu.ywq.trend_lite.dvo.PublishLiteVOVideo;
import com.seeu.ywq.user.dvo.SimpleUserVO;
import com.seeu.ywq.utils.AppVOUtils;
import com.seeu.ywq.resource.model.Video;
import com.seeu.ywq.resource.service.ResourceAuthService;
import com.seeu.ywq.trend.model.Picture;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.trend.model.PublishVideo;
import com.seeu.ywq.trend.service.PublishVideoService;
import com.seeu.ywq.trend_lite.model.PublishLite;
import com.seeu.ywq.trend_lite.repository.PublishLiteRepository;
import com.seeu.ywq.trend_lite.service.PublishLiteService;
import com.seeu.ywq.user.service.UserPictureService;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class PublishLiteServiceImpl implements PublishLiteService {

    @Autowired
    private UserPictureService userPictureService;
    @Autowired
    private ResourceAuthService resourceAuthService;
    @Resource
    private PublishLiteRepository publishLiteRepository;
    @Autowired
    private UserReactService userReactService;
    @Autowired
    private PublishVideoService publishVideoService;
    @Autowired
    private AppVOUtils appVOUtils;

    // 推荐
    @Override
    public Page<PublishLiteVO> findAllByTagIds(Long visitorUid, Pageable pageable, Long... ids) {
        if (ids == null || ids.length == 0) return new PageImpl<>(new ArrayList<>());
        List list = publishLiteRepository.queryItUseMyTags(visitorUid, Arrays.asList(ids), pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        Integer totalSize = publishLiteRepository.countItUseMyTags(Arrays.asList(ids));
        list = formPublishLite(list);
        completePicturesAndSimpleUsers(visitorUid, list); // 加载图片
        List transferList = transferToVO(list, visitorUid);
        return new PageImpl<>(transferList, pageable, totalSize);
    }


    // 关注
    @Override
    public Page<PublishLiteVO> findAllByFollowedUids(Long visitorUid, Pageable pageable, Long... uids) {
        if (uids == null || uids.length == 0) return new PageImpl<>(new ArrayList<>());
        List list = publishLiteRepository.queryItUseFollowedUids(visitorUid, Arrays.asList(uids), pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        Integer totalSize = publishLiteRepository.countItUseFollowedUids(Arrays.asList(uids));
        list = formPublishLite(list);
        completePicturesAndSimpleUsers(visitorUid, list); // 加载图片
        List transferList = transferToVO(list, visitorUid);
        return new PageImpl<>(transferList, pageable, totalSize);
    }

    // xx的动态
    @Override
    public Page<PublishLiteVO> findAllByUid(Long visitorUid, Long uid, Pageable pageable) {
        Page page = publishLiteRepository.findAllByUidAndStatus(uid, Publish.STATUS.normal, pageable);
        List<PublishLite> list = page.getContent();
        // TODO
        completePicturesAndSimpleUsers(visitorUid, list); // 加载图片
        if (list.size() == 0) return page;
        List transferList = transferToVO(list, visitorUid);
        return new PageImpl<>(transferList, pageable, page.getTotalElements());
    }

    @Override
    public Page<PublishLiteVO> search(Long visitorUid, String word, Pageable pageable) {
        if (word == null) return new PageImpl<>(new ArrayList<>());
        word = "%" + word + "%";
        Page page = publishLiteRepository.findAllByTextLikeOrLabelsLikeAndStatusOrderByLikeNumDesc(word, word, Publish.STATUS.normal, pageable);
        completePicturesAndSimpleUsers(visitorUid, page.getContent());
        List transferList = transferToVO(page.getContent(), visitorUid);
        return new PageImpl<>(transferList, pageable, page.getTotalElements());
    }

    // 将图片、个人信息加载进 vo
    private void completePicturesAndSimpleUsers(Long visitorUid, List<PublishLite> vos) {
        if (vos == null || vos.size() == 0) return;
        // hash
        HashMap<Long, PublishLite> map = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (PublishLite vo : vos) {
            vo.setPictures(new ArrayList<>()); // 清空原有图片数据
            ids.add(vo.getId());
            userIds.add(vo.getUid());
            map.put(vo.getId(), vo);
        }
        // Users
        List<SimpleUserVO> simpleUsers = userReactService.findAllSimpleUsers(visitorUid, userIds);
        HashMap<Long, SimpleUserVO> userMap = new HashMap();
        for (SimpleUserVO vo : simpleUsers) {
            userMap.put(vo.getUid(), vo);
        }
        for (PublishLite vo : vos) {
            vo.setUser(userMap.get(vo.getUid()));
        }
        // Pictures
        List<Picture> pictures = userPictureService.findAllByPublishIds(ids);
        if (pictures == null || pictures.size() == 0)
            return;
        for (Picture picture : pictures) {
            if (picture == null) continue;
            List pictureList = map.get(picture.getPublishId()).getPictures();
            if (pictureList == null) map.get(picture.getPublishId()).setPictures(pictureList = new ArrayList<>());
            pictureList.add(picture);
        }

    }

    private PublishLiteVO transferToVO(PublishLite publish, boolean canVisitClosedResource) {
        if (publish == null) return null;
        switch (publish.getType()) {
            case picture:
                PublishLiteVOPicture vop = new PublishLiteVOPicture();
                BeanUtils.copyProperties(publish, vop);
                vop.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vop.setCoverPictureUrl(publish.getPictures() == null || publish.getPictures().size() == 0 ? null : userPictureService.transferToVO(publish.getPictures().get(0), canVisitClosedResource));
                vop.setPictures(userPictureService.transferToVO(publish.getPictures(), canVisitClosedResource));
                vop.setUnlockPrice(publish.getUnlockPrice());
                return vop;
            case video:
                PublishLiteVOVideo vod = new PublishLiteVOVideo();
                BeanUtils.copyProperties(publish, vod);
                vod.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vod.setVideo(publishVideoService.transferToVO(publish.getVideo(), canVisitClosedResource));
                vod.setUnlockPrice(publish.getUnlockPrice());
                // TODO video 权限得加 checked 2018-01-12 night
                return vod;
            case audio:
                PublishLiteVOAudio voa = new PublishLiteVOAudio();
                BeanUtils.copyProperties(publish, voa);
                voa.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                voa.setAudio(publish.getAudio());
                return voa;
            case word:
            default:
                PublishLiteVO vo = new PublishLiteVO();
                BeanUtils.copyProperties(publish, vo);
                vo.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                return vo;
        }
    }

    private List<PublishLiteVO> transferToVO(List<PublishLite> publishs, Long visitorUid) {
        List<PublishLiteVO> vos = new ArrayList<>();
        for (PublishLite publish : publishs) {
            if (publish == null) continue;
            boolean canVisit = publish.getType() == null
                    || publish.getUnlockPrice() == null
                    || publish.getUnlockPrice().longValue() <= 0
                    || publish.getType() == Publish.PUBLISH_TYPE.word
                    || publish.getType() == Publish.PUBLISH_TYPE.audio
                    || visitorUid.equals(publish.getUid())
                    || resourceAuthService.canVisitPublish(visitorUid, publish.getId());
            publish.setStatus(null); // 消除不必要的数据
            vos.add(transferToVO(publish, canVisit));
        }
        return vos;
    }


    private PublishLite formPublishLite(Object[] objects) {
        if (objects == null || objects.length != 19) return null;// 长度必须是 19 个
        PublishLite vo = new PublishLite();
        vo.setId(appVOUtils.parseLong(objects[0]));
        vo.setUid(appVOUtils.parseLong(objects[1]));
        vo.setWeight(appVOUtils.parseInt(objects[2]));
        vo.setType(appVOUtils.paresPUBLISH_TYPE(objects[3]));
        vo.setTitle(appVOUtils.parseString(objects[4]));
        vo.setCreateTime(appVOUtils.parseDate(objects[5]));
        vo.setUnlockPrice((appVOUtils.parseDouble(objects[6])).longValue());
        vo.setViewNum(appVOUtils.parseInt(objects[7]));
        vo.setCommentNum(appVOUtils.parseInt(objects[8]));
        vo.setLikeNum(appVOUtils.parseInt(objects[9]));
        vo.setLabels(appVOUtils.parseString(objects[10]));
        vo.setText(appVOUtils.parseString(objects[11]));
        // 视频更多详情信息可在此补足
        Video video = new Video();
        video.setId(appVOUtils.parseLong(objects[12]));
        video.setCoverUrl(appVOUtils.parseString(objects[13]));
        video.setSrcUrl(appVOUtils.parseString(objects[14]));
        PublishVideo publishVideo = new PublishVideo();
        publishVideo.setVideo(video);
        vo.setVideo(publishVideo);
        vo.setPictures(new ArrayList<>());
        // 新增动态收入信息
        vo.setReceivedDiamonds(appVOUtils.parseLong(objects[15]));
        // 音频信息
        PublishAudio audio = new PublishAudio();
        audio.setAudioUrl(appVOUtils.parseString(objects[16]));
        audio.setAudioSecond(appVOUtils.parseLong(objects[17]));
        vo.setAudio(audio);
        // 是否喜欢该动态
        vo.setLikedIt(1 == appVOUtils.parseInt(objects[18]));
        return vo;
    }

    private List<PublishLite> formPublishLite(List<Object[]> objects) {
        if (objects == null || objects.size() == 0) return new ArrayList<>();
        List<PublishLite> list = new ArrayList();
        for (Object[] object : objects) {
            list.add(formPublishLite(object));
        }
        return list;
    }
}
