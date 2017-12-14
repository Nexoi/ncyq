package com.seeu.ywq.bean;

import javax.persistence.*;

/**
 * 标签表
 *
 * @author Scary
 */
@Entity
@Table(name = "label", indexes = {
        @Index(name = "label_index1", columnList = "label_no"),
        @Index(name = "label_index2", columnList = "label_name")

})
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//主键

    @Column(name = "label_no")
    private Long labelNo;//标签号
    @Column(name = "label_name")
    private String labelName;//标签名

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(Long labelNo) {
        this.labelNo = labelNo;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }


}
