package com.seeu.ywq.search.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by suneo.
 * User: neo
 * Date: 30/01/2018
 * Time: 3:23 PM
 * Describe:
 */
@Entity
@Table(name = "ywq_search_hot_words")
public class HotWord {
    @Id
    @Column(length = 20)
    private String word;

    private Date createTime;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
