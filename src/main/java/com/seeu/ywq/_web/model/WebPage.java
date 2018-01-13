package com.seeu.ywq._web.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ywq_web_page")
@IdClass(WebPagePKeys.class)
public class WebPage {

    public enum TYPE {
        activity,
        publish,
        help
    }

    public enum DELETE_FLAG {
        show,
        delete
    }

    @Id
    private Long id;
    @Id
    @Enumerated
    private TYPE type;
    @Enumerated
    private DELETE_FLAG deleteFlag;
    @Column(length = Integer.MAX_VALUE)
    private String htmlContent;
    private Date updateTime;
}
