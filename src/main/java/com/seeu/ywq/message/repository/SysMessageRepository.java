package com.seeu.ywq.message.repository;

import com.seeu.ywq.message.model.SysMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 26/01/2018
 * Time: 4:41 PM
 * Describe:
 */

public interface SysMessageRepository extends JpaRepository<SysMessage, Long> {

    List<SysMessage> findAllByCreateTimeAfter(@Param("createTime") Date createTime);


    Integer countAllByCreateTimeAfter(@Param("createTime") Date createTime);
}
