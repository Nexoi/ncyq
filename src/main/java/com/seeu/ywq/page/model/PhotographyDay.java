package com.seeu.ywq.page.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "ywq_photography_day")
public class PhotographyDay {
    @Id
    private Long uid;
    @NotNull
    private Date terminateTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Date getTerminateTime() {
        return terminateTime;
    }

    public void setTerminateTime(Date terminateTime) {
        this.terminateTime = terminateTime;
    }
}
