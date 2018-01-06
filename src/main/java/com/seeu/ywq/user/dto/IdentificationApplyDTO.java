package com.seeu.ywq.user.dto;

import com.seeu.ywq.resource.model.Image;
import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import java.util.Date;

public class IdentificationApplyDTO {
    private Long uid;
    private String name;
    private String phone;
    private String wechat;
    private String email;
    private String idCardNum;
    private String notes;
    private Image frontIdCardImage;
    private Image backIdCardImage;
    private Image transferVoucherImage;
    private String transferVoucherSerialNumber;
    private Long identificationId;
    private Date createTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Image getFrontIdCardImage() {
        return frontIdCardImage;
    }

    public void setFrontIdCardImage(Image frontIdCardImage) {
        this.frontIdCardImage = frontIdCardImage;
    }

    public Image getBackIdCardImage() {
        return backIdCardImage;
    }

    public void setBackIdCardImage(Image backIdCardImage) {
        this.backIdCardImage = backIdCardImage;
    }

    public Image getTransferVoucherImage() {
        return transferVoucherImage;
    }

    public void setTransferVoucherImage(Image transferVoucherImage) {
        this.transferVoucherImage = transferVoucherImage;
    }

    public String getTransferVoucherSerialNumber() {
        return transferVoucherSerialNumber;
    }

    public void setTransferVoucherSerialNumber(String transferVoucherSerialNumber) {
        this.transferVoucherSerialNumber = transferVoucherSerialNumber;
    }

    public Long getIdentificationId() {
        return identificationId;
    }

    public void setIdentificationId(Long identificationId) {
        this.identificationId = identificationId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
