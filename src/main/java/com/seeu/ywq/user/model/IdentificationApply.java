package com.seeu.ywq.user.model;

import com.seeu.ywq.resource.model.Image;
import io.swagger.annotations.ApiParam;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@IdClass(IdentificationApplyPKeys.class)
@Table(name = "ywq_identification_apply", indexes = {
        @Index(name = "identification_apply_index1", columnList = "uid,identification_id")
})
public class IdentificationApply {

    @ApiParam(hidden = true)
    @Id
    @Column(name = "uid",unique = false)
    private Long uid;

    @Id
    @NotNull
    @Column(name = "identification_id",unique = false)
    private Long identificationId;
    @NotNull
    private String name;
    @NotNull
    private String phone;
    @NotNull
    private String wechat;
    @NotNull
    private String email;
    @NotNull
    private String idCardNum;
    private String notes;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "front_id", referencedColumnName = "id")
    private Image frontIdCardImage;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "back_id", referencedColumnName = "id")
    private Image backIdCardImage;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", referencedColumnName = "id")
    private Image transferVoucherImage;

    @ApiParam(hidden = true)
    private String transferVoucherSerialNumber;

    @ApiParam(hidden = true)
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getIdentificationId() {
        return identificationId;
    }

    public void setIdentificationId(Long identificationId) {
        this.identificationId = identificationId;
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
}
