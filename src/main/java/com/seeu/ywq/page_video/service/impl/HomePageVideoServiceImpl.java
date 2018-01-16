package com.seeu.ywq.page_video.service.impl;

import com.seeu.third.filestore.FileUploadService;
import com.seeu.ywq.exception.ResourceAddException;
import com.seeu.ywq.exception.ResourceNotFoundException;
import com.seeu.ywq.page.dvo.HomePageVOVideo;
import com.seeu.ywq.user.dvo.SimpleUserVO;
import com.seeu.ywq.utils.AppVOUtils;
import com.seeu.ywq.page_video.model.HomePageVideo;
import com.seeu.ywq.page_video.repository.HomePageVideoRepository;
import com.seeu.ywq.page_video.service.HomePageVideoService;
import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.resource.model.Video;
import com.seeu.ywq.resource.service.ImageService;
import com.seeu.ywq.resource.service.VideoService;
import com.seeu.ywq.userlogin.service.UserReactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
public class HomePageVideoServiceImpl implements HomePageVideoService {
    @Resource
    private HomePageVideoRepository repository;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserReactService userReactService;
    @Autowired
    private AppVOUtils appVOUtils;

    @Override
    public HomePageVideo findOne(Long videoId) {
        HomePageVideo video = repository.findOne(videoId);
        if (video != null) repository.viewItOnce(videoId);
        return video;
    }

    @Override
    public Page findAllByUid(Long uid, Pageable pageable) {
        return repository.findAllByUid(uid, pageable);
    }

    @Override
    public HomePageVideo addVideo(MultipartFile videoFile, MultipartFile coverImage, Long uid, String title, HomePageVideo.CATEGORY category, Integer orderId) throws ResourceAddException {
        try {
            // video
            Video video = fileUploadService.uploadVideo(videoFile);
            Image image = fileUploadService.uploadImage(coverImage);
            video.setCoverUrl(image.getImageUrl()); // 设定一张封面
            // cover
            Video savedVideo = videoService.save(video);
            Image savedImage = imageService.save(image);
            HomePageVideo pageVideo = new HomePageVideo();
            pageVideo.setCategory(category);
            pageVideo.setUid(uid);
            pageVideo.setTitle(title);
            pageVideo.setViewNum(0l);
            pageVideo.setDeleteFlag(HomePageVideo.DELETE_FLAG.show);
            pageVideo.setVideo(savedVideo);
            pageVideo.setCoverImage(savedImage);
            pageVideo.setCreateTime(new Date());
            return repository.save(pageVideo);
        } catch (IOException e) {
            throw new ResourceAddException("Resource add exception: 视频上传失败！");
        }
    }

    @Override
    public void deleteVideo(Long videoId) throws ResourceNotFoundException {
        HomePageVideo video = repository.findOne(videoId);
        if (video == null || video.getDeleteFlag() != HomePageVideo.DELETE_FLAG.show)
            throw new ResourceNotFoundException("Can not found Resource[Video:" + videoId + "]");
        video.setDeleteFlag(HomePageVideo.DELETE_FLAG.delete);
        repository.saveAndFlush(video);
    }

    @Override
    public List<HomePageVOVideo> getVideo_HD() {
        return getVideo_HD(null);
    }

    @Override
    public List<HomePageVOVideo> getVideo_VR() {
        return getVideo_VR(null);
    }

    @Override
    public List<HomePageVOVideo> getVideo_HD(Long visitorUid) {
        return formVOs(visitorUid, HomePageVideo.CATEGORY.hd.ordinal());
    }

    @Override
    public List<HomePageVOVideo> getVideo_VR(Long visitorUid) {
        return formVOs(visitorUid, HomePageVideo.CATEGORY.vr.ordinal());
    }

    private List<HomePageVOVideo> formVOs(Long visitorUid, Integer category) {
        List list = repository.findThemByCategory(category);
        List<HomePageVOVideo> voVideos = formVideoVO(list);
        if (voVideos == null || voVideos.size() == 0) return new ArrayList<>();
        List<Long> uids = new ArrayList<>();
        for (HomePageVOVideo voVideo : voVideos) {
            Long uid = voVideo.getUid();
            if (uid != null) uids.add(uid);
        }
        // 查询用户信息
        List<SimpleUserVO> users = userReactService.findAllSimpleUsers(visitorUid, uids);
        if (users == null || users.size() == 0) return voVideos;
        Map<Long, SimpleUserVO> map = new HashMap<>();
        for (SimpleUserVO vo : users) {
            map.put(vo.getUid(), vo);
        }
        // 装载信息
        for (HomePageVOVideo voVideo : voVideos) {
            voVideo.setUser(map.get(voVideo.getUid()));
            voVideo.setUid(null);// 清除不必要的信息
        }
        return voVideos;
    }


    private HomePageVOVideo formVideoVO(Object[] objects) {
        if (objects == null || objects.length != 19) return null;// 长度必须是 19 个
        HomePageVOVideo vo = new HomePageVOVideo();
        vo.setId(appVOUtils.parseLong(objects[0]));
        vo.setCategory(appVOUtils.parseCATEGORY(objects[1]));
        vo.setTitle(appVOUtils.parseString(objects[2]));
        vo.setUid(appVOUtils.parseLong(objects[3]));
        vo.setTitle(appVOUtils.parseString(objects[4]));
        vo.setHeadIconUrl(appVOUtils.parseString(objects[5]));
        vo.setViewNum(appVOUtils.parseLong(objects[6]));
        vo.setCreateTime(appVOUtils.parseDate(objects[7]));
        Image image = new Image();
        image.setId(appVOUtils.parseLong(objects[8]));
        image.setHeight(appVOUtils.parseInt(objects[9]));
        image.setWidth(appVOUtils.parseInt(objects[10]));
        image.setImageUrl(appVOUtils.parseString(objects[11]));
        image.setThumbImage100pxUrl(appVOUtils.parseString(objects[12]));
        image.setThumbImage200pxUrl(appVOUtils.parseString(objects[13]));
        image.setThumbImage300pxUrl(appVOUtils.parseString(objects[14]));
        image.setThumbImage500pxUrl(appVOUtils.parseString(objects[15]));
        Video video = new Video();
        video.setId(appVOUtils.parseLong(objects[16]));
        video.setCoverUrl(appVOUtils.parseString(objects[17]));
        video.setSrcUrl(appVOUtils.parseString(objects[18]));

        vo.setCoverImage(image);
        vo.setVideo(video);
        return vo;
    }

    private List<HomePageVOVideo> formVideoVO(List<Object[]> objects) {
        if (objects == null || objects.size() == 0) return new ArrayList<>();
        List<HomePageVOVideo> vos = new ArrayList<>();
        for (Object[] object : objects) {
            vos.add(formVideoVO(object));
        }
        return vos;
    }
}
