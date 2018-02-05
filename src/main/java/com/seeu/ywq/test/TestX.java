package com.seeu.ywq.test;

import javax.persistence.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 05/02/2018
 * Time: 11:00 AM
 * Describe:
 */
@Entity
@Table(name = "testx")
public class TestX {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 1024)
    private String info;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
