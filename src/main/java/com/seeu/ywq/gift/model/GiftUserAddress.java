package com.seeu.ywq.gift.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户收货地址，一次订单一次地址
 */
@Entity
@Table(name = "ywq_gift_user_address")
public class GiftUserAddress {
    @Id
    private Long orderId;
    private Long receiveUid;
    private String province;
    private String city;
    private String detail;
    private String postcode;
    private String phone;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getReceiveUid() {
        return receiveUid;
    }

    public void setReceiveUid(Long receiveUid) {
        this.receiveUid = receiveUid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
