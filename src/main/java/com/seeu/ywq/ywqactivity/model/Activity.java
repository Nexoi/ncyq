package com.seeu.ywq.ywqactivity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
//@Table(name = "ywq_activity")
public class Activity {
    @Id
    private Long id;
    private String title;
    private String subTitle;
    @Column(length = 400)
    private String url;

}
