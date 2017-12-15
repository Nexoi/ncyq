package com.seeu.file.aliyun_storage;

import com.seeu.ywq.user.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageImageService {


    Result saveImages(MultipartFile[] files) throws Exception;


    public class Result {
        public enum STATUS {
            success,
            failure,
            exception
        }

        private STATUS status;
        private List<Image> imageList;
        private int imageNum;

        public STATUS getStatus() {
            return status;
        }

        public void setStatus(STATUS status) {
            this.status = status;
        }

        public List<Image> getImageList() {
            return imageList;
        }

        public void setImageList(List<Image> imageList) {
            this.imageList = imageList;
        }

        public int getImageNum() {
            return imageNum;
        }

        public void setImageNum(int imageNum) {
            this.imageNum = imageNum;
        }
    }
}
